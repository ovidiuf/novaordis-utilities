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
import java.util.StringTokenizer;

/**
 * InLineXmlEditor is an API that can be used to modify XML files on disk directly from Java programs.
 *
 * Currently NOT thread safe, must be accessed and used from a single thread at a time.
 *
 * For more details see https://kb.novaordis.com/index.php/In-Line_XML_Editor
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class InLineXmlEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(InLineXmlEditor.class);

    // Static ----------------------------------------------------------------------------------------------------------

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
    public InLineXmlEditor(File xmlFile) throws IOException {

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

    // Public ----------------------------------------------------------------------------------------------------------

    public int getLineCount() {
        return content.getLineCount();
    }

    public File getFile() {
        return xmlFile;
    }

    /**
     * Updates the value of the element/attribute indicated by the path with the given string.
     *
     * If the path does not exist, it will be created.
     *
     * @return true if an actual update occurred, false if the existing value is identical with the new value.
     */
    public boolean set(String path, String newValue) {

        XmlContext context = walk(path);

        XMLEvent previous = context.getPrevious();
        XMLEvent current = context.getCurrent();

        if (!current.isCharacters()) {
            throw new RuntimeException("NOT YET IMPLEMENTED");
        }

        Characters characters = current.asCharacters();
        String oldValue = characters.getData();

        if (oldValue.equals(newValue)) {
            return false;
        }

        //
        // values are different, replace
        //

        Location location = previous.getLocation();
        int zeroBasedLineNumber = location.getLineNumber() - 1;
        int zeroBasedPositionInLine = location.getColumnNumber() - 1;

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

    /**
     * @return the string associated with the element specified by path. May return null if no such path exists.
     */
    public String get(String path) {

        XmlContext context = walk(path);

        XMLEvent current = context.getCurrent();

        if (!current.isCharacters()) {
            throw new RuntimeException("NOT YET IMPLEMENTED");
        }

        Characters characters = current.asCharacters();
        //noinspection UnnecessaryLocalVariable
        String value = characters.getData();
        return value;
    }

    /**
     * Writes the in-memory updates (if any) into the file.
     *
     * @return true if state was changed on disk as result of the save operation, false otherwise.
     *
     * @see InLineXmlEditor#undo()
     */
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

    /**
     * Reverts the effects on disk of the <b>last</b> save() operation (if any), by restoring the underlying file
     * and the corresponding memory state to the version available before the save() operation.
     *
     * If there was no previous save() operation, undo() is a noop and returns false.
     *
     * If there was a previous save() operation, but the operation did not change state (un-consequential save()),
     * undo() will be a noop and will return false.
     *
     * If there were previous save() operations and the operations changed state on disk, undo() will revert the
     * underlying file and the corresponding memory state to the version present on disk before the <b>last</b> save()
     * operation, and will return true. All subsequent undo() calls after that (unless a new consequential save()
     * operation is performed) will be noops and will return false.
     *
     * @return true if disk state was changed as result of the last undo() operation, false otherwise.
     *
     * @see InLineXmlEditor#save()
     *
     * @throws IOException
     */
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

    public boolean isDirty() {
        return content.isDirty();
    }

    /**
     * @return the text content, as currently cached in memory. It may contain changes that are not saved on disk.
     */
    public String getContent() {
        return content.getText();
    }

    @Override
    public String toString() {

        return xmlFile + " " + (content == null ? "UNINITIALIZED" : (content.isDirty() ? "(dirty)" : "(not dirty)"));

    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Concurrently walk the path and the XML document and return the target element
     */
    XmlContext walk(String path) {

        //
        // concurrently parse the path and the content
        //

        StringTokenizer st = new StringTokenizer(path, "/");

        if (!st.hasMoreTokens()) {

            //
            // empty path
            //

            throw new RuntimeException("NOT YET IMPLEMENTED");
        }


        XMLEventReader xmlReader = null;

        try {

            Reader lineBasedContentReader = new LineBasedContentReader(content);
            xmlReader = staxFactory.createXMLEventReader(lineBasedContentReader);

            String currentPathToken = st.nextToken();

            while (true) {

                //
                // iterate over the XML content until we find a matching element/attribute
                //

                XMLEvent previous;
                XMLEvent current;

                while (xmlReader.hasNext()) {

                    current = xmlReader.nextEvent();

                    if (current.isEndDocument()) {

                        //
                        // apparently, hasNext() returns true after we pull END_DOCUMENT, is this supposed to happen
                        // or is a StAX defect?
                        //

                        //
                        // we reached the end of the document and our path is NOT depleted
                        //
                        throw new RuntimeException(
                                "NOT YET IMPLEMENTED: we did not find element <" + currentPathToken +
                                        "> in document, and we are supposed to add it, but this functionality is not implemented yet");
                    }

                    if (!current.isStartElement()) {

                        continue;
                    }

                    StartElement se = current.asStartElement();
                    String elementName = se.getName().getLocalPart();

                    if (currentPathToken.equals(elementName)) {

                        //
                        // we matched a path element
                        //

                        if (!st.hasMoreTokens()) {

                            //
                            // we're at the end of the path
                            //

                            previous = current;
                            current = xmlReader.nextEvent();
                            return new XmlContext(previous, current);
                        }

                        //
                        // ... else continue walking the path
                        //

                        currentPathToken = st.nextToken();
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
     * It overwrites the current content cached in memory, if any.
     */
    void loadFromDisk() throws IOException {

        BufferedInputStream bis = null;

        try {

            bis = new BufferedInputStream(new FileInputStream(xmlFile));
            content.read(bis);
            log.debug("loaded content of " + xmlFile + " in memory");
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
