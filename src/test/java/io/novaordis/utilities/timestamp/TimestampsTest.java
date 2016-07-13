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

    // getDefaultTimezoneMs/Hours --------------------------------------------------------------------------------------

//    @Test
//    public void getDefaultTimezoneMs() throws Exception {
//
//        int t = Timestamps.getDefaultTimezoneMs();
//        assertEquals(t, TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
//    }
//
//    @Test
//    public void getDefaultTimezoneHours() throws Exception {
//
//        int t = Timestamps.getDefaultTimezoneHours();
//        assertEquals(t, (TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings()) /
//                Timestamps.MILLISECONDS_IN_AN_HOUR);
//    }
////
////
////    // getTimeZoneOffsetHours() ----------------------------------------------------------------------------------------
////
////    @Test
////    public void timezoneOffsetHoursFromString() throws Exception {
////
////        assertNull(Timestamps.timezoneOffsetHoursFromString("blah"));
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_BothNegativeAndPositiveTimezones() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +0100 -0200 something");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            log.info(msg);
////            assertTrue(msg.contains("both timezone prefixes ' +' and ' -' detected in timestamp"));
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_Negative() throws Exception {
////
////        Integer result = Timestamps.timezoneOffsetHoursFromString("something -1200 something");
////        assertNotNull(result);
////        assertEquals(-12, result.intValue());
////    }
////    @Test
////    public void timezoneOffsetHoursFromString_SmallNegative() throws Exception {
////
////        Integer result = Timestamps.timezoneOffsetHoursFromString("something -0100 something");
////        assertNotNull(result);
////        assertEquals(-1, result.intValue());
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_Zero() throws Exception {
////
////        Integer result = Timestamps.timezoneOffsetHoursFromString("something -0000 something");
////        assertNotNull(result);
////        assertEquals(0, result.intValue());
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_Zero2() throws Exception {
////
////        Integer result = Timestamps.timezoneOffsetHoursFromString("something +0000 something");
////        assertNotNull(result);
////        assertEquals(0, result.intValue());
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_Positive() throws Exception {
////
////        Integer result = Timestamps.timezoneOffsetHoursFromString("something +1400 something");
////        assertNotNull(result);
////        assertEquals(14, result.intValue());
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_LowerThanLowestBound() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something -1300 something");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            log.info(e.getMessage());
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_HigherThanHighestBound() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +1500 something");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            log.info(msg);
////            assertEquals("invalid timezone offset value 15", msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_IncompleteTimezone() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            assertEquals(msg, "incomplete timezone fragment \"+\"");
////            log.info(msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_IncompleteTimezone2() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +0");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            assertEquals(msg, "incomplete timezone fragment \"+0\"");
////            log.info(msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_IncompleteTimezone3() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +00");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            assertEquals(msg, "incomplete timezone fragment \"+00\"");
////            log.info(msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_IncompleteTimezone4() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +000");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            assertEquals(msg, "incomplete timezone fragment \"+000\"");
////            log.info(msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_FractionalTimeZone() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +0110");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            assertEquals(msg, "fractional timezones not supported (yet)");
////            log.info(msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetHoursFromString_FractionalTimeZone2() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursFromString("something +0101");
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////
////            String msg = e.getMessage();
////            assertEquals(msg, "fractional timezones not supported (yet)");
////            log.info(msg);
////        }
////    }
////
////    @Test
////    public void timezoneOffsetMsFromString_Positive() throws Exception {
////
////        Integer result = Timestamps.timezoneOffsetMsFromString("something +1400 something");
////        assertNotNull(result);
////        assertEquals(14 * Timestamps.MILLISECONDS_IN_AN_HOUR, result.intValue());
////    }
////
////    // isValidTimeZoneOffset -------------------------------------------------------------------------------------------
////
////    @Test
////    public void isValidTimeZoneOffsetHours() throws Exception {
////
////        assertFalse(Timestamps.isValidTimeZoneOffsetHours(-13));
////        assertTrue(Timestamps.isValidTimeZoneOffsetHours(-12));
////        assertTrue(Timestamps.isValidTimeZoneOffsetHours(0));
////        assertTrue(Timestamps.isValidTimeZoneOffsetHours(14));
////        assertFalse(Timestamps.isValidTimeZoneOffsetHours(15));
////    }
////
////    @Test
////    public void isValidTimeZoneOffsetMs() throws Exception {
////
////        assertFalse(Timestamps.isValidTimeZoneOffsetMs(-13 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////        assertTrue(Timestamps.isValidTimeZoneOffsetMs(-12 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////        assertTrue(Timestamps.isValidTimeZoneOffsetMs(0));
////        assertTrue(Timestamps.isValidTimeZoneOffsetMs(14 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////        assertFalse(Timestamps.isValidTimeZoneOffsetMs(15 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////    }
////
////    // timezoneOffsetHoursToString() -----------------------------------------------------------------------------------
////
////    @Test
////    public void timezoneOffsetHoursToString() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetHoursToString(-13);
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////            log.info(e.getMessage());
////        }
////
////        assertEquals("-1000", Timestamps.timezoneOffsetHoursToString(-10));
////        assertEquals("-0900", Timestamps.timezoneOffsetHoursToString(-9));
////        assertEquals("+0000", Timestamps.timezoneOffsetHoursToString(0));
////        assertEquals("+0900", Timestamps.timezoneOffsetHoursToString(9));
////        assertEquals("+1400", Timestamps.timezoneOffsetHoursToString(14));
////
////        try {
////            Timestamps.timezoneOffsetHoursToString(15);
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////            log.info(e.getMessage());
////        }
////    }
////
////    // timezoneOffsetMsToString() --------------------------------------------------------------------------------------
////
////    @Test
////    public void timezoneOffsetMsToString() throws Exception {
////
////        try {
////
////            Timestamps.timezoneOffsetMsToString(-13 * Timestamps.MILLISECONDS_IN_AN_HOUR);
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////            log.info(e.getMessage());
////        }
////
////        assertEquals("-1000", Timestamps.timezoneOffsetMsToString(-10 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////        assertEquals("-0900", Timestamps.timezoneOffsetMsToString(-9 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////        assertEquals("+0000", Timestamps.timezoneOffsetMsToString(0));
////        assertEquals("+0900", Timestamps.timezoneOffsetMsToString(9 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////        assertEquals("+1400", Timestamps.timezoneOffsetMsToString(14 * Timestamps.MILLISECONDS_IN_AN_HOUR));
////
////        try {
////            Timestamps.timezoneOffsetMsToString(15 * Timestamps.MILLISECONDS_IN_AN_HOUR);
////            fail("should have thrown exception");
////        }
////        catch(IllegalArgumentException e) {
////            log.info(e.getMessage());
////        }
////    }
//
//    // format() --------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void format_NullTargetFormat() throws Exception {
//
//        Timestamp ts = new TimestampImpl("01/01/16 00:00:00", new SimpleDateFormat("MM/dd/yy HH:mm:ss"));
//
//        try {
//            Timestamps.format(ts, null, "N/A");
//            fail("should have thrown exception");
//        }
//        catch(IllegalArgumentException e) {
//
//            String msg = e.getMessage();
//            log.info(msg);
//            assertTrue(msg.contains("null target format"));
//        }
//    }
//
//    @Test
//    public void format_NullTimestamp() throws Exception {
//
//        String result = Timestamps.format(null, new SimpleDateFormat("yyyy"), "n6w3");
//        assertEquals("n6w3", result);
//    }
//
//    @Test
//    public void format_NoSourceTimezoneOffset() throws Exception {
//
//        DateFormat sourceFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
//        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//
//        Timestamp ts = new TimestampImpl("16/12/31 01:01:01", sourceFormat);
//
//        String result = Timestamps.format(ts, targetFormat, null);
//
//        assertEquals("12/31/16 01:01:01", result);
//    }
//
//    @Test
//    public void format_SourceHasTimezoneOffset_TargetDoesNotHaveTimezoneOffset_DifferentTimezone() throws Exception {
//
//        TimeZone defaultTimeZone = TimeZone.getDefault();
//        int ourTimezoneOffsetHours =
//                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ Timestamps.MILLISECONDS_IN_AN_HOUR;
//        int sourceTimezoneOffset = ourTimezoneOffsetHours - 4;
//        if (sourceTimezoneOffset < Timestamps.LOWEST_VALID_TIMEZONE_OFFSET_HOURS) {
//            sourceTimezoneOffset = Timestamps.HIGHEST_VALID_TIMEZONE_OFFSET_HOURS;
//        }
//
//        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        String sourceTimestamp = "07/11/15 11:00:00 " + Timestamps.timezoneOffsetHoursToString(sourceTimezoneOffset);
//
//        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//
//        Timestamp ts = new TimestampImpl(sourceTimestamp, sourceFormat);
//
//        String result = Timestamps.format(ts, targetFormat, null);
//
//        assertEquals("07/11/15 11:00:00", result);
//    }
//
//    @Test
//    public void format_SourceHasTimezoneOffset_TargetDoesNotHaveTimezoneOffset_SameTimezone() throws Exception {
//
//        TimeZone defaultTimeZone = TimeZone.getDefault();
//        int ourTimezoneOffsetHours =
//                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ Timestamps.MILLISECONDS_IN_AN_HOUR;
//
//        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        String sourceTimestamp = "07/11/15 11:00:00 " + Timestamps.timezoneOffsetHoursToString(ourTimezoneOffsetHours);
//
//        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//
//        Timestamp ts = new TimestampImpl(sourceTimestamp, sourceFormat);
//
//        String result = Timestamps.format(ts, targetFormat, null);
//
//        assertEquals("07/11/15 11:00:00", result);
//    }
//
//    @Test
//    public void format_SourceHasTimezoneOffset_TargetHasTimezoneOffset() throws Exception {
//
//        TimeZone defaultTimeZone = TimeZone.getDefault();
//        int ourTimezoneOffsetHours =
//                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ Timestamps.MILLISECONDS_IN_AN_HOUR;
//
//        int sourceTimezoneOffset = ourTimezoneOffsetHours - 4;
//        if (sourceTimezoneOffset < Timestamps.LOWEST_VALID_TIMEZONE_OFFSET_HOURS) {
//            sourceTimezoneOffset = Timestamps.HIGHEST_VALID_TIMEZONE_OFFSET_HOURS;
//        }
//
//        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        DateFormat targetFormat = new SimpleDateFormat("yy/dd/MM HH:mm:ss Z");
//        DateFormat referenceFormatNoTimezone = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//
//        String timestampString = "07/10/15 10:00:00 " + Timestamps.timezoneOffsetHoursToString(sourceTimezoneOffset);
//
//        Timestamp ts = new TimestampImpl(timestampString, sourceFormat);
//
//        String result = Timestamps.format(ts, targetFormat, null);
//
//        String reference = Timestamps.format(ts, referenceFormatNoTimezone, null);
//
//        String resultHourFragment = extractHourFragment(result);
//        String referenceHourFragment = extractHourFragment(reference);
//
//        assertFalse(resultHourFragment.equals(referenceHourFragment));
//    }
//
//    // doesIncludeTimezoneSpecification() ------------------------------------------------------------------------------
//
//    @Test
//    public void doesIncludeTimezoneSpecification_ItDoes() throws Exception {
//
//        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        assertTrue(Timestamps.doesIncludeTimezoneSpecification(f));
//    }
//
//    @Test
//    public void doesIncludeTimezoneSpecification_ItDoesNot() throws Exception {
//
//        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//        assertFalse(Timestamps.doesIncludeTimezoneSpecification(f));
//    }
//
//    // adjustForTimezone() ---------------------------------------------------------------------------------------------
//
//    @Test
//    public void adjustForTimezone_SameTimezone() throws Exception {
//
//        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        Timestamp t = new TimestampImpl("01/01/1970 00:00:01 +0000",df);
//
//        assertEquals(1000L, Timestamps.adjustForTimezone(t, 0));
//
//    }
//
//    @Test
//    public void adjustForTimezone_SameTimezone2() throws Exception {
//
//        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        Timestamp t = new TimestampImpl("01/01/1970 01:00:01 +0100",df);
//
//        //noinspection PointlessArithmeticExpression
//        assertEquals(1000L, Timestamps.adjustForTimezone(t, 1 * Timestamps.MILLISECONDS_IN_AN_HOUR));
//
//    }
//
//    @Test
//    public void adjustForTimezone_IncomingTimestampHasNoTimezoneOffset() throws Exception {
//
//        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
//        Timestamp t = new TimestampImpl("01/05/1970 01:00:01",df);
//
//        long gmt = t.getTimestampGMT();
//        assertNull(t.getTimezoneOffsetMs());
//
//        assertEquals(gmt, Timestamps.adjustForTimezone(t, -1 * Timestamps.MILLISECONDS_IN_AN_HOUR));
//        //noinspection PointlessArithmeticExpression
//        assertEquals(gmt, Timestamps.adjustForTimezone(t, 0  * Timestamps.MILLISECONDS_IN_AN_HOUR));
//        //noinspection PointlessArithmeticExpression
//        assertEquals(gmt, Timestamps.adjustForTimezone(t, 1 * Timestamps.MILLISECONDS_IN_AN_HOUR));
//    }
//
//    @Test
//    public void adjustForTimezone_DifferentTimezones() throws Exception {
//
//        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
//        Timestamp t = new TimestampImpl("01/01/1970 00:00:00 +0000",df);
//
//        //noinspection PointlessArithmeticExpression
//        long result = Timestamps.adjustForTimezone(t, 1 * Timestamps.MILLISECONDS_IN_AN_HOUR);
//
//        assertEquals(-1 * Timestamps.MILLISECONDS_IN_AN_HOUR, result);
//    }
//
//    @Test
//    public void adjustForTimezone_DifferentTimezones2() throws Exception {
//
//        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:dd Z");
//
//        Timestamp ts = new TimestampImpl("01/01/16 10:00:00 -0800", df);
//
//        long t = df.parse("01/01/16 10:00:00 -0600").getTime();
//        long result = Timestamps.adjustForTimezone(ts, -6 * Timestamps.MILLISECONDS_IN_AN_HOUR);
//
//        assertEquals(result, t);
//    }
//
//    // Private ---------------------------------------------------------------------------------------------------------
//
//    private String extractHourFragment(String s) {
//
//        int i = s.indexOf(' ');
//        int j = s.indexOf(':');
//        return s.substring(i + 1, j);
//    }

}
