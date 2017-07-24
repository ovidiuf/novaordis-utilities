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
 * The application rely on a base log4j configuration file, usually shipped as part of the application's installation
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

    public static void apply(LoggingConfiguration c) throws IOException, LoggingConfigurationException {

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
                logger.addAppender(fileAppender);
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
