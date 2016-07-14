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
        assertEquals(offset, TimeZone.getDefault().getOffset(System.currentTimeMillis()));

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


//    // format() --------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void format() throws Exception {
//
//        TimeZone dt = TimeZone.getDefault();
//        String s = TimeZoneUtil.toRFC822String(dt);
//
//        // our reference timezone is one hour apart from the default timezone
//        TimeZone reference = TimeZoneUtil.shift(dt, 1);
//        String offset = TimeZoneUtil.toRFC822String(reference);
//        String timestamp = "07/01/15 10:00:00 " + offset;
//
//        Timestamp ts = getTimestampToTest(timestamp , new SimpleDateFormat("MM/dd/yy HH:mm:ss Z"));
//
//        TimeZone target = TimeZoneUtil.shift(reference, 1);
//        DateFormat targetFormat = new SimpleDateFormat("HH:mm:ss");
//
//        String result = ts.format(targetFormat, target);
//
//        //
//        // we expect an hour difference
//        //
//        assertEquals("08:00:00", result);
//    }
//
    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Timestamp getTimestampToTest(String timestampAsString, DateFormat dateFormat) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

}
