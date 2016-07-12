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
    public void accessors() throws Exception {

        Timestamp t = getTimestampToTest(1L, 2);
        assertEquals(1L, t.getTimestampGMT());
        assertEquals(2, t.getTimezoneOffsetMs().intValue());
    }

    @Test
    public void noTimezoneOffset() throws Exception {

        Timestamp t = getTimestampToTest(1L, null);
        assertEquals(1L, t.getTimestampGMT());
        assertNull(t.getTimezoneOffsetMs());
    }

    @Test
    public void illegalTimestampGMTValue() throws Exception {

        try {
            getTimestampToTest(-1L, null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void illegalTimezoneOffsetValue() throws Exception {

        try {
            getTimestampToTest(1L, 100 * Timestamps.MILLISECONDS_IN_AN_HOUR);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("invalid timezone offset value"));
        }

        try {
            getTimestampToTest(1L, -14 * Timestamps.MILLISECONDS_IN_AN_HOUR);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("invalid timezone offset value"));
        }

        try {
            getTimestampToTest(1L, -13 * Timestamps.MILLISECONDS_IN_AN_HOUR);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("invalid timezone offset value"));
        }

        for(int i = -12; i < 15; i ++) {

            //
            // make sure it does not fail
            //
            getTimestampToTest(1L, i * Timestamps.MILLISECONDS_IN_AN_HOUR);
        }

        try {
            getTimestampToTest(1L, 15 * Timestamps.MILLISECONDS_IN_AN_HOUR);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("invalid timezone offset value"));
        }

        try {
            getTimestampToTest(1L, 16 * Timestamps.MILLISECONDS_IN_AN_HOUR);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("invalid timezone offset value"));
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

    protected abstract Timestamp getTimestampToTest(long timestampGMT, Integer timezoneOffsetMs) throws Exception;

    protected abstract Timestamp getTimestampToTest(String timestampAsString, DateFormat dateFormat) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

}
