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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class MutableXMLDocumentTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MutableXMLDocumentTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void endToEnd() throws Exception {

        File sampleFile = new File(System.getProperty("basedir"), "src/test/resources/data/xml/pom-sample.xml");

        assertTrue(sampleFile.isFile());

        MutableXMLDocument d = new MutableXMLDocument(sampleFile);



    }

    // preconditions fail ----------------------------------------------------------------------------------------------

    @Test
    public void fileDoesNotExist() throws Exception {

        File doesNotExist = new File("/I/am/sure/this/file/does/not/exist.xml");

        try {
            new MutableXMLDocument(doesNotExist);
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("file /I/am/sure/this/file/does/not/exist.xml does not exist", msg);
        }
    }

    @Test
    public void fileCannotBeWritten() throws Exception {

        File sampleFile =
                new File(System.getProperty("basedir"), "src/test/resources/data/xml/file-that-cannot-be-written.xml");

        assertTrue(sampleFile.isFile());
        assertTrue(sampleFile.canRead());
        assertFalse(sampleFile.canWrite());

        try {
            new MutableXMLDocument(sampleFile);
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            //assertTrue(msg.matches("file .*src/test/resources/data/xml/file-that-cannot-be-read.xml cannot be written"));
            assertTrue(msg.matches("^file .*src/test/resources/data/xml/file-that-cannot-be-written\\.xml cannot be written"));
        }
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
