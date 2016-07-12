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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/10/16
 */
public abstract class TimestampTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimestampTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_TimezoneOffset() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        Timestamp t = getTimestampToTest("01/01/1970 01:00:01 +0100", df);

        assertEquals(1000L, t.getTimestampGMT());

        //noinspection PointlessArithmeticExpression
        assertEquals(1 * Timestamps.MILLISECONDS_IN_AN_HOUR, t.getTimezoneOffsetMs().intValue());
    }

    @Test
    public void constructor_NoTimezoneOffset() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        Timestamp t = getTimestampToTest("01/03/1970 00:00:00", df);

        long gmt = t.getTimestampGMT();
        assertTrue(gmt > 0);
        assertNull(t.getTimezoneOffsetMs());
    }

    @Test
    public void illegalTimezoneOffsetValue() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");

        try {
            getTimestampToTest("12/01/16 10:00:00 +2500", df);
            fail("should have thrown exception");
        }
        catch(ParseException e) {

            log.info(e.getMessage());
        }
    }

    // getDay()/getMonth()/getYear() -----------------------------------------------------------------------------------

    @Test
    public void dayMonthYear_TimeZoneOffset() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        String s = "07/30/16 10:00:00 -0200";

        Timestamp ts = getTimestampToTest(s, df);

        assertEquals(30, ts.getDay());
        assertEquals(7, ts.getMonth());
        assertEquals(16, ts.getYear());
    }

    @Test
    public void dayMonthYear_NoTimeZoneOffset() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        String s = "07/30/16 10:00:00";

        Timestamp ts = getTimestampToTest(s, df);

        assertEquals(30, ts.getDay());
        assertEquals(7, ts.getMonth());
        assertEquals(16, ts.getYear());
    }

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Timestamp getTimestampToTest(String timestampAsString, DateFormat dateFormat) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

}
