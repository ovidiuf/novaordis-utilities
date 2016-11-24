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

package io.novaordis.utilities.os;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public class MockOSTest extends OSTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(MockOSTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    public void execute_DoubleQuotedStringFragment() throws Exception {
        // irrelevant, noop
    }

    // getInstance() ---------------------------------------------------------------------------------------------------

    @Test
    public void getInstance_NoSuchClass() throws Exception {

        try {

            System.setProperty(OS.OS_IMPLEMENTATION_PROPERTY_NAME, "i.am.sure.there.is.no.such.os.Implementation");

            try {

                OS.getInstance();
                fail("should throw exception");
            } catch (ClassNotFoundException e) {

                log.info(e.getMessage());
            }
        }
        finally {

            System.clearProperty(OS.OS_IMPLEMENTATION_PROPERTY_NAME);
        }

    }

    // name ------------------------------------------------------------------------------------------------------------

    @Test
    public void name() throws Exception {

        assertEquals("MockOS", getOSToTest().getName());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MockOS getOSToTest() throws Exception {

        //
        // use the conventional custom OS creation mechanism
        //

        try {

            System.setProperty(OS.OS_IMPLEMENTATION_PROPERTY_NAME, MockOS.class.getName());

            MockOS mockOS = (MockOS)OS.getInstance();

            mockOS.addCommandThatSucceeds("ls", "something", "");
            mockOS.addCommandThatSucceeds("ls .", "something", "");

            return mockOS;

        }
        finally {

            System.clearProperty(OS.OS_IMPLEMENTATION_PROPERTY_NAME);
        }
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
