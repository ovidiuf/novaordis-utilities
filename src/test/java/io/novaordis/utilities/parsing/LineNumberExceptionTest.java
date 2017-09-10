/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.utilities.parsing;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LineNumberExceptionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(LineNumberExceptionTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullLineNumber_NullPositionInLine() throws Exception {

        RuntimeException cause = new RuntimeException();
        LineNumberException e = new LineNumberException(null, "test", cause);
        assertEquals("test", e.getMessage());
        assertEquals(cause, e.getCause());
        assertNull(e.getLineNumber());
        assertNull(e.getPositionInLine());

        log.debug(".");
    }

    @Test
    public void constructor() throws Exception {

        RuntimeException cause = new RuntimeException();
        LineNumberException e = new LineNumberException(10L, 15, "test", cause);
        assertEquals("test", e.getMessage());
        assertEquals(cause, e.getCause());
        assertEquals(10L, e.getLineNumber().longValue());
        assertEquals(15, e.getPositionInLine().intValue());
    }

    @Test
    public void constructor2() throws Exception {

        LineNumberException e = new LineNumberException("test");
        assertEquals("test", e.getMessage());
    }

    @Test
    public void constructor3() throws Exception {

        RuntimeException cause = new RuntimeException();
        LineNumberException e = new LineNumberException("test", cause);
        assertEquals("test", e.getMessage());
        assertEquals(cause, e.getCause());
    }

    @Test
    public void constructor4() throws Exception {

        LineNumberException e = new LineNumberException(10L, "test");
        assertEquals("test", e.getMessage());
        assertEquals(10L, e.getLineNumber().longValue());
    }

    @Test
    public void constructor5() throws Exception {

        LineNumberException e = new LineNumberException(10L, 15, "test");
        assertEquals("test", e.getMessage());
        assertEquals(10L, e.getLineNumber().longValue());
        assertEquals(15, e.getPositionInLine().intValue());
    }

    // toLogFormat() ---------------------------------------------------------------------------------------------------


    @Test
    public void toLogFormat() throws Exception {

        LineNumberException e = new LineNumberException(10L, 15, "some message");
        String expected = "line 10, position 15: some message";
        assertEquals(expected, e.toLogFormat());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
