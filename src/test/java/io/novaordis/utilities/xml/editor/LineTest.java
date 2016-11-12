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

        assertEquals(5, line.length());

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

        assertEquals(6, line.length());

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

        assertEquals(4, line.length());

        assertEquals(1, line.getLineNumber());
        assertEquals("blah", line.getValue());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
