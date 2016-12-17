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
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

    private File scratchDirectory;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Before
    public void before() throws Exception {

        String projectBaseDirName = System.getProperty("basedir");
        scratchDirectory = new File(projectBaseDirName, "target/test-scratch");
        assertTrue(scratchDirectory.isDirectory());
    }

    @After
    public void tearDown() {

        //
        // scratch directory cleanup
        //

        assertTrue(io.novaordis.utilities.Files.rmdir(scratchDirectory, false));

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
    public void execute_Real_OneWord() throws Exception {

        OS os = getOSToTest();
        NativeExecutionResult ner = os.execute("ls");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_Real_TwoWord() throws Exception {

        OS os = getOSToTest();
        NativeExecutionResult ner = os.execute("ls .");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_Real_NullCurrentDirectory() throws Exception {

        OS os = getOSToTest();
        NativeExecutionResult ner = os.execute(null, "ls");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_Real_DoubleQuotedStringFragment() throws Exception {

        OS os = getOSToTest();

        NativeExecutionResult er = os.execute("echo \"two words\"");

        assertTrue(er.isSuccess());

        String stdout = er.getStdout();
        assertEquals("two words\n", stdout);
    }

    @Test
    public void execute_Real_DifferentDirectory() throws Exception {

        OS os = getOSToTest();

        File d = new File(scratchDirectory, "test-dir");
        assertTrue(d.mkdir());

        NativeExecutionResult er = os.execute(d, "pwd");

        assertTrue(er.isSuccess());

        String stdout = er.getStdout();

        stdout = stdout.trim();

        File d2 = new File(stdout);

        assertEquals(d2, d);
    }

    @Test
    public void execute_Real_NonExistentCommand() throws Exception {

        OS os = getOSToTest();

        String command = "i-am-sure-there-is-no-such-command";

        NativeExecutionResult r = os.execute(command);

        String stdout = r.getStdout();
        String stderr = r.getStderr();

        assertEquals(127, r.getExitStatus());
        assertNull(stdout);
        assertNotNull(stderr);
    }

    @Test
    public void execute_Real_TestScript_SimpleEcho() throws Exception {

        OS os = getOSToTest();

        File dir = new File(System.getProperty("basedir"));
        dir = new File(dir, "src/test/resources/data/executable-scripts");
        assertTrue(dir.isDirectory());
        String script = "simple-echo.sh";
        File scriptFile = new File(dir, script);
        assertTrue(scriptFile.isFile());

        String command = script + " test";

        NativeExecutionResult r = os.execute(dir, command);

        String stdout = r.getStdout();
        String stderr = r.getStderr();

        assertTrue(r.isSuccess());
        assertEquals("test\n", stdout);
        assertNull(stderr);
    }


    /**
     * This is commented out as it blocks, we don't have a good way to handle processes that read, yet.
     */
//    @Test
//    public void execute_Real_TheUnderlyingProcessBlocksWaitingForInput() throws Exception {
//
//        OS os = getOSToTest();
//
//        File dir = new File(System.getProperty("basedir"));
//        dir = new File(dir, "src/test/resources/data/executable-scripts");
//        assertTrue(dir.isDirectory());
//        String script = "read-from-stdin.sh";
//        File scriptFile = new File(dir, script);
//        assertTrue(scriptFile.isFile());
//
//        String command = script;
//
//        NativeExecutionResult r = os.execute(dir, command);
//
//        String stdout = r.getStdout();
//        String stderr = r.getStderr();
//
//        assertTrue(r.isSuccess());
//        assertNull(stderr);
//        assertNotNull(stdout);
//    }

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

    protected abstract OS getOSToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
