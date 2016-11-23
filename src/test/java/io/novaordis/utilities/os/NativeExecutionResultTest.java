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

package io.novaordis.utilities.os;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public class NativeExecutionResultTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(NativeExecutionResultTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void invalidExitStatus() throws Exception {

        try {
            new NativeExecutionResult(-1, "something", "something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void invalidExitStatus2() throws Exception {

        try {
            new NativeExecutionResult(256, "something", "something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void constructor() throws Exception {

        NativeExecutionResult r = new NativeExecutionResult(0, "something", "something else");

        assertEquals(0, r.getExitStatus());
        assertTrue(r.isSuccess());
        assertFalse(r.isFailure());
        assertEquals("something", r.getStdout());
        assertEquals("something else", r.getStderr());
    }

    @Test
    public void constructor2() throws Exception {

        NativeExecutionResult r = new NativeExecutionResult(1, "something", "something else");

        assertEquals(1, r.getExitStatus());
        assertFalse(r.isSuccess());
        assertTrue(r.isFailure());
    }

    @Test
    public void constructor_NullStdout() throws Exception {

        NativeExecutionResult r = new NativeExecutionResult(0, null, "something");

        assertNull(r.getStdout());
        assertEquals("something", r.getStderr());
    }

    @Test
    public void constructor_NullStderr() throws Exception {

        NativeExecutionResult r = new NativeExecutionResult(0, "something", null);

        assertEquals("something", r.getStdout());
        assertNull(r.getStderr());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
