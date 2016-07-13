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
    public void parsingConstructorAndAccessors_TimezoneOffset() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        Timestamp t = getTimestampToTest("01/01/1970 00:00:00 +0000", df);

        assertEquals(0L, t.getTimestampGMT());
        assertEquals(TimeZone.getTimeZone("+0000"), t.getTimeZone());
        assertEquals("1", t.getTimestampElement("d"));
        assertEquals("01", t.getTimestampElement("dd"));
        assertEquals("1", t.getTimestampElement("M"));
        assertEquals("01", t.getTimestampElement("MM"));
        assertEquals("70", t.getTimestampElement("yy"));
        assertEquals("1970", t.getTimestampElement("yyyy"));

        log.debug(".");
    }

    @Test
    public void parsingConstructorAndAccessors_NoTimezoneOffset() throws Exception {

        DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        String ts = "07/12/15 10:00:00";
        Timestamp t = getTimestampToTest(ts, df);

        long gmt = t.getTimestampGMT();
        assertEquals(TimeZone.getDefault(), t.getTimeZone());
        assertEquals("12", t.getTimestampElement("d"));
        assertEquals("12", t.getTimestampElement("dd"));
        assertEquals("7", t.getTimestampElement("M"));
        assertEquals("07", t.getTimestampElement("MM"));
        assertEquals("15", t.getTimestampElement("yy"));
        assertEquals("2015", t.getTimestampElement("yyyy"));

        //
        // make sure the GMT timestamp is correctly calculated
        //

        DateFormat referenceDf = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        String reference = referenceDf.format(new Date());
        reference = reference.substring(reference.lastIndexOf(' ') + 1);
        reference = ts + " " + reference;

        long referenceGmt = referenceDf.parse(reference).getTime();

        assertEquals(gmt, referenceGmt);
    }


    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Timestamp getTimestampToTest(String timestampAsString, DateFormat dateFormat) throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

}
