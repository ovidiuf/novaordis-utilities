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
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public abstract class OSTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(NativeExecutionResultTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @After
    public void tearDown() {

        //
        // clear the cached OS instance, if any
        //

        OS.clearInstance();
    }

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

    // execute ---------------------------------------------------------------------------------------------------------

    @Test
    public void execute_OneWord() throws Exception {

        OS os = getOSToTest();
        NativeExecutionResult ner = os.execute("ls");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_TwoWord() throws Exception {

        OS os = getOSToTest();
        NativeExecutionResult ner = os.execute("ls .");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_NullCurrentDirectory() throws Exception {

        OS os = getOSToTest();
        NativeExecutionResult ner = os.execute(null, "ls");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract OS getOSToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
