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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public abstract class OSTest extends NativeExecutorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(OSTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // getInstance() ---------------------------------------------------------------------------------------------------

    @Test
    public void getInstance() throws Exception {

        OS os = OS.getInstance();
        log.info(os);
        assertNotNull(os);
    }

    @Test
    public void getInstance_SuccessiveInvocationsReturnIdenticalInstances() throws Exception {

        OS os = OS.getInstance();

        OS os2 = OS.getInstance();

        assertTrue(os == os2);
    }

    @Test
    public void constructor() throws Exception {

        OS os = getOSToTest();
        assertNotNull(os);
    }

    // split -----------------------------------------------------------------------------------------------------------

    @Test
    public void split_NoSpaces() throws Exception {

        String[] s = OSBase.split("test");
        assertEquals(1, s.length);
        assertEquals("test", s[0]);
    }

    @Test
    public void split_Spaces() throws Exception {

        String[] s = OSBase.split(" test1     test2 \t\t test3 ");
        assertEquals(3, s.length);
        assertEquals("test1", s[0]);
        assertEquals("test2", s[1]);
        assertEquals("test3", s[2]);
    }

    @Test
    public void split_DoubleQuotes() throws Exception {

        String[] s = OSBase.split("a \"b c\" d");
        assertEquals(3, s.length);
        assertEquals("a", s[0]);
        assertEquals("b c", s[1]);
        assertEquals("d", s[2]);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected NativeExecutor getNativeExecutorToTest() throws Exception {

        return getOSToTest();
    }

    protected abstract OS getOSToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
