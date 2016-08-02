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

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/16
 */
public class MacOSConfigurationTest extends OSConfigurationTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(MacOSConfigurationTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_executeThrowsException() throws Exception {

        MockMacOS mmos = new MockMacOS();

        mmos.addCommandThatThrowsException("pagesize");

        try {
            new MacOSConfiguration(mmos);
            fail("should throw exception");
        }
        catch(OSConfigurationException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("failed to run pagesize", msg);

            NativeExecutionException nee = (NativeExecutionException)e.getCause();
            assertEquals("SYNTHETIC", nee.getMessage());
        }
    }

    @Test
    public void constructor_executeReturnsNonZero() throws Exception {

        MockMacOS mmos = new MockMacOS();

        mmos.addCommandThatFails("pagesize");

        try {
            new MacOSConfiguration(mmos);
            fail("should throw exception");
        }
        catch(OSConfigurationException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("failed to get the system memory page size: synthetic failure", msg);
        }
    }

    @Test
    public void constructor_executeReturnsANonInteger() throws Exception {

        MockMacOS mmos = new MockMacOS();

        mmos.addCommandThatSucceeds("pagesize", "blah", null);

        try {
            new MacOSConfiguration(mmos);
            fail("should throw exception");
        }
        catch(OSConfigurationException e) {

            String msg = e.getMessage();
            log.info(msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected MacOSConfiguration getOSConfigurationToTest() throws Exception {

        MockMacOS os = new MockMacOS();
        os.addCommandThatSucceeds("pagesize", "4096", null);
        return new MacOSConfiguration(os);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
