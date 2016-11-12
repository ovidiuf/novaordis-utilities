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

package io.novaordis.utilities.xml.editor;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/11/16
 */
public class LineTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(LineTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void newLine() throws Exception {

        Line line = new Line(1, "blah", "\n");

        assertTrue(line.hasNewLine());
        assertEquals("\n", line.getNewLine());

        char[] chars = line.getChars();
        assertEquals(5, chars.length);
        assertEquals('b', chars[0]);
        assertEquals('l', chars[1]);
        assertEquals('a', chars[2]);
        assertEquals('h', chars[3]);
        assertEquals('\n', chars[4]);

        assertEquals(5, line.getLength());

        assertEquals(1, line.getLineNumber());
        assertEquals("blah", line.getValue());
    }

    @Test
    public void carriageReturnLineFeed() throws Exception {

        Line line = new Line(1, "blah", "\r\n");

        assertTrue(line.hasNewLine());
        assertEquals("\r\n", line.getNewLine());

        char[] chars = line.getChars();
        assertEquals(6, chars.length);
        assertEquals('b', chars[0]);
        assertEquals('l', chars[1]);
        assertEquals('a', chars[2]);
        assertEquals('h', chars[3]);
        assertEquals('\r', chars[4]);
        assertEquals('\n', chars[5]);

        assertEquals(6, line.getLength());

        assertEquals(1, line.getLineNumber());
        assertEquals("blah", line.getValue());
    }

    @Test
    public void noNewLine() throws Exception {

        Line line = new Line(1, "blah", null);

        assertFalse(line.hasNewLine());
        assertNull(line.getNewLine());

        char[] chars = line.getChars();
        assertEquals(4, chars.length);
        assertEquals('b', chars[0]);
        assertEquals('l', chars[1]);
        assertEquals('a', chars[2]);
        assertEquals('h', chars[3]);

        assertEquals(4, line.getLength());

        assertEquals(1, line.getLineNumber());
        assertEquals("blah", line.getValue());
    }

    @Test
    public void noNewLine_OneCharacter() throws Exception {

        Line line = new Line(1, "c", null);

        assertFalse(line.hasNewLine());
        assertNull(line.getNewLine());

        char[] chars = line.getChars();
        assertEquals(1, chars.length);
        assertEquals('c', chars[0]);

        assertEquals(1, line.getLength());
        assertEquals(1, line.getLineNumber());
        assertEquals("c", line.getValue());
    }

    @Test
    public void dirty() throws Exception {

        Line line = new Line(1, "blah", "\n");
        assertFalse(line.isDirty());

        line.setDirty(true);
        assertTrue(line.isDirty());

        line.setDirty(false);
        assertFalse(line.isDirty());
    }

    // getChars()/getBytes() -------------------------------------------------------------------------------------------

    @Test
    public void getChars_getBytes() throws Exception {

        Line line = new Line(1, "abc", "\n");

        char[] chars = line.getChars();
        assertEquals(4, chars.length);
        assertEquals('a', chars[0]);
        assertEquals('b', chars[1]);
        assertEquals('c', chars[2]);
        assertEquals('\n', chars[3]);

        byte[] bytes = line.getBytes();
        assertEquals(4, bytes.length);
        assertEquals('a', bytes[0]);
        assertEquals('b', bytes[1]);
        assertEquals('c', bytes[2]);
        assertEquals('\n', bytes[3]);
    }

    // replace ---------------------------------------------------------------------------------------------------------

    @Test
    public void replace_fromOutOfBounds_Low() throws Exception {

        Line line = new Line(1, "something", "\n");

        try {
            line.replace(-1, 3, "a");
        }
        catch(IndexOutOfBoundsException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("'from' index -1", msg);
        }
    }

    @Test
    public void replace_fromOutOfBounds_High() throws Exception {

        Line line = new Line(1, "something", "\n");

        try {
            line.replace(9, 3, "a");
        }
        catch(IndexOutOfBoundsException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("'from' index 9", msg);
        }
    }

    @Test
    public void replace_fromOutOfBounds_High2() throws Exception {

        Line line = new Line(1, "something", null);

        try {
            line.replace(9, 3, "a");
        }
        catch(IndexOutOfBoundsException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("'from' index 9", msg);
        }
    }

    @Test
    public void replace_toOutOfBounds_Low() throws Exception {

        Line line = new Line(1, "something", "\n");

        try {
            line.replace(1, 0, "a");
        }
        catch(IndexOutOfBoundsException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("'to' index 0", msg);
        }
    }

    @Test
    public void replace_toOutOfBounds_High() throws Exception {

        Line line = new Line(1, "some", "\n");

        try {
            line.replace(0, 5, "a");
        }
        catch(IndexOutOfBoundsException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("'to' index 5", msg);
        }
    }

    @Test
    public void replace_FromAndToIdentical() throws Exception {

        Line line = new Line(1, "    <something>blah</something>", "\n");
        assertTrue(line.replace(19, 19, "halb"));
        assertTrue(line.isDirty());
        String newValue = line.getValue();
        assertEquals("    <something>blahhalb</something>", newValue);
    }

    @Test
    public void replace_NoReplacementMade() throws Exception {

        Line line = new Line(1, "    <something>blah</something>", "\n");
        assertFalse(line.replace(15, 19, "blah"));
        assertFalse(line.isDirty());
    }

    @Test
    public void replace_NewContentSameLength() throws Exception {

        Line line = new Line(1, "    <something>blah</something>", "\n");
        assertTrue(line.replace(15, 19, "halb"));
        assertTrue(line.isDirty());

        String newValue = line.getValue();
        assertEquals("    <something>halb</something>", newValue);
    }

    @Test
    public void replace_NewContentShorter() throws Exception {

        Line line = new Line(1, "    <something>blah</something>", "\n");
        assertTrue(line.replace(15, 19, "X"));
        assertTrue(line.isDirty());

        String newValue = line.getValue();
        assertEquals("    <something>X</something>", newValue);
    }

    @Test
    public void replace_NewContentLonger() throws Exception {

        Line line = new Line(1, "    <something>blah</something>", "\n");
        assertTrue(line.replace(15, 19, "somethingelse"));
        assertTrue(line.isDirty());

        String newValue = line.getValue();
        assertEquals("    <something>somethingelse</something>", newValue);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
