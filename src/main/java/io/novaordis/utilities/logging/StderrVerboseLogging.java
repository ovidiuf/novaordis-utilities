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

import java.util.Enumeration;

/**
 * Logic to dynamically enable DEBUG level logging at stderr.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
public class StderrVerboseLogging {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Enables DEBUG level logging at stderr, by raising the root's level to DEBUG and adding a STDERR console appender
     * if necessary.
     */
    public static void enable() {

        Logger rootLogger = LogManager.getRootLogger();

        //
        // control logging level and add the appender, if necessary
        //

        Level currentLevel = rootLogger.getLevel();

        if (!currentLevel.isGreaterOrEqual(Level.DEBUG)) {
            rootLogger.setLevel(Level.DEBUG);
        }

        ConsoleAppender stderr = null;

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {

            Appender a = (Appender)e.nextElement();

            if (a instanceof ConsoleAppender) {
                ConsoleAppender ca = (ConsoleAppender)a;
                if ("System.err".equals(ca.getTarget())) {
                    stderr = ca;
                }
            }
        }

        if (stderr == null) {
            //
            // no such appender, create and add
            //
            Layout layout = new PatternLayout();
            stderr = new ConsoleAppender(layout, "System.err");
            rootLogger.addAppender(stderr);
        }

        stderr.setThreshold(Level.DEBUG);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private StderrVerboseLogging() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
