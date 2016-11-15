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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

    // time offset never null ------------------------------------------------------------------------------------------

    @Test
    public void timeOffsetNeverNull_NoTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00", new SimpleDateFormat("MM/dd/yy HH:mm:ss"));
        TimeOffset to = t.getTimeOffset();
        assertNotNull(to);
    }

    @Test
    public void timeOffsetNeverNull_ZeroTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00 +0000", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));
        TimeOffset to = t.getTimeOffset();
        assertNotNull(to);
    }

    @Test
    public void timeOffsetNeverNull_NonZeroTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00 +1100", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));
        TimeOffset to = t.getTimeOffset();
        assertNotNull(to);
    }

    // accessors -------------------------------------------------------------------------------------------------------

    @Test
    public void accessors_NoTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00", new SimpleDateFormat("MM/dd/yy HH:mm:ss"));

        long time = t.getTime();
        TimeOffset to = t.getTimeOffset();

        assertEquals(time, new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("07/01/16 10:00:00").getTime());

        //
        // the time offset should be the default one at the time of the parsing
        //

        int offset = to.getOffset();
        long timeAtTheTimeTheTimestampWasRecorded =
                new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse("07/01/16 10:00:00").getTime();

        assertEquals(offset, TimeZone.getDefault().getOffset(timeAtTheTimeTheTimestampWasRecorded));

        String s = to.toRFC822String();
        log.info(s);
    }

    @Test
    public void accessors_ZeroTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00 +0000", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        long time = t.getTime();
        TimeOffset to = t.getTimeOffset();

        assertEquals(time, new SimpleDateFormat("MM/dd/yy HH:mm:ss Z").parse("07/01/16 10:00:00 +0000").getTime());

        int offset = to.getOffset();
        assertEquals(0, offset);

        String s = to.toRFC822String();
        assertEquals("+0000", s);
    }

    @Test
    public void accessors_UTC() throws Exception {

        Timestamp t = getTimestampToTest("01/01/70 00:00:00 +0000", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        long time = t.getTime();
        TimeOffset to = t.getTimeOffset();

        assertEquals(0, time);

        int offset = to.getOffset();
        assertEquals(0, offset);

        String s = to.toRFC822String();
        assertEquals("+0000", s);
    }

    @Test
    public void accessors_NonZeroTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00 +1100", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        long time = t.getTime();
        TimeOffset to = t.getTimeOffset();

        assertEquals(time, new SimpleDateFormat("MM/dd/yy HH:mm:ss Z").parse("07/01/16 10:00:00 +1100").getTime());

        int offset = to.getOffset();
        assertEquals(11 * 3600 * 1000, offset);

        String s = to.toRFC822String();
        assertEquals("+1100", s);
    }

    // elementToString() -----------------------------------------------------------------------------------------------

    /**
     * elementToString() only works if we have the possiblity to modify a TimeZone instance without affecting the
     * same type of TimeZone. We test that.
     */
    @Test
    public void testTimeZoneInstancesIsolated() throws Exception {

        TimeZone synthetic = TimeZone.getTimeZone("UTC");

        assertEquals(0L, synthetic.getRawOffset());

        synthetic.setRawOffset(7200);

        assertEquals(7200, synthetic.getRawOffset());

        assertFalse(synthetic.inDaylightTime(new SimpleDateFormat("MM/dd/yy").parse("12/20/16")));
        assertFalse(synthetic.inDaylightTime(new SimpleDateFormat("MM/dd/yy").parse("06/20/16")));

        TimeZone otherUtc = TimeZone.getTimeZone("UTC");

        //
        // make sure that setting offset on the UTC time zone did not affect other instances
        //
        assertEquals(0, otherUtc.getRawOffset());
    }

    @Test
    public void elementToString_NoTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00", new SimpleDateFormat("MM/dd/yy HH:mm:ss"));

        String s = t.elementToString("MM/dd/yy HH:mm:ss");
        assertEquals("07/01/16 10:00:00", s);
    }

    @Test
    public void elementToString_ZeroTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00 +0000", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        String s = t.elementToString("MM/dd/yy HH:mm:ss");
        assertEquals("07/01/16 10:00:00", s);
    }

    @Test
    public void elementToString_NonZeroTimeOffsetInTimestampString() throws Exception {

        Timestamp t = getTimestampToTest("07/01/16 10:00:00 +1100", new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        String s = t.elementToString("MM/dd/yy HH:mm:ss");
        assertEquals("07/01/16 10:00:00", s);
    }

    // format() --------------------------------------------------------------------------------------------------------

    @Test
    public void format_NullTargetFormat() throws Exception {

        Timestamp ts = new TimestampImpl("01/01/16 00:00:00", new SimpleDateFormat("MM/dd/yy HH:mm:ss"));

        try {
            ts.format(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("null target format"));
        }
    }

    @Test
    public void format() throws Exception {

        TimeZone currentTimeZone = TimeZone.getDefault();
        int currentOffset = currentTimeZone.getOffset(System.currentTimeMillis());

        // pick a different offset, to make sure the representations are different

        int testOffsetValue = currentOffset - 2 * 3600 * 1000;

        if (!TimeOffset.isValidOffset(testOffsetValue)) {
            testOffsetValue = currentOffset + 2 * 3600 * 1000;
        }

        //
        // this will fail if the test offset is not valid
        //
        TimeOffset testOffset = new TimeOffset(testOffsetValue);
        String rfc822Offset = testOffset.toRFC822String();

        String original = "07/01/16 12:00:00";

        String s = original + " " + rfc822Offset;

        Timestamp timestamp = new TimestampImpl(s, new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        //
        // simply formatting the UTC time will lead to a different representation than the original
        // because the parsing and the rendering are done with two different time offsets
        //

        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        String incorrect = targetFormat.format(timestamp.getTime());

        assertFalse(original.equals(incorrect));

        //
        // now the correct rendering
        //

        String correct = timestamp.format(targetFormat);

        assertTrue(original.equals(correct));
    }

    @Test
    public void equivalentFormat() throws Exception {

        String original = "07/01/16 12:00:00";
        SimpleDateFormat originalDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        int offsetAtTheTimeTheTimestampWasRecorded =
                TimeZone.getDefault().getOffset(originalDateFormat.parse(original).getTime());

        // pick a different offset, to make sure the representations are different

        int testOffsetValue = offsetAtTheTimeTheTimestampWasRecorded - 2 * 3600 * 1000;

        if (!TimeOffset.isValidOffset(testOffsetValue)) {
            testOffsetValue = offsetAtTheTimeTheTimestampWasRecorded + 2 * 3600 * 1000;
        }

        TimeOffset testOffset = new TimeOffset(testOffsetValue);
        String rfc822Offset = testOffset.toRFC822String();

        String originalWithOffsetLiteral = original + " " + rfc822Offset;

        Timestamp timestamp = new TimestampImpl(originalWithOffsetLiteral, new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));

        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        String representation = timestamp.format(targetFormat);

        String representation2 = targetFormat.format(timestamp.adjustTime(TimeOffset.getDefault()));

        assertTrue(representation.equals(representation2));
        assertTrue(original.equals(representation));
    }

    @Test
    public void format_NoSourceTimezoneOffset() throws Exception {

        DateFormat sourceFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        Timestamp ts = new TimestampImpl("16/12/31 01:01:01", sourceFormat);

        String result = ts.format(targetFormat);

        assertEquals("12/31/16 01:01:01", result);
    }

    @Test
    public void format_SourceHasTimezoneOffset_TargetDoesNotHaveTimezoneOffset_DifferentTimezone() throws Exception {

        TimeZone defaultTimeZone = TimeZone.getDefault();
        int ourTimezoneOffsetHours =
                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings())/ (3600 * 1000);
        int sourceTimezoneOffset = ourTimezoneOffsetHours - 4;
        if (sourceTimezoneOffset < TimeOffset.LOWEST_VALID_TIME_OFFSET / (3600 * 1000)) {
            sourceTimezoneOffset = TimeOffset.HIGHEST_VALID_TIME_OFFSET / (3600 * 1000);
        }

        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");

        TimeOffset to = new TimeOffset(sourceTimezoneOffset * 3600 * 1000);

        String sourceTimestamp = "07/11/15 11:00:00 " +  to.toRFC822String();

        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        Timestamp ts = new TimestampImpl(sourceTimestamp, sourceFormat);

        String result = ts.format(targetFormat);

        assertEquals("07/11/15 11:00:00", result);
    }

    @Test
    public void format_SourceHasTimezoneOffset_TargetDoesNotHaveTimezoneOffset_SameTimezone() throws Exception {

        TimeZone defaultTimeZone = TimeZone.getDefault();
        int ourTimezoneOffsetHours = defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings();
        TimeOffset to = new TimeOffset(ourTimezoneOffsetHours);

        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        String sourceTimestamp = "07/11/15 11:00:00 " + to.toRFC822String();

        DateFormat targetFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        Timestamp ts = new TimestampImpl(sourceTimestamp, sourceFormat);

        String result = ts.format(targetFormat);

        assertEquals("07/11/15 11:00:00", result);
    }

    @Test
    public void format_SourceHasTimezoneOffset_TargetHasTimezoneOffset() throws Exception {

        DateFormat sourceFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        DateFormat targetFormat = new SimpleDateFormat("yy/dd/MM HH:mm:ss Z");
        DateFormat refFormatNoTimeOffset = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        TimeOffset to = getADifferentTimeOffsetThanOurs();
        String timestampString = "07/10/15 10:00:00 " + to.toRFC822String();

        Timestamp ts = new TimestampImpl(timestampString, sourceFormat);

        String result = ts.format(targetFormat);

        assertEquals("15/10/07 10:00:00 " + to.toRFC822String(), result);

        String reference = ts.format(refFormatNoTimeOffset);

        // it should be identical with the source
        assertEquals("07/10/15 10:00:00", reference);
    }

    // adjustTime() ---------------------------------------------------------------------------------------------

    @Test
    public void adjustTime_SameTimezone() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        Timestamp t = new TimestampImpl("01/01/1970 00:00:01 +0000", df);

        assertEquals(1000L, t.adjustTime(new TimeOffset(0)));
    }

    @Test
    public void adjustTime_SameTimezone2() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        Timestamp t = new TimestampImpl("01/01/1970 01:00:01 +0100",df);

        //noinspection PointlessArithmeticExpression
        assertEquals(1000L, t.adjustTime(new TimeOffset(1 * 3600 * 1000)));
    }

    @Test
    public void adjustTime_DifferentTimezones() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        Timestamp t = new TimestampImpl("01/01/1970 00:00:00 +0000", df);

        //noinspection PointlessArithmeticExpression
        long result = t.adjustTime(new TimeOffset(1 * 3600 * 1000));
        assertEquals(-1 * 3600 * 1000, result);
    }

    @Test
    public void adjustTime_DifferentTimezones2() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:dd Z");

        Timestamp ts = new TimestampImpl("01/01/16 10:00:00 -0800", df);

        long t = df.parse("01/01/16 10:00:00 -0600").getTime();
        long result = ts.adjustTime(new TimeOffset (- 6 * 3600 * 1000));

        assertEquals(result, t);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Timestamp getTimestampToTest(String timestampAsString, DateFormat dateFormat) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    private TimeOffset getADifferentTimeOffsetThanOurs() {

        TimeZone defaultTimeZone = TimeZone.getDefault();

        int ourTimezoneOffsetHours = defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings();

        int sourceTimezoneOffset = ourTimezoneOffsetHours - 4 * 3600 * 1000;

        if (sourceTimezoneOffset < TimeOffset.LOWEST_VALID_TIME_OFFSET) {
            sourceTimezoneOffset = TimeOffset.HIGHEST_VALID_TIME_OFFSET - 2 * 3600 * 1000;
        }

        return new TimeOffset(sourceTimezoneOffset);
    }

}
