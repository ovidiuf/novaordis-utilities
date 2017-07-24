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

package io.novaordis.utilities.logging;

import io.novaordis.utilities.logging.log4j.Log4j;
import io.novaordis.utilities.logging.log4j.Log4jLevel;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;

/**
 * A pattern that consists in routing all log4j logging information to stderr if -v or --verbose command line options
 * are used. This class contains logic to dynamically enable DEBUG level logging at stderr.
 *
 * See https://kb.novaordis.com/index.php/Project_log4j_Debugging_on_--verbose#Overview
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
public class StderrVerboseLogging {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String VERBOSE_SYSTEM_PROPERTY_NAME = "verbose";

    public static final String DEFAULT_PATTERN = "@%t %d{ABSOLUTE} %-5p [%c{1}] %m%n";

    public static final String STDERR_CONSOLE_APPENDER_NAME = "STDERR";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Enables DEBUG level logging at stderr if the "verbose" system property is set to true.
     */
    public static void init() {

        if (Boolean.getBoolean(VERBOSE_SYSTEM_PROPERTY_NAME)) {
            enable();
        }
    }

    /**
     * Enables DEBUG level logging at stderr, by raising the root's level to DEBUG and adding a STDERR console appender
     * if necessary. Upon successful completion of the operation, isEnabled() will return true.
     */
    public static void enable() {

        Logger rootLogger = Log4j.getRootLogger();

        //
        // control logging level and add the appender, if necessary
        //

        Log4j.setLevel(rootLogger, Log4jLevel.DEBUG);
        setVerboseLoggingEnabled(true);

        ConsoleAppender ca = Log4j.getConsoleAppender(STDERR_CONSOLE_APPENDER_NAME, "System.err");

        if (ca == null) {

            //
            // no such appender, create and add one
            //

            ca = Log4j.addConsoleAppender(STDERR_CONSOLE_APPENDER_NAME, "System.err", DEFAULT_PATTERN);
            setVerboseLoggingEnabled(true);
        }

        if (Log4j.setLevel(ca, Log4jLevel.DEBUG)) {

            setVerboseLoggingEnabled(true);
        }
    }

    public static boolean isEnabled() {

        return verboseLoggingEnabled;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private static boolean verboseLoggingEnabled;

    // Constructors ----------------------------------------------------------------------------------------------------

    private StderrVerboseLogging() {

        setVerboseLoggingEnabled(false);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Used internally by the class and externally within the package for testing only.
     */
    static void setVerboseLoggingEnabled(boolean b) {

        verboseLoggingEnabled = b;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
