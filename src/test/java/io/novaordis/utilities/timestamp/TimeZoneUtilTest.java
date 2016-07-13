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

package io.novaordis.utilities.timestamp;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/13/16
 */
public class TimeZoneUtilTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimeZoneUtilTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // fromRFC822String() ----------------------------------------------------------------------------------------------

    @Test
    public void fromRFC822String_PositiveOffset() throws Exception {

        String s = "+0800";

        TimeZone timeZone = TimeZoneUtil.fromRFC822String(s);

        assertNotNull(timeZone);
        assertEquals("GMT+08:00", timeZone.getID());
    }

    @Test
    public void fromRFC822String_GMT() throws Exception {

        String s = "-0000";

        TimeZone timeZone = TimeZoneUtil.fromRFC822String(s);

        assertNotNull(timeZone);
        assertEquals("GMT-00:00", timeZone.getID());
    }

    @Test
    public void fromRFC822String_GMT2() throws Exception {

        String s = "+0000";

        TimeZone timeZone = TimeZoneUtil.fromRFC822String(s);

        assertNotNull(timeZone);
        assertEquals("GMT+00:00", timeZone.getID());
    }

    @Test
    public void fromRFC822String_NegativeOffset() throws Exception {

        String s = "-0800";

        TimeZone timeZone = TimeZoneUtil.fromRFC822String(s);

        assertNotNull(timeZone);
        assertEquals("GMT-08:00", timeZone.getID());
    }

    // fromRFC822String() exceptional conditions -----------------------------------------------------------------------

    @Test
    public void fromRFC822String_NoOffset() throws Exception {

        TimeZone timeZone = TimeZoneUtil.fromRFC822String("something lacking known separators");
        assertNull(timeZone);
    }

    @Test
    public void fromRFC822String_IncompleteTimeZoneOffset() throws Exception {

        try {

            TimeZoneUtil.fromRFC822String("+");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 timezone offset fragment \"+\"", msg);
        }
    }


    @Test
    public void fromRFC822String_IncompleteTimeZoneOffset2() throws Exception {

        try {

            TimeZoneUtil.fromRFC822String("+0");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 timezone offset fragment \"+0\"", msg);
        }
    }

    @Test
    public void fromRFC822String_IncompleteTimeZoneOffset3() throws Exception {

        try {

            TimeZoneUtil.fromRFC822String("+010");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 timezone offset fragment \"+010\"", msg);
        }
    }

    @Test
    public void fromRFC822String_InvalidTimeZoneOffset() throws Exception {

        try {

            TimeZoneUtil.fromRFC822String("+a");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid RFC822 timezone \"+a\"", msg);
        }
    }

    // toRFC822String() ------------------------------------------------------------------------------------------------

    @Test
    public void toRFC822String_GMT() {

        String result = TimeZoneUtil.toRFC822String(TimeZone.getTimeZone("GMT"));
        assertEquals("+0000", result);
    }

    @Test
    public void toRFC822String() {

        TimeZone tz = TimeZoneUtil.shift(TimeZone.getTimeZone("GMT"), 1);
        String result = TimeZoneUtil.toRFC822String(tz);
        assertEquals("+0100", result);
    }

    @Test
    public void toRFC822String2() {

        TimeZone tz = TimeZoneUtil.shift(TimeZone.getTimeZone("GMT"), -1);
        String result = TimeZoneUtil.toRFC822String(tz);
        assertEquals("-0100", result);
    }

    // shift() ----------------------------------------------------------------------------------------------------------

    @Test
    public void shift_ZeroOffset() throws Exception {

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0200");
        TimeZone timeZone2 = TimeZoneUtil.shift(timeZone, 0);

        assertEquals(timeZone, timeZone2);

        int difference = timeZone2.getOffset(0) - timeZone.getOffset(0);
        assertEquals(0, difference);
    }

    @Test
    public void shift_PositiveReference_PositiveOffset() throws Exception {

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0200");
        TimeZone result = TimeZoneUtil.shift(timeZone, 1);

        assertFalse(timeZone.equals(result));

        String s = TimeZoneUtil.toRFC822String(result);
        assertEquals("+0300", s);
    }

    @Test
    public void shift_PositiveReference_NegativeOffset() throws Exception {

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0200");
        TimeZone result = TimeZoneUtil.shift(timeZone, -1);

        assertFalse(timeZone.equals(result));

        String s = TimeZoneUtil.toRFC822String(result);
        assertEquals("+0100", s);
    }

    @Test
    public void shift_NegativeReference_PositiveOffset() throws Exception {

        TimeZone timeZone = TimeZone.getTimeZone("GMT-0200");
        TimeZone result = TimeZoneUtil.shift(timeZone, 1);

        assertFalse(timeZone.equals(result));

        String s = TimeZoneUtil.toRFC822String(result);
        assertEquals("-0100", s);
    }

    @Test
    public void shift_NegativeReference_NegativeOffset() throws Exception {

        TimeZone timeZone = TimeZone.getTimeZone("GMT-0200");
        TimeZone result = TimeZoneUtil.shift(timeZone, -1);

        assertFalse(timeZone.equals(result));

        String s = TimeZoneUtil.toRFC822String(result);
        assertEquals("-0300", s);
    }

    @Test
    public void shift_InvalidOffset() throws Exception {

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0200");

        try {

            TimeZoneUtil.shift(timeZone, 1000);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("no timezone"));
        }
    }

    @Test
    public void shift_DefaultTimezone() throws Exception {

        TimeZone timeZone = TimeZone.getDefault();

        TimeZone t = TimeZoneUtil.shift(timeZone, 1);
        TimeZone t2 = TimeZoneUtil.shift(timeZone, -1);

    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
