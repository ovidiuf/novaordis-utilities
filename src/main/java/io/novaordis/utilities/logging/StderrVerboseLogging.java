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

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

import java.util.Enumeration;

/**
 * Logic to dynamically enable DEBUG level logging at stderr.
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

        Logger rootLogger = LogManager.getRootLogger();

        //
        // control logging level and add the appender, if necessary
        //

        Level currentLevel = rootLogger.getLevel();

        if (currentLevel.isGreaterOrEqual(Level.DEBUG)) {

            if (!currentLevel.equals(Level.DEBUG)) {

                rootLogger.setLevel(Level.DEBUG);
                setVerboseLoggingEnabled(true);
            }
        }

        ConsoleAppender stderr = null;

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {

            Appender a = (Appender)e.nextElement();

            if (a instanceof ConsoleAppender) {

                ConsoleAppender ca = (ConsoleAppender)a;

                if (STDERR_CONSOLE_APPENDER_NAME.equals(ca.getName()) && "System.err".equals(ca.getTarget())) {

                    stderr = ca;
                }
            }
        }

        if (stderr == null) {

            //
            // no such appender, create and add
            //

            Layout layout = new PatternLayout(DEFAULT_PATTERN);
            stderr = new ConsoleAppender(layout, "System.err");
            stderr.setName(STDERR_CONSOLE_APPENDER_NAME);
            rootLogger.addAppender(stderr);

            setVerboseLoggingEnabled(true);
        }

        Priority stderrPriority = stderr.getThreshold();

        if (stderrPriority == null || stderrPriority.isGreaterOrEqual(Level.DEBUG)) {

            if (!Level.DEBUG.equals(stderrPriority)) {

                stderr.setThreshold(Level.DEBUG);
                setVerboseLoggingEnabled(true);
            }
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
