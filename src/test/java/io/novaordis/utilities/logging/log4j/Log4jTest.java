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

package io.novaordis.utilities.logging.log4j;

import io.novaordis.utilities.Files;
import io.novaordis.utilities.logging.LoggerConfiguration;
import io.novaordis.utilities.logging.YamlLoggerConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.net.JMSAppender;
import org.apache.log4j.spi.RootLogger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class Log4jTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Log4jTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // getRootLogger() -------------------------------------------------------------------------------------------------

    @Test
    public void getRootLogger() throws Exception {

        Logger logger = Log4j.getRootLogger();

        assertNotNull(logger);

        //noinspection ConstantConditions
        assertTrue(logger instanceof RootLogger);
    }

    // getConsoleAppender() --------------------------------------------------------------------------------------------

    @Test
    public void getConsoleAppender_CONSOLE_Stdout() throws Exception {

        ConsoleAppender a = Log4j.getConsoleAppender("CONSOLE", "System.out");
        assertNotNull(a);

        assertEquals("CONSOLE", a.getName());
        assertEquals("System.out", a.getTarget());
    }

    @Test
    public void getConsoleAppender_CONSOLE_Stderr() throws Exception {

        ConsoleAppender a = Log4j.getConsoleAppender("CONSOLE", "System.err");
        assertNull(a);
    }

    // getFileAppender() -----------------------------------------------------------------------------------------------

    @Test
    public void getFileAppenders() throws Exception {

        List<FileAppender> fas = Log4j.getFileAppenders();

        assertEquals(1, fas.size());

        FileAppender fa = fas.get(0);

        String name = fa.getName();
        assertEquals("FILE", name);

        String file = fa.getFile();
        assertEquals("target/test.log", file);
    }

    // getFileAppender() -----------------------------------------------------------------------------------------------

    @Test
    public void getFileAppender_NoSuchAppender() throws Exception {

        FileAppender fa = Log4j.getFileAppender("no-such-file-appender");
        assertNull(fa);
    }

    @Test
    public void getFileAppender() throws Exception {

        FileAppender fa = Log4j.getFileAppender("FILE");

        assertNotNull(fa);

        String name = fa.getName();
        assertEquals("FILE", name);

        String file = fa.getFile();
        assertEquals("target/test.log", file);
    }

    // getAppender() ---------------------------------------------------------------------------------------------------

    @Test
    public void getAppender_NoSuchAppender_NameMismatch() throws Exception {

        Appender a = Log4j.getAppender(ConsoleAppender.class, "no-such-appender");
        assertNull(a);
    }

    @Test
    public void getAppender_NoSuchAppender_TypeMismatch() throws Exception {

        Appender a = Log4j.getAppender(JMSAppender.class, "CONSOLE");
        assertNull(a);
    }

    @Test
    public void getAppender() throws Exception {

        Appender a = Log4j.getAppender(ConsoleAppender.class, "CONSOLE");
        assertNotNull(a);

        ConsoleAppender ca = (ConsoleAppender)a;
        assertEquals("CONSOLE", ca.getName());
    }

    // getHighestVerbosityLoggingLevel() -------------------------------------------------------------------------------

    @Test
    public void getHighestVerbosityLoggingLevel_EmptyList() throws Exception {

        assertNull(Log4j.getHighestVerbosityLoggingLevel(Collections.emptyList()));
    }

    @Test
    public void getHighestVerbosityLoggingLevel() throws Exception {

        List<LoggerConfiguration> configs = Arrays.asList(
                new YamlLoggerConfiguration("a", Log4jLevel.OFF),
                new YamlLoggerConfiguration("b", Log4jLevel.INFO),
                new YamlLoggerConfiguration("c", Log4jLevel.TRACE),
                new YamlLoggerConfiguration("c", Log4jLevel.FATAL));

        Log4jLevel h = Log4j.getHighestVerbosityLoggingLevel(configs);
        assertEquals(Log4jLevel.TRACE, h);
    }

    // addFileAppender() -----------------------------------------------------------------------------------------------

    @Test
    public void addFileAppender() throws Exception {

        List<FileAppender> fas = Log4j.getFileAppenders();

        assertEquals(1, fas.size());
        assertEquals("FILE", fas.get(0).getName());

        File f = new File(System.getProperty("basedir"),
                "target/test-file-appender-" + UUID.randomUUID().toString() + ".log");

        assertFalse(f.isFile());

        FileAppender fa = Log4j.addFileAppender("TEST-FILE-APPENDER", f, "@%t %d{ABSOLUTE} %-5p [%c{1}] %m%n");

        assertTrue(fa instanceof RollingFileAppender);
        assertEquals("TEST-FILE-APPENDER", fa.getName());
        assertEquals(f.getPath(), fa.getFile());

        List<FileAppender> fas2 = Log4j.getFileAppenders();

        assertEquals(2, fas2.size());
        assertEquals("FILE", fas2.get(0).getName());
        assertEquals("TEST-FILE-APPENDER", fas2.get(1).getName());

        //
        // log something so the file will get written into
        //

        log.info("this should go into the new file");

        String content = Files.read(f);

        assertTrue(content.contains("this should go into the new file"));

        //
        // remove it
        //

        assertTrue(Log4j.removeFileAppender("TEST-FILE-APPENDER"));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
