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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/11/16
 */
public class LineBasedContentTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(LineBasedContent.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void read_edgeCase_CarriageReturnAtTheEndOfARead_1() throws Exception {

        LineBasedContent c = new LineBasedContent();

        String s = "a\r";

        c.setBufferSize(2);

        try {
            c.read(new ByteArrayInputStream(s.getBytes()));
            fail("should have thrown exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("unsupported carriage return combination", msg);
        }
    }

    @Test
    public void read_edgeCase_CarriageReturnAtTheEndOfARead_2() throws Exception {

        LineBasedContent c = new LineBasedContent();

        String s = "a\rb";

        c.setBufferSize(2);

        try {
            c.read(new ByteArrayInputStream(s.getBytes()));
            fail("should have thrown exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("unsupported carriage return combination", msg);
        }
    }

    @Test
    public void read_edgeCase_CarriageReturnAtTheEndOfARead_3() throws Exception {

        LineBasedContent c = new LineBasedContent();

        String s = "a\r\n";

        c.setBufferSize(2);

        c.read(new ByteArrayInputStream(s.getBytes()));

        assertEquals(1, c.getLineCount());

        Line line = c.get(0);

        assertEquals(1, line.getLineNumber());
        assertEquals("a", line.getValue());
        assertEquals("\r\n", line.getNewLine());
    }

    @Test
    public void read_edgeCase_NewLine() throws Exception {

        LineBasedContent c = new LineBasedContent();

        String s = "a\n";

        c.setBufferSize(2);

        c.read(new ByteArrayInputStream(s.getBytes()));

        assertEquals(1, c.getLineCount());

        Line line = c.get(0);

        assertEquals(1, line.getLineNumber());
        assertEquals("a", line.getValue());
        assertEquals("\n", line.getNewLine());
    }

    @Test
    public void read_edgeCase_NewLine_2() throws Exception {

        LineBasedContent c = new LineBasedContent();

        String s = "abcdef\n";

        c.setBufferSize(4);

        c.read(new ByteArrayInputStream(s.getBytes()));

        assertEquals(1, c.getLineCount());

        Line line = c.get(0);

        assertEquals(1, line.getLineNumber());
        assertEquals("abcdef", line.getValue());
        assertEquals("\n", line.getNewLine());
    }

    @Test
    public void read_edgeCase_NewLine_3() throws Exception {

        LineBasedContent c = new LineBasedContent();

        String s = "\n";

        c.setBufferSize(4);

        c.read(new ByteArrayInputStream(s.getBytes()));

        assertEquals(1, c.getLineCount());

        Line line = c.get(0);

        assertEquals(1, line.getLineNumber());
        assertEquals("", line.getValue());
        assertEquals("\n", line.getNewLine());
    }

    @Test
    public void read_endToEnd() throws Exception {

        String s =
                "a\n" +
                        "b\r\n" +
                        "c";

        LineBasedContent c = new LineBasedContent(s);

        assertEquals(3, c.getLineCount());
        assertFalse(c.isDirty());

        Line line;

        line = c.get(0);

        assertEquals(2, line.getLength());
        assertEquals(1, line.getLineNumber());
        assertTrue(line.hasNewLine());
        assertEquals("\n", line.getNewLine());
        assertEquals("a", line.getValue());

        line = c.get(1);

        assertEquals(3, line.getLength());
        assertEquals(2, line.getLineNumber());
        assertTrue(line.hasNewLine());
        assertEquals("\r\n", line.getNewLine());
        assertEquals("b", line.getValue());

        line = c.get(2);

        assertEquals(1, line.getLength());
        assertEquals(3, line.getLineNumber());
        assertFalse(line.hasNewLine());
        assertNull(line.getNewLine());
        assertEquals("c", line.getValue());
    }

    @Test
    public void clear() throws Exception {

        LineBasedContent c = new LineBasedContent("a\n");

        assertEquals(1, c.getLineCount());
        assertFalse(c.isDirty());

        c.clear();

        assertEquals(0, c.getLineCount());
        assertFalse(c.isDirty());
    }

    // get() -----------------------------------------------------------------------------------------------------------

    @Test
    public void get_IndexOutOfBounds() throws Exception {

        LineBasedContent c = new LineBasedContent("a");

        try {
            c.get(-1);
            fail("should have thrown exception");
        }
        catch (IndexOutOfBoundsException e) {
            log.info(e.getMessage());
        }

        Line line = c.get(0);
        assertNotNull(line);

        try {
            c.get(1);
            fail("should have thrown exception");
        }
        catch (IndexOutOfBoundsException e) {
            log.info(e.getMessage());
        }
    }

    // replace ---------------------------------------------------------------------------------------------------------

    @Test
    public void replace_NoReplacementMade() throws Exception {

        LineBasedContent c = new LineBasedContent("    <something>blah</something>\n");
        assertFalse(c.replace(0, 15, 19, "blah"));
        assertFalse(c.isDirty());
    }

    @Test
    public void replace_NewContentShorter() throws Exception {

        LineBasedContent c = new LineBasedContent("    <something>blah</something>\n");
        assertTrue(c.replace(0, 15, 19, "Z"));
        assertTrue(c.isDirty());
        Line line = c.get(0);
        assertTrue(line.isDirty());
        assertEquals("    <something>Z</something>", line.getValue());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        c.write(baos);

        assertFalse(c.isDirty());
        assertFalse(c.get(0).isDirty());

        String newValue = new String(baos.toByteArray());
        assertEquals("    <something>Z</something>\n", newValue);
    }

    @Test
    public void replace_NewContentLonger() throws Exception {

        LineBasedContent c = new LineBasedContent("    <something>blah</something>\n");
        assertTrue(c.replace(0, 15, 19, "somethingelse"));
        assertTrue(c.isDirty());
        assertTrue(c.get(0).isDirty());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        c.write(baos);

        assertFalse(c.isDirty());
        assertFalse(c.get(0).isDirty());

        String newValue = new String(baos.toByteArray());
        assertEquals("    <something>somethingelse</something>\n", newValue);
    }

    // write -----------------------------------------------------------------------------------------------------------

    @Test
    public void writeNotDirty() throws Exception {

        LineBasedContent c = new LineBasedContent("a\nb\nc\n");
        assertFalse(c.isDirty());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        c.write(baos);

        byte[] content = baos.toByteArray();
        assertEquals(6, content.length);
        assertEquals('a', content[0]);
        assertEquals('\n', content[1]);
        assertEquals('b', content[2]);
        assertEquals('\n', content[3]);
        assertEquals('c', content[4]);
        assertEquals('\n', content[5]);

        assertFalse(c.isDirty());
    }

    @Test
    public void writeDirty() throws Exception {

        LineBasedContent c = new LineBasedContent("a\nb\nc\n");

        //
        // synthetic dirtiness
        //

        c.get(2).setDirty(true);

        assertTrue(c.isDirty());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        c.write(baos);

        byte[] content = baos.toByteArray();
        assertEquals(6, content.length);
        assertEquals('a', content[0]);
        assertEquals('\n', content[1]);
        assertEquals('b', content[2]);
        assertEquals('\n', content[3]);
        assertEquals('c', content[4]);
        assertEquals('\n', content[5]);

        assertFalse(c.isDirty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
