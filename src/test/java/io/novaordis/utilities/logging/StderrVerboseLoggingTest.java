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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Enumeration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/6/16
 */
public class StderrVerboseLoggingTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Level currentRootLoggerLevel;

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Before
    public void setUp() {

        if (Boolean.getBoolean(StderrVerboseLogging.VERBOSE_SYSTEM_PROPERTY_NAME)) {
            fail("previous tests did not clean up " + StderrVerboseLogging.VERBOSE_SYSTEM_PROPERTY_NAME +
                    " system property");
        }

        Logger rootLogger = LogManager.getRootLogger();
        currentRootLoggerLevel = rootLogger.getLevel();

        //
        // make sure there's no STDERR appender
        //

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {
            Appender a = (Appender)e.nextElement();
            String name = StderrVerboseLogging.STDERR_CONSOLE_APPENDER_NAME;
            if (a.getName().equals(name)) {
                fail("the log4j root logger contains a " + name +
                        " appender, which means one of the previous tests did not clean the environment") ;
            }
        }
    }

    @After
    public void cleanUp() {

        //
        // clean the "verbose" system property, in case the test set it up
        //
        System.clearProperty(StderrVerboseLogging.VERBOSE_SYSTEM_PROPERTY_NAME);

        //
        // restore logging configuration
        //

        Logger rootLogger = LogManager.getRootLogger();

        rootLogger.setLevel(currentRootLoggerLevel);

        //
        // remove the STDERR ConsoleAppender
        //

        Appender appenderToRemove = null;

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {
            Appender a = (Appender)e.nextElement();
            if (a.getName().equals(StderrVerboseLogging.STDERR_CONSOLE_APPENDER_NAME)) {
                appenderToRemove = a;
                break;
            }
        }

        if (appenderToRemove != null) {
            rootLogger.removeAppender(appenderToRemove);
        }

        StderrVerboseLogging.setVerboseLoggingEnabled(false);
    }

    // enable() --------------------------------------------------------------------------------------------------------

    @Test
    public void enable() throws Exception {

        //
        // capture the pre-enable() logging configuration to be able to restore it
        //

        assertFalse(StderrVerboseLogging.isEnabled());

        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        assertEquals(Level.INFO, rootLogger.getLevel());

        StderrVerboseLogging.enable();

        assertTrue(StderrVerboseLogging.isEnabled());

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
        assertEquals(Level.DEBUG, p);
    }

    // init() ----------------------------------------------------------------------------------------------------------

    @Test
    public void init_NoVerboseSystemProperty() throws Exception {

        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        assertEquals(Level.INFO, rootLogger.getLevel());

        assertFalse(Boolean.getBoolean(StderrVerboseLogging.VERBOSE_SYSTEM_PROPERTY_NAME));

        assertFalse(StderrVerboseLogging.isEnabled());

        StderrVerboseLogging.init();

        assertFalse(StderrVerboseLogging.isEnabled());

        assertEquals(Level.INFO, rootLogger.getLevel());

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {
            Appender a = (Appender)e.nextElement();
            String name = StderrVerboseLogging.STDERR_CONSOLE_APPENDER_NAME;
            if (a.getName().equals(name)) {
                fail("a STDERR appender was installed even that was not supposed to happen") ;
            }
        }
    }

    @Test
    public void init_VerboseSystemPropertySetTrue() throws Exception {

        Logger rootLogger = LogManager.getRootLogger();
        rootLogger.setLevel(Level.INFO);
        assertEquals(Level.INFO, rootLogger.getLevel());

        assertFalse(Boolean.getBoolean(StderrVerboseLogging.VERBOSE_SYSTEM_PROPERTY_NAME));

        System.setProperty(StderrVerboseLogging.VERBOSE_SYSTEM_PROPERTY_NAME, "true");

        assertFalse(StderrVerboseLogging.isEnabled());

        StderrVerboseLogging.init();

        assertTrue(StderrVerboseLogging.isEnabled());

        assertEquals(Level.DEBUG, rootLogger.getLevel());

        Appender stderr = null;

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {
            Appender a = (Appender)e.nextElement();
            String name = StderrVerboseLogging.STDERR_CONSOLE_APPENDER_NAME;
            if (a.getName().equals(name)) {
                stderr = a;
                break;
            }
        }

        if (stderr == null) {
            fail("no STDERR Appender installed");
        }

        ConsoleAppender c = (ConsoleAppender)stderr;
        assertEquals(StderrVerboseLogging.STDERR_CONSOLE_APPENDER_NAME, c.getName());
        assertEquals("System.err", c.getTarget());
        assertEquals(Level.DEBUG, c.getThreshold());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
