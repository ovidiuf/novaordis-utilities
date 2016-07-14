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

import static io.novaordis.utilities.timestamp.TimeOffset.fromRFC822String;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/13/16
 */
public class TimeOffsetTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TimeOffsetTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // fromRFC822String() ----------------------------------------------------------------------------------------------

    @Test
    public void fromRFC822String_InvalidLowValue() throws Exception {

        String s = "-1201";

        try {
            fromRFC822String(s);
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("time offset not within expected limits -1200...+1400"));
        }
    }

    @Test
    public void fromRFC822String1() throws Exception {

        String s = "blah blah -1200";

        TimeOffset to = fromRFC822String(s);
        assertNotNull(to);
        assertEquals("-1200", to.toRFC822String());
        assertEquals(-12 * 3600 * 1000, to.getOffset());
    }

    @Test
    public void fromRFC822String2() throws Exception {

        String s = "+0800";

        TimeOffset to = fromRFC822String(s);
        assertNotNull(to);
        assertEquals(s, to.toRFC822String());
        assertEquals(8 * 3600 * 1000, to.getOffset());
    }

    @Test
    public void fromRFC822String3() throws Exception {

        String s = "+0800something";

        TimeOffset to = fromRFC822String(s);
        assertNotNull(to);
        assertEquals("+0800", to.toRFC822String());
        assertEquals(8 * 3600 * 1000, to.getOffset());
    }

    @Test
    public void fromRFC822String4() throws Exception {

        String s = "something +0800something";

        TimeOffset to = fromRFC822String(s);
        assertNotNull(to);
        assertEquals("+0800", to.toRFC822String());
        assertEquals(8 * 3600 * 1000, to.getOffset());
    }

    @Test
    public void fromRFC822String_InvalidHighValue() throws Exception {

        String s = "+1401";

        try {
            fromRFC822String(s);
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("time offset not within expected limits -1200...+1400"));
        }
    }

    @Test
    public void fromRFC822String_Production() throws Exception {

        String s = "Jan-01 2016 12:01:01";
        assertNull(fromRFC822String(s));
    }

    // fromRFC822String() exceptional conditions -----------------------------------------------------------------------

    @Test
    public void fromRFC822String_NoOffset() throws Exception {

        TimeOffset to = fromRFC822String("something lacking known separators");
        assertNull(to);
    }

    @Test
    public void fromRFC822String_IncompleteTimeOffset() throws Exception {

        try {

            fromRFC822String("+");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 time offset fragment \"+\"", msg);
        }
    }

    @Test
    public void fromRFC822String_IncompleteTimeOffset2() throws Exception {

        try {

            fromRFC822String("+1");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 time offset fragment \"+1\"", msg);
        }
    }

    @Test
    public void fromRFC822String_IncompleteTimeOffset3() throws Exception {

        try {

            fromRFC822String("-12");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 time offset fragment \"-12\"", msg);
        }
    }

    @Test
    public void fromRFC822String_IncompleteTimeOffset4() throws Exception {

        try {

            fromRFC822String("+123");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("incomplete RFC822 time offset fragment \"+123\"", msg);
        }
    }

    @Test
    public void fromRFC822String_InvalidTimeZoneOffset() throws Exception {

        try {

            fromRFC822String("+a");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e)  {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("invalid RFC822 time offset"));
            assertTrue(msg.contains("not a digit"));
        }
    }


    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NumericArgument_InvalidLowValue() throws Exception {

        try {
            new TimeOffset(-12 * 3600 * 1000 - 1);
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("time offset not within expected limits -1200...+1400"));
        }
    }

    @Test
    public void constructor_NumericArgument_NegativeOffset() throws Exception {

        TimeOffset to = new TimeOffset(-8 * 3600 * 1000);
        assertEquals(-8 * 3600 * 1000, to.getOffset());
        assertEquals("-0800", to.toRFC822String());
    }

    @Test
    public void constructor_NumericArgument_Zero() throws Exception {

        TimeOffset to = new TimeOffset(0);
        assertEquals(0, to.getOffset());
        assertEquals("+0000", to.toRFC822String());
    }

    @Test
    public void constructor_NumericArgument_PositiveOffset() throws Exception {

        TimeOffset to = new TimeOffset(2 * 3600 * 1000);
        assertEquals(2 * 3600 * 1000, to.getOffset());
        assertEquals("+0200", to.toRFC822String());
    }

    @Test
    public void constructor_NumericArgument_InvalidHighValue() throws Exception {

        try {
            new TimeOffset(14 * 3600 * 1000 +1);
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("time offset not within expected limits -1200...+1400"));
        }
    }

    @Test
    public void constructor_StringArgument_InvalidFirstChar() throws Exception {

        try {
            new TimeOffset("something");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.equals("time offset representation must start with + or -"));
        }
    }

    @Test
    public void constructor_StringArgument() throws Exception {

        try {
            new TimeOffset("-1201");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("time offset not within expected limits -1200...+1400"));
        }
    }

    @Test
    public void constructor_StringArgument2() throws Exception {

        String s = "-1200";
        TimeOffset to = new TimeOffset(s);
        assertEquals(-12 * 3600 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument3() throws Exception {

        String s = "-0200";
        TimeOffset to = new TimeOffset(s);
        assertEquals(-2 * 3600 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument4() throws Exception {

        String s = "-0159";
        TimeOffset to = new TimeOffset(s);
        assertEquals(-1 * 3600 * 1000 - 59 * 60 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument5() throws Exception {

        String s = "-0001";
        TimeOffset to = new TimeOffset(s);
        assertEquals(-60 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument6() throws Exception {

        String s = "0000";
        TimeOffset to = new TimeOffset(s);
        assertEquals(0, to.getOffset());
        assertEquals("+" + s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument7() throws Exception {

        String s = "+0000";
        TimeOffset to = new TimeOffset(s);
        assertEquals(0, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument8() throws Exception {

        String s = "+0001";
        TimeOffset to = new TimeOffset(s);
        assertEquals(60 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument9() throws Exception {

        String s = "+0010";
        TimeOffset to = new TimeOffset(s);
        assertEquals(10 * 60 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument10() throws Exception {

        String s = "+0100";
        TimeOffset to = new TimeOffset(s);
        assertEquals(3600 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument11() throws Exception {

        String s = "+1000";
        TimeOffset to = new TimeOffset(s);
        assertEquals(10 * 3600 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument12() throws Exception {

        String s = "+1400";
        TimeOffset to = new TimeOffset(s);
        assertEquals(14 * 3600 * 1000, to.getOffset());
        assertEquals(s, to.toRFC822String());
    }

    @Test
    public void constructor_StringArgument13() throws Exception {

        try {
            new TimeOffset("+1401");
            fail("should have thrown exception");
        }
        catch(InvalidTimeOffsetException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.contains("time offset not within expected limits -1200...+1400"));
        }
    }

    // toRFC822String() ------------------------------------------------------------------------------------------------

    @Test
    public void toRFC822String() throws Exception {

        TimeOffset to = new TimeOffset(-12 * 3600 * 1000);
        String result = to.toRFC822String();
        assertEquals("-1200", result);
    }

    @Test
    public void toRFC822String2() throws Exception {

        TimeOffset to = new TimeOffset(-12 * 3600 * 1000 + 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("-1159", result);
    }

    @Test
    public void toRFC822String3() throws Exception {

        TimeOffset to = new TimeOffset(-10 * 3600 * 1000);
        String result = to.toRFC822String();
        assertEquals("-1000", result);
    }

    @Test
    public void toRFC822String4() throws Exception {

        TimeOffset to = new TimeOffset(-10 * 3600 * 1000 + 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("-0959", result);
    }

    @Test
    public void toRFC822String5() throws Exception {

        TimeOffset to = new TimeOffset(-1 * 3600 * 1000);
        String result = to.toRFC822String();
        assertEquals("-0100", result);
    }

    @Test
    public void toRFC822String6() throws Exception {

        TimeOffset to = new TimeOffset(-1 * 3600 * 1000 + 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("-0059", result);
    }

    @Test
    public void toRFC822String7() throws Exception {

        TimeOffset to = new TimeOffset(-1 * 9 * 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("-0009", result);
    }

    @Test
    public void toRFC822String8() throws Exception {

        TimeOffset to = new TimeOffset(-1 * 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("-0001", result);
    }

    @Test
    public void toRFC822String9() throws Exception {

        TimeOffset to = new TimeOffset(-1 * 60 * 1000 + 1);
        String result = to.toRFC822String();
        assertEquals("-0000", result);
    }

    @Test
    public void toRFC822String10() throws Exception {

        TimeOffset to = new TimeOffset(-1);
        String result = to.toRFC822String();
        assertEquals("-0000", result);
    }

    @Test
    public void toRFC822String11() throws Exception {

        TimeOffset to = new TimeOffset(0);
        String result = to.toRFC822String();
        assertEquals("+0000", result);
    }

    @Test
    public void toRFC822String12() throws Exception {

        TimeOffset to = new TimeOffset(1);
        String result = to.toRFC822String();
        assertEquals("+0000", result);
    }

    @Test
    public void toRFC822String13() throws Exception {

        TimeOffset to = new TimeOffset(60 * 1000);
        String result = to.toRFC822String();
        assertEquals("+0001", result);
    }

    @Test
    public void toRFC822String14() throws Exception {

        TimeOffset to = new TimeOffset(11 * 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("+0011", result);
    }

    @Test
    public void toRFC822String15() throws Exception {

        TimeOffset to = new TimeOffset(3600 * 1000 + 11 * 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("+0111", result);
    }

    @Test
    public void toRFC822String16() throws Exception {

        TimeOffset to = new TimeOffset(11 * 3600 * 1000 + 11 * 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("+1111", result);
    }

    @Test
    public void toRFC822String17() throws Exception {

        TimeOffset to = new TimeOffset(14 * 3600 * 1000 - 60 * 1000);
        String result = to.toRFC822String();
        assertEquals("+1359", result);
    }

    @Test
    public void toRFC822String18() throws Exception {

        TimeOffset to = new TimeOffset(14 * 3600 * 1000);
        String result = to.toRFC822String();
        assertEquals("+1400", result);
    }

    // getDefault() ----------------------------------------------------------------------------------------------------

    @Test
    public void getDefault() throws Exception {

        TimeOffset d = TimeOffset.getDefault();
        assertEquals(TimeZone.getDefault().getOffset(System.currentTimeMillis()), d.getOffset());
    }

    // isValidOffset ---------------------------------------------------------------------------------------------------

    @Test
    public void isValidOffset() throws Exception {

        assertFalse(TimeOffset.isValidOffset(TimeOffset.LOWEST_VALID_TIME_OFFSET - 1));
    }

    @Test
    public void isValidOffset2() throws Exception {

        assertTrue(TimeOffset.isValidOffset(TimeOffset.LOWEST_VALID_TIME_OFFSET));
    }

    @Test
    public void isValidOffset3() throws Exception {

        assertTrue(TimeOffset.isValidOffset(0));
    }

    @Test
    public void isValidOffset4() throws Exception {

        assertTrue(TimeOffset.isValidOffset(TimeOffset.HIGHEST_VALID_TIME_OFFSET));
    }

    @Test
    public void isValidOffset5() throws Exception {

        assertFalse(TimeOffset.isValidOffset(TimeOffset.HIGHEST_VALID_TIME_OFFSET + 1));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
