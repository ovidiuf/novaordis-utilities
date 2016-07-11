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
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/10/16
 */
public class TimestampsTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimestampsTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // getTimeZoneOffsetHours() ----------------------------------------------------------------------------------------

    @Test
    public void getTimeZoneOffsetHours() throws Exception {

        assertNull(Timestamps.getTimeZoneOffsetHours("blah"));
    }

    @Test
    public void getTimeZoneOffsetHours_BothNegativeAndPositiveTimezones() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +0100 -0200 something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("both timezone prefixes ' +' and ' -' detected in timestamp"));
        }
    }

    @Test
    public void getTimeZoneOffsetHours_Negative() throws Exception {

        Integer result = Timestamps.getTimeZoneOffsetHours("something -1200 something");
        assertNotNull(result);
        assertEquals(-12, result.intValue());
    }
    @Test
    public void getTimeZoneOffsetHours_SmallNegative() throws Exception {

        Integer result = Timestamps.getTimeZoneOffsetHours("something -0100 something");
        assertNotNull(result);
        assertEquals(-1, result.intValue());
    }

    @Test
    public void getTimeZoneOffsetHours_Zero() throws Exception {

        Integer result = Timestamps.getTimeZoneOffsetHours("something -0000 something");
        assertNotNull(result);
        assertEquals(0, result.intValue());
    }

    @Test
    public void getTimeZoneOffsetHours_Zero2() throws Exception {

        Integer result = Timestamps.getTimeZoneOffsetHours("something +0000 something");
        assertNotNull(result);
        assertEquals(0, result.intValue());
    }

    @Test
    public void getTimeZoneOffsetHours_Positive() throws Exception {

        Integer result = Timestamps.getTimeZoneOffsetHours("something +1400 something");
        assertNotNull(result);
        assertEquals(14, result.intValue());
    }

    @Test
    public void getTimeZoneOffsetHours_LowerThanLowestBound() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something -1300 something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }
    }

    @Test
    public void getTimeZoneOffsetHours_HigherThanHighestBound() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +1500 something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("invalid timezone offset value 15", msg);
        }
    }

    @Test
    public void getTimeZoneOffsetHours_IncompleteTimezone() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals(msg, "incomplete timezone fragment \"+\"");
            log.info(msg);
        }
    }

    @Test
    public void getTimeZoneOffsetHours_IncompleteTimezone2() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +0");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals(msg, "incomplete timezone fragment \"+0\"");
            log.info(msg);
        }
    }

    @Test
    public void getTimeZoneOffsetHours_IncompleteTimezone3() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +00");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals(msg, "incomplete timezone fragment \"+00\"");
            log.info(msg);
        }
    }

    @Test
    public void getTimeZoneOffsetHours_IncompleteTimezone4() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +000");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals(msg, "incomplete timezone fragment \"+000\"");
            log.info(msg);
        }
    }

    @Test
    public void getTimeZoneOffsetHours_FractionalTimeZone() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +0110");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals(msg, "fractional timezones not supported (yet)");
            log.info(msg);
        }
    }

    @Test
    public void getTimeZoneOffsetHours_FractionalTimeZone2() throws Exception {

        try {

            Timestamps.getTimeZoneOffsetHours("something +0101");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals(msg, "fractional timezones not supported (yet)");
            log.info(msg);
        }
    }


    // isValidTimeZoneOffset() -----------------------------------------------------------------------------------------

    @Test
    public void isValidTimeZoneOffset() throws Exception {

        assertFalse(Timestamps.isValidTimeZoneOffset(-13));
        assertTrue(Timestamps.isValidTimeZoneOffset(-12));
        assertTrue(Timestamps.isValidTimeZoneOffset(0));
        assertTrue(Timestamps.isValidTimeZoneOffset(14));
        assertFalse(Timestamps.isValidTimeZoneOffset(15));
    }

    // toTimezoneOffsetString() ----------------------------------------------------------------------------------------

    @Test
    public void toTimezoneOffsetString() throws Exception {

        try {

            Timestamps.toTimezoneOffsetString(-13);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }

        assertEquals("-1000", Timestamps.toTimezoneOffsetString(-10));
        assertEquals("-0900", Timestamps.toTimezoneOffsetString(-9));
        assertEquals("+0000", Timestamps.toTimezoneOffsetString(0));
        assertEquals("+0900", Timestamps.toTimezoneOffsetString(9));
        assertEquals("+1400", Timestamps.toTimezoneOffsetString(14));

        try {
            Timestamps.toTimezoneOffsetString(15);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    // format() --------------------------------------------------------------------------------------------------------

    @Test
    public void format_InvalidSourceTimeZoneOffsetValue() throws Exception {

        try {

            Timestamps.format(1L, -14, new SimpleDateFormat("yy/MM/dd HH:mm:ss"), "N/A");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }

        try {

            Timestamps.format(1L, -13, new SimpleDateFormat("yy/MM/dd HH:mm:ss"), "N/A");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }

        for(int i = -12; i < 15; i ++) {

            //
            // make sure it does not fail
            //
            Timestamps.format(1L, i, new SimpleDateFormat("HH"), null);
        }

        try {

            Timestamps.format(1L, 15, new SimpleDateFormat("yy/MM/dd HH:mm:ss"), "N/A");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }

        try {

            Timestamps.format(1L, 16, new SimpleDateFormat("yy/MM/dd HH:mm:ss"), "N/A");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }

    }

    @Test
    public void format_NullTargetFormat() throws Exception {

        try {

            Timestamps.format(1L, 0, null, "N/A");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }
    }

    @Test
    public void format_NullTimestamp() throws Exception {

        String result = Timestamps.format(null, null, new SimpleDateFormat("yyyy"), "n6w3");
        assertEquals("n6w3", result);
    }

    @Test
    public void format_NoSourceTimezoneOffset() throws Exception {

        DateFormat sourceFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        Date timestamp = sourceFormat.parse("16/12/31 01:01:01");

        String result = Timestamps.format(timestamp.getTime(), null, targetFormat, null);

        assertEquals("12/31/16 01:01:01", result);
    }

    @Test
    public void format_SourceHasTimezoneOffset_TargetDoesNotHaveTimezoneOffset_DifferentTimezone() throws Exception {

        TimeZone defaultTimeZone = TimeZone.getDefault();
        int ourTimezoneOffsetHours =
                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ Timestamps.MILLISECONDS_IN_AN_HOUR;
        int sourceTimezoneOffset = ourTimezoneOffsetHours - 4;
        if (sourceTimezoneOffset < Timestamps.LOWEST_VALID_TIMEZONE_OFFSET_HOURS) {
            sourceTimezoneOffset = Timestamps.HIGHEST_VALID_TIMEZONE_OFFSET_HOURS;
        }

        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        String sourceTimestamp = "07/11/15 11:00:00 " + Timestamps.toTimezoneOffsetString(sourceTimezoneOffset);

        Date timestamp = sourceFormat.parse(sourceTimestamp);

        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        String result = Timestamps.format(timestamp.getTime(), sourceTimezoneOffset, targetFormat, null);

        assertEquals("07/11/15 11:00:00", result);
    }

    @Test
    public void format_SourceHasTimezoneOffset_TargetDoesNotHaveTimezoneOffset_SameTimezone() throws Exception {

        TimeZone defaultTimeZone = TimeZone.getDefault();
        int ourTimezoneOffsetHours =
                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ Timestamps.MILLISECONDS_IN_AN_HOUR;

        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        String sourceTimestamp = "07/11/15 11:00:00 " + Timestamps.toTimezoneOffsetString(ourTimezoneOffsetHours);

        Date timestamp = sourceFormat.parse(sourceTimestamp);

        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        String result = Timestamps.format(timestamp.getTime(), ourTimezoneOffsetHours, targetFormat, null);

        assertEquals("07/11/15 11:00:00", result);
    }

    @Test
    public void format_SourceHasTimezoneOffset_TargetHasTimezoneOffset() throws Exception {

        TimeZone defaultTimeZone = TimeZone.getDefault();
        int ourTimezoneOffsetHours =
                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ Timestamps.MILLISECONDS_IN_AN_HOUR;

        int sourceTimezoneOffset = ourTimezoneOffsetHours - 4;
        if (sourceTimezoneOffset < Timestamps.LOWEST_VALID_TIMEZONE_OFFSET_HOURS) {
            sourceTimezoneOffset = Timestamps.HIGHEST_VALID_TIMEZONE_OFFSET_HOURS;
        }

        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        DateFormat targetFormat = new SimpleDateFormat("yy/dd/MM HH:mm:ss Z");
        DateFormat referenceFormatNoTimezone = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        String timestampString = "07/10/15 10:00:00 " + Timestamps.toTimezoneOffsetString(sourceTimezoneOffset);

        Date timestamp = sourceFormat.parse(timestampString);

        String result = Timestamps.format(timestamp.getTime(), sourceTimezoneOffset, targetFormat, null);
        String reference = Timestamps.format(timestamp.getTime(), sourceTimezoneOffset, referenceFormatNoTimezone, null);

        String resultHourFragment = extractHourFragment(result);
        String referenceHourFragment = extractHourFragment(reference);

        assertFalse(resultHourFragment.equals(referenceHourFragment));
    }

    // doesIncludeTimezoneSpecification() ------------------------------------------------------------------------------

    @Test
    public void doesIncludeTimezoneSpecification_ItDoes() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        assertTrue(Timestamps.doesIncludeTimezoneSpecification(f));
    }

    @Test
    public void doesIncludeTimezoneSpecification_ItDoesNot() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        assertFalse(Timestamps.doesIncludeTimezoneSpecification(f));
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private String extractHourFragment(String s) {

        int i = s.indexOf(' ');
        int j = s.indexOf(':');
        return s.substring(i + 1, j);
    }

}
