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

package io.novaordis.utilities.xml;

import java.io.File;
import java.io.IOException;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class InLineXMLEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Fails early. Performs as many verifications it can (file exists, can be read, can be written) at this stage and
     * fails if the preconditions aren't met.
     *
     * @param xmlFile
     *
     * @exception java.io.IOException
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
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
