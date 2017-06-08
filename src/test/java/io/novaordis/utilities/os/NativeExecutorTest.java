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
 * @since 6/8/17
 */
public abstract class NativeExecutorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(NativeExecutorTest.class);


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

    // Tests -----------------------------------------------------------------------------------------------------------

    // execute ---------------------------------------------------------------------------------------------------------

    @Test
    public void execute_Real_OneWord() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();
        NativeExecutionResult ner = ne.execute("ls");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_Real_TwoWord() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();
        NativeExecutionResult ner = ne.execute("ls .");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_Real_NullCurrentDirectory() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();
        NativeExecutionResult ner = ne.execute(null, "ls");

        assertEquals(0, ner.getExitStatus());
        String lsContent = ner.getStdout();
        log.info(lsContent);
        assertFalse(lsContent.isEmpty());
    }

    @Test
    public void execute_Real_DoubleQuotedStringFragment() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        NativeExecutionResult er = ne.execute("echo \"two words\"");

        assertTrue(er.isSuccess());

        String stdout = er.getStdout();
        assertEquals("two words\n", stdout);
    }

    @Test
    public void execute_Real_DifferentDirectory() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        File d = new File(scratchDirectory, "test-dir");
        assertTrue(d.mkdir());

        NativeExecutionResult er = ne.execute(d, "pwd");

        assertTrue(er.isSuccess());

        String stdout = er.getStdout();

        stdout = stdout.trim();

        File d2 = new File(stdout);

        assertEquals(d2, d);
    }

    @Test
    public void execute_Real_NonExistentCommand() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        String command = "i-am-sure-there-is-no-such-command";

        NativeExecutionResult r = ne.execute(command);

        String stdout = r.getStdout();
        String stderr = r.getStderr();

        assertEquals(127, r.getExitStatus());
        assertNull(stdout);
        assertNotNull(stderr);
    }

    @Test
    public void execute_Real_TestScript_SimpleEcho() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        File dir = new File(System.getProperty("basedir"));
        dir = new File(dir, "src/test/resources/data/executable-scripts");
        assertTrue(dir.isDirectory());
        String script = "simple-echo.sh";
        File scriptFile = new File(dir, script);
        assertTrue(scriptFile.isFile());

        String command = script + " test";

        NativeExecutionResult r = ne.execute(dir, command);

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
//        NativeExecutor ne =getNativeExecutorToTest();
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
//        NativeExecutionResult r = ne.execute(dir, command);
//
//        String stdout = r.getStdout();
//        String stderr = r.getStderr();
//
//        assertTrue(r.isSuccess());
//        assertNull(stderr);
//        assertNotNull(stdout);
//    }

    /**
     * Command that returns successfully but that closes stdout without placing anything on it.
     */
    @Test
    public void execute_true() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        //
        // this is an example of a command that completes successfully but does not write anything to stdout - it
        // closes the stream without putting anything in it
        //

        NativeExecutionResult r = ne.execute("true");

        assertTrue(r.isSuccess());

        String stdout = r.getStdout();

        assertNull(stdout);
    }

    /**
     * Command that returns successfully but that closes stdout without placing anything on it.
     */
    @Test
    public void execute_CommandThatCompletesSuccessfullyAndWritesEmptyString() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        File dir = new File(System.getProperty("basedir"));
        dir = new File(dir, "src/test/resources/data/executable-scripts");
        assertTrue(dir.isDirectory());
        String command = "produces-empty-string";
        File scriptFile = new File(dir, command);
        assertTrue(scriptFile.isFile());

        NativeExecutionResult r = ne.execute(dir, command);

        String stdout = r.getStdout();
        String stderr = r.getStderr();

        assertTrue(r.isSuccess());
        assertNull(stdout);
        assertNull(stderr);
    }

    /**
     * Command that returns successfully and writes a new line at stdout.
     */
    @Test
    public void execute_CommandThatCompletesSuccessfullyAndWritesANewLine() throws Exception {

        NativeExecutor ne =getNativeExecutorToTest();

        File dir = new File(System.getProperty("basedir"));
        dir = new File(dir, "src/test/resources/data/executable-scripts");
        assertTrue(dir.isDirectory());
        String command = "produces-a-new-line";
        File scriptFile = new File(dir, command);
        assertTrue(scriptFile.isFile());

        NativeExecutionResult r = ne.execute(dir, command);

        String stdout = r.getStdout();
        String stderr = r.getStderr();

        assertTrue(r.isSuccess());
        assertEquals("\n", stdout);
        assertNull(stderr);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract NativeExecutor getNativeExecutorToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
