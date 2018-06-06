/*
 * Copyright (c) 2016 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.utilities.xml.editor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple implementation of an InLineXMLEditor that does not recognize and handle variables. It is used internally
 * by the InLineXMLEditor implementation that is capable of handling variables.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class BasicInLineXmlEditor implements InLineXmlEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(BasicInLineXmlEditor.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Package Protected Static ----------------------------------------------------------------------------------------

    /**
     * Normalizes the path, makes sure it starts with a "/", etc.
     */
    static String normalize(String path) {

        List<String> tokens = new ArrayList<>();

        String crt = "";

        for(int i = 0; i < path.length(); i ++) {

            char c = path.charAt(i);

            if (c == '/') {

                if (i != 0) {

                    tokens.add(crt);
                    crt = "";
                }
            }
            else {

                crt += c;
            }
        }

        if (crt.length() > 0) {

            tokens.add(crt);
        }

        String s = "/";

        for(Iterator<String> i = tokens.iterator(); i.hasNext(); ) {

            s += i.next();

            if (i.hasNext())  {

                s += '/';
            }
        }

        return s;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private File xmlFile;
    private LineBasedContent content;
    private XMLInputFactory staxFactory;

    // may be null if there's nothing to undo
    private byte[] undoContent;

    private boolean moreThanOneConsequentialSave;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Fails early. Performs as many verifications it can (file exists, can be read, can be written) at this stage and
     * fails if the preconditions aren't met.
     *
     * @throws java.io.IOException
     */
    public BasicInLineXmlEditor(File xmlFile) throws IOException {

        if (xmlFile == null) {
            throw new IllegalArgumentException("null xml file");
        }

        if (!xmlFile.isFile()) {
            throw new IOException("file " + xmlFile + " does not exist");
        }

        if (!xmlFile.canRead()) {
            throw new IOException("file " + xmlFile + " cannot be read");
        }

        if (!xmlFile.canWrite()) {
            throw new IOException("file " + xmlFile + " cannot be written");
        }

        this.xmlFile = xmlFile;
        this.content = new LineBasedContent();

        loadFromDisk();

        // nothing to undo so far
        undoContent = null;

        staxFactory = XMLInputFactory.newFactory();
    }

    // InLineXMLEditor implementation ----------------------------------------------------------------------------------

    @Override
    public File getFile() {

        return xmlFile;
    }

    @Override
    public int getLineCount() {
        return content.getLineCount();
    }

    @Override
    public boolean isDirty() {
        return content.isDirty();
    }

    @Override
    public String getContent() {
        return content.getText();
    }

    @Override
    public String get(String path) {

        List<String> matches = getList(path);

        if (matches.isEmpty()) {
            return null;
        }

        return matches.get(0);
    }

    @Override
    public List<String> getList(String path) {

        List<String> result = new ArrayList<>();

        List<XmlContext> matches = collectMatches(path);

        for(XmlContext c: matches) {

            XMLEvent current = c.getCurrent();

            if (!current.isCharacters()) {
                throw new RuntimeException("NOT YET IMPLEMENTED");
            }

            Characters characters = current.asCharacters();
            String value = characters.getData().trim();
            result.add(value);
        }

        return result;
    }

    @Override
    public List<XmlElement> getElements(String path) {

        List<XmlElement> children = new ArrayList<>();

        // walk the XML content and collect element names and values on match

        walk(path, false, (String normalizedXmlPath, XMLEvent previous, XMLEvent current) -> {

            //
            // exact match or the normalized XML path starts with the given path
            //

            if (normalizedXmlPath.equals(path) || !previous.isStartElement()) {

                //
                // ignore the exact match, or non start elements
                //

                return;
            }

            StartElement se = previous.asStartElement();
            String name = se.getName().getLocalPart();

            if (!current.isCharacters()) {
                throw new RuntimeException(
                        "NOT YET IMPLEMENTED (1): cannot process embedded elements yet (" + normalizedXmlPath + ")");
            }

            String value = current.asCharacters().getData().trim();

            if (value.isEmpty()) {
                throw new RuntimeException(
                        "NOT YET IMPLEMENTED (2): cannot process embedded elements yet (" + normalizedXmlPath + ")");
            }

            XmlElement element = new XmlElement(name, value);
            children.add(element);
        });

        return children;
    }

    @Override
    public boolean set(String path, String newValue) {

        List<XmlContext> matches = collectMatches(path);

        if (matches.isEmpty()) {

            throw new RuntimeException("NOT YET IMPLEMENTED: we did not find element and we don't know how to add");
        }

        //
        // set the first match only
        //

        XmlContext match = matches.get(0);
        XMLEvent previous = match.getPrevious();
        XMLEvent current = match.getCurrent();

        if (!current.isCharacters()) {
            throw new RuntimeException("NOT YET IMPLEMENTED");
        }

        Characters characters = current.asCharacters();

        String oldValue = characters.getData();

        Location startElementLocation = previous.getLocation();
        int zeroBasedLineNumber = startElementLocation.getLineNumber() - 1;
        int zeroBasedPositionInLine = startElementLocation.getColumnNumber() - 1;

        //
        // account for multi-line character sequences, and for the fact that the non-blank value may be preceded and
        // trailed by blanks (spaces, tabs, etc.); we want to preserve leading and trailing blank areas (including new
        // lines)
        //
        // TODO multi-line support not complete, we won't handle well cases when the value contains one or more new lines
        //

        int lineCountOffset = 0;
        String leadingBlankSpace = "";
        String trailingBlankSpace = "";
        int trimmedOldValueFrom =0;
        int trimmedOldValueTo = oldValue.length();
        String trimmedOldValue;

        //
        // start with the front of the string, extract leading blank space
        //

        for(int i = 0; i < oldValue.length(); i ++) {

            char c = oldValue.charAt(i);

            if (c == ' ' || c == '\t') {
                leadingBlankSpace += c;
            }
            else if (c == '\n')  {

                // TODO: also handle '\r\n' for Windows-generated XML

                leadingBlankSpace = "";
                lineCountOffset++;
                //
                // also change zero-based position in line, as where the preceding element ends is irrelevant now
                //
                zeroBasedPositionInLine = 0;
            }
            else {

                //
                // continue at the back of the string
                //
                trimmedOldValueFrom = i;
                break;
            }
        }

        //
        // continue from the back of the string, extract the trailing blank space
        //

        for(int i = oldValue.length() - 1; i >= trimmedOldValueFrom; i --) {

            char c = oldValue.charAt(i);

            if (c == ' ' || c == '\t') {
                trailingBlankSpace = c + trailingBlankSpace;
            }
            else if (c == '\n')  {

                // TODO: also handle '\r\n' for Windows-generated XML

                trailingBlankSpace = "";
            }
            else {

                trimmedOldValueTo = i + 1;
                break;
            }
        }

        trimmedOldValue = oldValue.substring(trimmedOldValueFrom, trimmedOldValueTo);

        //
        // check for the old value part that matters
        //

        if (trimmedOldValue.equals(newValue)) {

            //
            // no replacement necessary
            //

            return false;
        }

        //
        // values are different, replace, but preserve the leading and trailing blanks
        //

        oldValue = leadingBlankSpace + trimmedOldValue + trailingBlankSpace;
        newValue = leadingBlankSpace + newValue + trailingBlankSpace;
        zeroBasedLineNumber = zeroBasedLineNumber + lineCountOffset;

        //
        // undo state management; save previous state in case we need to undo
        //

        // this is an non-expensive operation, the underlying implementation caches content
        byte[] transientUndoContent = content.getText().getBytes();

        boolean replaced = content.replace(
                zeroBasedLineNumber, zeroBasedPositionInLine, zeroBasedPositionInLine + oldValue.length(), newValue);

        if (replaced && (moreThanOneConsequentialSave || undoContent == null)) {
            // we do this only on the first change or if the content was already overwritten on disk by a previous
            // consequential save and we need to reset the state undo will revert to, if invoked.
            undoContent = transientUndoContent;
        }

        //
        // end of undo state management
        //

        return replaced;
    }

    @Override
    public boolean save() throws IOException {

        if (!isDirty()) {

            log.debug("the content is not dirty, save is non consequential");
            return false;
        }

        BufferedOutputStream bos = null;

        try {

            bos = new BufferedOutputStream(new FileOutputStream(xmlFile));
            content.write(bos);

            log.debug(this.getFile() + " saved");

            moreThanOneConsequentialSave = true;

            return true;
        }
        finally {

            if (bos != null) {
                bos.close();
            }
        }
    }

    @Override
    public boolean undo() throws IOException {

        if (undoContent == null) {
            return false;
        }

        //
        // undo on disk
        //

        FileOutputStream fos = new FileOutputStream(xmlFile);
        fos.write(undoContent);
        fos.close();

        //
        // nothing to undo after undoing
        //
        undoContent = null;

        //
        // update memory
        //

        loadFromDisk();

        return true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return xmlFile + " " + (content == null ? "UNINITIALIZED" : (content.isDirty() ? "(dirty)" : "(not dirty)"));

    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Walks the XML document attempting to match the path to the XML document element paths, for as many times as
     * possible. Execute the closure upon match.
     *
     * @param exactMatch if true, the closure will be applied only in case of exact match. If false, the closure
     *                   will be applied both on exact matches and on paths that start with the given path.
     */
    void walk(String pathAsString, boolean exactMatch, XmlContextClosure closure) {

        String normalizedPath = normalize(pathAsString);

        XMLEventReader xmlReader = null;

        try {

            Reader lineBasedContentReader = new LineBasedContentReader(content);
            xmlReader = staxFactory.createXMLEventReader(lineBasedContentReader);
            XMLEvent previousEvent = null, currentEvent;

            String normalizedXmlContentPath = "";

            //
            // Apparently, hasNext() returns true after we pull END_DOCUMENT, so we don't use it at all while iterating.
            // Is this supposed to happen or is a StAX defect?
            //
            for(; !(currentEvent = xmlReader.nextEvent()).isEndDocument(); previousEvent = currentEvent) {

                //
                // iterate over the XML content repeatedly matching the path against the content
                //

                if (currentEvent.isStartElement()) {

                    StartElement se = currentEvent.asStartElement();
                    String elementName = se.getName().getLocalPart();
                    normalizedXmlContentPath += "/" + elementName;
                }
                else if (currentEvent.isEndElement()) {

                    normalizedXmlContentPath = normalizedXmlContentPath.substring(0, normalizedXmlContentPath.lastIndexOf('/'));
                }
                else {

                    if (normalizedXmlContentPath.equals(normalizedPath) ||
                            (!exactMatch && normalizedXmlContentPath.startsWith(normalizedPath))) {

                        //
                        // full path match, apply the closure
                        //
                        closure.apply(normalizedXmlContentPath, previousEvent, currentEvent);
                    }
                }
            }
        }
        catch (XMLStreamException e) {

            throw new RuntimeException("NOT YET IMPLEMENTED");
        }
        finally {

            if (xmlReader != null) {

                try {
                    xmlReader.close();
                }
                catch(XMLStreamException e) {

                    log.warn("failed to close an XML Reader: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Walks the XML document attempting to match the path to the XML document element paths, for as many times as
     * possible. Returns a list of matched contexts, in the order in which they were found, or an empty list if there
     * was no match
     */
    List<XmlContext> collectMatches(String pathAsString) {

        List<XmlContext> matches = new ArrayList<>();

        //
        // the closure simply collects the XMLContext and puts in "matches"
        //

        walk(pathAsString, true, (String normalizedXmlPath, XMLEvent previous, XMLEvent current) -> {

            //
            // avoid duplicate Characters event addition, only add the first one
            //

            if (!matches.isEmpty() && matches.get(matches.size() - 1).getCurrent().equals(previous)) {

                //
                // already added content for that path, ignore
                //

                return;
            }

            XmlContext c = new XmlContext(normalizedXmlPath, previous, current);
            matches.add(c);
        });

        return matches;
    }


//    /**
//     * Walks the XML document attempting to match the path to the XML document element paths, for as many times as
//     * possible. Returns a list of matched contexts, in the order in which they were found, or an empty list if there
//     * was no match
//     */
//    List<XMLContext> walk(String pathAsString) {
//
//        List<XMLContext> matches = new ArrayList<>();
//
//        String normalizedPath = normalize(pathAsString);
//
//        XMLEventReader xmlReader = null;
//
//        try {
//
//            Reader lineBasedContentReader = new LineBasedContentReader(content);
//            xmlReader = staxFactory.createXMLEventReader(lineBasedContentReader);
//            XMLEvent previousEvent = null, currentEvent;
//
//            String normalizedXmlContentPath = "";
//
//            //
//            // Apparently, hasNext() returns true after we pull END_DOCUMENT, so we don't use it at all while iterating.
//            // Is this supposed to happen or is a StAX defect?
//            //
//            for(; !(currentEvent = xmlReader.nextEvent()).isEndDocument(); previousEvent = currentEvent) {
//
//                //
//                // iterate over the XML content repeatedly matching the path against the content
//                //
//
//                if (currentEvent.isStartElement()) {
//
//                    StartElement se = currentEvent.asStartElement();
//                    String elementName = se.getName().getLocalPart();
//                    normalizedXmlContentPath += "/" + elementName;
//                }
//                else if (currentEvent.isEndElement()) {
//
//                    normalizedXmlContentPath = normalizedXmlContentPath.substring(0, normalizedXmlContentPath.lastIndexOf('/'));
//                }
//                else {
//
//                    if (normalizedXmlContentPath.equals(normalizedPath)) {
//
//                        //
//                        // full path match, add to context anything that follows that match
//                        //
//
//                        //
//                        // avoid duplicate Characters event addition, only add the first one
//                        //
//
//                        if (!matches.isEmpty() && matches.get(matches.size() - 1).getCurrent().equals(previousEvent)) {
//
//                            //
//                            // already added content for that path, ignore
//                            //
//
//                            continue;
//                        }
//
//                        XMLContext c = new XMLContext(normalizedXmlContentPath, previousEvent, currentEvent);
//                        matches.add(c);
//                    }
//                }
//            }
//
//            return matches;
//        }
//        catch (XMLStreamException e) {
//
//            throw new RuntimeException("NOT YET IMPLEMENTED");
//        }
//        finally {
//
//            if (xmlReader != null) {
//
//                try {
//                    xmlReader.close();
//                }
//                catch(XMLStreamException e) {
//
//                    log.warn("failed to close an XML Reader: " + e.getMessage());
//                }
//            }
//        }
//    }

    /**
     * It overwrites the current content cached in memory, if any.
     */
    void loadFromDisk() throws IOException {

        BufferedInputStream bis = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(xmlFile));
            content.read(bis);
            log.debug("loaded " + xmlFile);
        }
        finally {

            if (bis != null) {
                bis.close();
            }
        }
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
