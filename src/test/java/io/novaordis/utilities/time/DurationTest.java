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

package io.novaordis.utilities.time;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/22/16
 */
public class DurationTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimestampImplTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_Milliseconds() throws Exception {

        Duration d = new Duration(1L);
        assertEquals(1L, d.getMilliseconds());
    }

    @Test
    public void constructor_String() throws Exception {

        Duration d = new Duration("10m");

        assertEquals(10L * 60 * 1000, d.getMilliseconds());
    }

    // stringToMs() ----------------------------------------------------------------------------------------------------

    @Test
    public void stringToMs_Seconds() throws Exception {

        long d = Duration.stringToMs("10s");
        assertEquals(10L * 1000, d);
    }

    @Test
    public void stringToMs_Minutes() throws Exception {

        long d = Duration.stringToMs("10m");
        assertEquals(10L * 60 * 1000, d);
    }

    @Test
    public void stringToMs_Hours() throws Exception {

        long d = Duration.stringToMs("10h");
        assertEquals(10L * 60 * 60 * 1000, d);
    }

    @Test
    public void stringToMs_Invalid() throws Exception {

        try {

            Duration.stringToMs("something");
            fail("should throw exception");
        }
        catch(DurationFormatException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid duration string \"something\"", msg);
        }
    }

    @Test
    public void stringToMs_InvalidNumericPortion() throws Exception {

        try {

            Duration.stringToMs("blahm");
            fail("should throw exception");
        }
        catch(DurationFormatException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid number portion of the duration string: \"blah\"", msg);

            Throwable cause = e.getCause();
            assertTrue(cause instanceof NumberFormatException);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
