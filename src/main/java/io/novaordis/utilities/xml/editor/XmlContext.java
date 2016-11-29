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

import javax.xml.stream.events.XMLEvent;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/11/16
 */
public class XMLContext {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String xmlContentPath;
    private XMLEvent previous;
    private XMLEvent current;


    // Constructors ----------------------------------------------------------------------------------------------------

    public XMLContext(String xmlContentPath, XMLEvent previous, XMLEvent current) {

        this.xmlContentPath = xmlContentPath;
        this.current = current;
        this.previous = previous;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public XMLEvent getPrevious() {
        return previous;
    }

    public XMLEvent getCurrent() {
        return current;
    }

    public String getXmlContentPath() {

        return xmlContentPath;
    }

    @Override
    public String toString() {

        return xmlContentPath + "(" + previous + "), " + current;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
