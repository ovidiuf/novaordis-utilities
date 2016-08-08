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
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.junit.Test;

import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
public class StderrVerboseLoggingTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void enable() throws Exception {


        //
        // capture the pre-enable() logging configuration to be able to restore it
        //

        Logger rootLogger = LogManager.getRootLogger();
        Level currentLevel = rootLogger.getLevel();

        try {

            StderrVerboseLogging.enable();

            Level newLevel = rootLogger.getLevel();
            assertEquals(Level.DEBUG, newLevel);

            ConsoleAppender stderr = null;

            for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {
                Appender a = (Appender)e.nextElement();
                if (a instanceof ConsoleAppender && ((ConsoleAppender)a).getTarget().equals("System.err")) {
                    stderr = (ConsoleAppender)a;
                    break;
                }
            }

            //
            // we must have a System.err ConsoleAppender
            //
            assertNotNull(stderr);

            Layout layout = stderr.getLayout();
            assertTrue(layout instanceof PatternLayout);
            PatternLayout patternLayout = (PatternLayout)layout;

            String pattern = patternLayout.getConversionPattern();
            assertEquals(StderrVerboseLogging.DEFAULT_PATTERN, pattern);

            //
            // that appender must allow DEBUG
            //

            Priority p = stderr.getThreshold();
            assertTrue(p.isGreaterOrEqual(Level.DEBUG));
        }
        finally {

            //
            // restore logging configuration
            //

            rootLogger.setLevel(currentLevel);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
