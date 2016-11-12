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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Currently NOT thread safe, must be accessed and used from a single thread at a time.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class InLineXMLEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private File xmlFile;

    private List<Line> lines;

    private XMLInputFactory staxFactory;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Fails early. Performs as many verifications it can (file exists, can be read, can be written) at this stage and
     * fails if the preconditions aren't met.
     *
     * @param xmlFile
     * @throws java.io.IOException
     */
    public InLineXMLEditor(File xmlFile) throws IOException {

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

        lines = new ArrayList<>();

        //
        // read the lines in memory
        //

        cacheInMemory();

        staxFactory = XMLInputFactory.newFactory();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Updates the value of the element/attribute indicated by the path with the given string.
     * <p>
     * If the path does not exist
     *
     * @return true if an actual update occurred, false if the existing value is identical with the new value.
     */
    public boolean set(String path, String value) {

        //
        // concurrently parse the path and the content
        //

        StringTokenizer st = new StringTokenizer(path, "/");
        XMLEventReader xmlReader = null;

        try {

            xmlReader = staxFactory.createXMLEventReader(new CachedContentReader(lines));

            String currentPathToken;

            while (st.hasMoreTokens()) {

                currentPathToken = st.nextToken();

                //
                // iterate over the XML content until we find a matching element/attribute
                //

                if (xmlReader.hasNext()) {

                    XMLEvent xmlEvent = xmlReader.nextEvent();

                    if (xmlEvent.isStartElement()) {
                        StartElement se = xmlEvent.asStartElement();
                        String elementName = se.getName().getLocalPart();

                        if (currentPathToken.equals(elementName)) {

                            //
                            // we matched the element
                            //
                        }
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

                    throw new RuntimeException("NOT YET IMPLEMENTED");
                }
            }
        }

        throw new RuntimeException("NYE");
    }

    /**
     * @return the string associated with the element specified by path. May return null if no such path exists.
     */
    public String get(String path) {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    /**
     * Writes the in-memory updates (if any) into the file.
     */
    public void save() {
        throw new RuntimeException("NOT YET IMPLEMENTED");
    }

    public boolean isDirty() {
        return false;
    }

    public int getLineCount() {

        return lines.size();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void cacheInMemory() throws IOException {

        BufferedReader br = null;

        try {

            FileReader fr = new FileReader(xmlFile);
            br = new BufferedReader(fr);

            String line;
            int lineNumber = 1;

            while((line = br.readLine()) != null) {

                // TODO we assume Linux and the fact that the last line as a \n. We need to refine this
                lines.add(new Line(lineNumber ++, line, "\n"));
            }
        }
        finally {

            if (br != null) {
                br.close();
            }
        }

    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
