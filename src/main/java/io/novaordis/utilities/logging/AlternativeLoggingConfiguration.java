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

import io.novaordis.utilities.logging.log4j.Log4j;
import io.novaordis.utilities.logging.log4j.Log4jLevel;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A pattern that consists in applying alternative log4j configuration for applications that run in background.
 * The application relies on a base log4j configuration file, usually shipped as part of the application's installation
 * bundle, but it can change its logging behavior based on logging configuration present in the application's
 * configuration file. Configuration that comes from the application's configuration file takes priority over
 * configuration stored in log4j.xml.
 *
 * See https://kb.novaordis.com/index.php/Project_Alternative_log4j_Configuration#API
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class AlternativeLoggingConfiguration {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AlternativeLoggingConfiguration.class);

    public static final String HARDCODED_FILE_LOGGING_PATTERN = "@%t %d{ABSOLUTE} %-5p [%c{1}] %m%n";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Apply alternative log4j configuration. The application relies on a base log4j configuration file, usually shipped
     * as part of the application's installation bundle. This method changes the application's logging behavior,
     * superimposing log4j configuration provided as a LoggingConfiguration instance - usually read from an application
     * specific configuration file. Configuration that comes from the LoggingConfiguration instance takes priority over
     * configuration stored in log4j.xml.
     *
     * @param removeConsole because the application is supposed to run in background, the CONSOLE, if present in the
     *                      log4j runtime state, may be removed, and it will be if this argument is true.
     *
     * @throws IOException
     * @throws LoggingConfigurationException
     */
    public static void apply(LoggingConfiguration c, boolean removeConsole)
            throws IOException, LoggingConfigurationException {

        log.debug("configuring alternative logging");

        File file = c.getFile();
        List<LoggerConfiguration> loggerConfiguration = c.getLoggerConfiguration();

        FileAppender fileAppender = Log4j.getFileAppender("FILE");

        if (file != null) {

            //
            // switch the logging file
            //

            if (fileAppender != null && new File(fileAppender.getFile()).equals(file)) {

                log.debug("file appender already configured with the correct file " + file);
            }
            else {

                Log4j.removeFileAppender("FILE");
                fileAppender = Log4j.addFileAppender("FILE", file, HARDCODED_FILE_LOGGING_PATTERN);
            }

            //
            // if we have a file we want the output to go to, remove the console, because this pattern is supposed
            // to be apply to background applications.
            //

            if (removeConsole) {

                Log4j.removeConsoleAppender("CONSOLE", "System.out");
            }
        }

        if (!loggerConfiguration.isEmpty()) {

            //
            // update loggers
            //

            Log4jLevel highestVerbosityLevel = Log4j.getHighestVerbosityLoggingLevel(loggerConfiguration);
            Log4j.setLevel(Log4j.getRootLogger(), highestVerbosityLevel);

            if (fileAppender != null) {

                Log4j.setLevel(fileAppender, highestVerbosityLevel);
            }

            //
            // plug in the root appender into all new individual loggers
            //

            for(LoggerConfiguration lc: loggerConfiguration) {

                String loggerName = lc.getName();
                Log4jLevel loggerLevel = lc.getLevel();

                Logger logger = Logger.getLogger(loggerName);
                logger.setLevel(loggerLevel.getLog4jNativeLevel());

                // no need to set appender on the logger, it's already set on the root logger, if we do this
                // everything will be duplicated
                //logger.addAppender(fileAppender);
            }
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
