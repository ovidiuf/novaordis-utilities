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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/11/16
 */
public class TimestampImplTest extends TimestampTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimestampImplTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void parsingConstructor_NullFormat() throws Exception {

        try {
            new TimestampImpl("something", null);
            fail("Should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
        }
    }

    @Test
    public void parsingConstructor_TimezoneOffset() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
        TimestampImpl t = new TimestampImpl("07/01/16 10:00:00 -0800", f);
        assertEquals(-8 * 3600 * 1000, t.getTimeOffset().getOffset());
    }

    @Test
    public void parsingConstructor_NoTimezoneOffset() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
        TimestampImpl t = new TimestampImpl("07/01/16 10:00:00", f);

        long timeAtTheTimeTheTimestampWasRecorded = f.parse("07/01/16 10:00:00").getTime();

        assertEquals(
                TimeZone.getDefault().getOffset(timeAtTheTimeTheTimestampWasRecorded),
                t.getTimeOffset().getOffset());
    }

    // synthetic constructors ------------------------------------------------------------------------------------------

    @Test
    public void syntheticConstructor_NullTimeOffset() throws Exception {

        try {
            new TimestampImpl(0L, null);
        }
        catch(IllegalArgumentException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("null time offset"));
        }
    }

    @Test
    public void syntheticConstructor_ExplicitTimeOffset() throws Exception {

        TimestampImpl ts = new TimestampImpl(0L, new TimeOffset(0));

        assertEquals(0L, ts.getTime());
        assertEquals(new TimeOffset(0), ts.getTimeOffset());

        assertEquals("01", ts.elementToString("dd"));
        assertEquals("01", ts.elementToString("MM"));
        assertEquals("70", ts.elementToString("yy"));
    }

    @Test
    public void syntheticConstructor() throws Exception {

        TimestampImpl ts = new TimestampImpl(0L);

        assertEquals(0L, ts.getTime());

        TimeOffset defaultTimeOffset = new TimeOffset(TimeZone.getDefault().getOffset(System.currentTimeMillis()));
        assertEquals(defaultTimeOffset, ts.getTimeOffset());

        if (defaultTimeOffset.getOffset() > 0) {

            assertEquals("01", ts.elementToString("dd"));
            assertEquals("01", ts.elementToString("MM"));
            assertEquals("70", ts.elementToString("yy"));
        }
        else {
            //
            // we're still in 1969
            //
            assertEquals("31", ts.elementToString("dd"));
            assertEquals("12", ts.elementToString("MM"));
            assertEquals("69", ts.elementToString("yy"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Timestamp getTimestampToTest(String timestampAsString, DateFormat dateFormat) throws Exception {

        return new TimestampImpl(timestampAsString, dateFormat);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
