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

package io.novaordis.utilities.logging;

import io.novaordis.utilities.Files;
import io.novaordis.utilities.logging.log4j.Log4j;
import io.novaordis.utilities.logging.log4j.Log4jLevel;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class AlternativeLoggingConfigurationTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(AlternativeLoggingConfigurationTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // configureLogging() ----------------------------------------------------------------------------------------------

    @Test
    public void apply_Noop() throws Exception {

        MockLoggingConfiguration mlc = new MockLoggingConfiguration();
        assertNull(mlc.getFile());
        assertTrue(mlc.getLoggerConfiguration().isEmpty());

        AlternativeLoggingConfiguration.apply(mlc, true);
    }

    @Test
    public void apply() throws Exception {

        ConsoleAppender originalConsoleAppender = Log4j.getConsoleAppender("CONSOLE", "System.out");
        assertNotNull(originalConsoleAppender);

        //noinspection ConstantConditions
        String originalFile = Log4j.getFileAppender("FILE").getFile();

        //
        // we come up with random files and categories and we insure the logging infrastructure is configured
        // to use those
        //

        MockLoggingConfiguration mlc = new MockLoggingConfiguration();

        File f = new File(
                System.getProperty("basedir"), "target/test-logging-" + UUID.randomUUID().toString() + ".log");

        assertFalse(f.isFile());
        mlc.setFile(f);

        LoggerConfiguration c = new YamlLoggerConfiguration("test.logger.config.A", Log4jLevel.TRACE);
        LoggerConfiguration c2 = new YamlLoggerConfiguration("test.logger.config.B", Log4jLevel.INFO);

        mlc.setLoggerConfiguration(Arrays.asList(c, c2));

        try {

            AlternativeLoggingConfiguration.apply(mlc, true);

            String randomString = UUID.randomUUID().toString();

            Logger.getLogger("test.logger.config.A").trace(randomString);

            String randomString2 = UUID.randomUUID().toString();

            Logger.getLogger("test.logger.config.A").info(randomString2);

            String randomString3 = UUID.randomUUID().toString();

            Logger.getLogger("test.logger.config.B").trace(randomString3);

            String randomString4 = UUID.randomUUID().toString();

            Logger.getLogger("test.logger.config.B").error(randomString4);

            //
            // we should find randomString, randomString2 and randomString4 in the file
            //

            String content = Files.read(f);

            assertTrue(content.contains(randomString));
            assertTrue(content.contains(randomString2));
            assertTrue(content.contains(randomString4));

            assertFalse(content.contains(randomString3));

            //
            // make sure the Console was removed
            //

            assertNull(Log4j.getConsoleAppender("CONSOLE", "System.out"));
            assertNull(Log4j.getConsoleAppender("CONSOLE", "System.err"));

        }
        finally {

            //
            // restore the CONSOLE appender
            //

            if (Log4j.getConsoleAppender("CONSOLE", "System.out") == null) {

                Log4j.getRootLogger().addAppender(originalConsoleAppender);
            }

            //
            // cleanup, revert logging into the original file, with DEBUG level.
            //

            MockLoggingConfiguration cleanupMlc = new MockLoggingConfiguration();
            cleanupMlc.setFile(new File(originalFile));
            cleanupMlc.setLoggerConfiguration(Collections.singletonList(
                    new YamlLoggerConfiguration("something", Log4jLevel.DEBUG)));
            AlternativeLoggingConfiguration.apply(cleanupMlc, false);
            log.info("restored");
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
