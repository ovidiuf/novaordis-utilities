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
import org.apache.log4j.Category;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Priority;
import org.apache.log4j.Logger;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
public class StderrVerboseLogging {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(StderrVerboseLogging.class);

    // Static ----------------------------------------------------------------------------------------------------------

    public static void enable() {

        //
        // Turn DEBUG on CONSOLE
        //

        Category c = log;
        Category root = c;
        if ((c = c.getParent()) != null) {
            root = c;
        }

        Appender appender = root.getAppender("CONSOLE");

        if (appender != null) {

            ConsoleAppender console = (ConsoleAppender) appender;
            //noinspection deprecation
            console.setThreshold(Priority.DEBUG);
        }


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
