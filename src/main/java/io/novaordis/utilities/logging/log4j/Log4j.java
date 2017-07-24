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

import io.novaordis.utilities.logging.LoggerConfiguration;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Priority;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.RootLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Collection of log4j static utilities.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class Log4j {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static RootLogger getRootLogger() {

        return (RootLogger)LogManager.getRootLogger();
    }

    /**
     * @return a appender registered with the root logger, which also has the given type and name. Return null if no
     *  appender matches.
     */
    public static Appender getAppender(Class<? extends Appender> expectedType, String name) {

        Appender result = null;

        Logger rootLogger = getRootLogger();

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {

            Appender a = (Appender)e.nextElement();

            Class<? extends Appender> type = a.getClass();

            if (!type.equals(expectedType)) {

                continue;
            }

            String aName = a.getName();

            if (name.equals(aName)) {

                result = a;
                break;
            }
        }

        return result;
    }

    /**
     * Returns the first ConsoleAppender registered with the root logger, which also has the given attributes. May
     * return null if no ConsoleAppender with the given attributes was configured.
     */
    public static ConsoleAppender getConsoleAppender(String name, String expectedTarget) {

        ConsoleAppender ca = (ConsoleAppender)getAppender(ConsoleAppender.class, name);

        if (ca == null) {

            return null;
        }

        String target = ca.getTarget();

        return target.equals(expectedTarget) ? ca : null;
    }

    /**
     * @return all FileAppenders registered with the root logger.
     */
    public static List<FileAppender> getFileAppenders() {

        List<FileAppender> result = new ArrayList<>();

        Logger rootLogger = getRootLogger();

        for(Enumeration e = rootLogger.getAllAppenders(); e.hasMoreElements(); ) {

            Appender a = (Appender)e.nextElement();

            Class<? extends Appender> type = a.getClass();

            if (FileAppender.class.isAssignableFrom(type)) {

                result.add((FileAppender)a);
            }
        }

        return result;
    }

    /**
     * @return the highest verbosity logging level among the given LoggerConfigurations. May return null if the list
     * is empty.
     */
    public static Log4jLevel getHighestVerbosityLoggingLevel(List<LoggerConfiguration> configs) {

        Log4jLevel result = null;

        for(LoggerConfiguration lc: configs) {

            if (result == null) {

                result = lc.getLevel();
            }
            else {

                Log4jLevel other = lc.getLevel();

                if (other.compareTo(result) > 0) {

                    result = other;
                }
            }
        }

        return result;
    }

    /**
     * @return all FileAppender registered with the root logger that has the specified name, or null if there is no
     * match.
     */
    public static FileAppender getFileAppender(String name) {

        for(FileAppender a: getFileAppenders()) {

            if (a.getName().equals(name)) {

                return a;
            }
        }

        return null;
    }

    public static void setLevel(Logger logger, Log4jLevel level) {

        Level currentLevel = logger.getLevel();
        Level newLevel = level.getLog4jNativeLevel();


        if (currentLevel.isGreaterOrEqual(newLevel)) {

            if (!currentLevel.equals(newLevel)) {

                logger.setLevel(newLevel);
            }
        }
    }

    /**
     * @return the console appender that was created and added to the root logger.
     */
    public static ConsoleAppender addConsoleAppender(String name, String target, String layoutPattern) {

        Logger rootLogger = getRootLogger();
        Layout layout = new PatternLayout(layoutPattern);
        ConsoleAppender ca = new ConsoleAppender(layout, target);
        ca.setName(name);
        rootLogger.addAppender(ca);
        return ca;
    }

    /**
     * Creates a new FileAppender and registers it with the root logger.
     *
     * @return the file appender that was created and added to the root logger.
     */
    public static FileAppender addFileAppender(String name, File file, String layoutPattern) throws IOException {

        Logger rootLogger = getRootLogger();
        Layout layout = new PatternLayout(layoutPattern);
        FileAppender fa = new RollingFileAppender(layout, file.getPath());
        fa.setName(name);
        rootLogger.addAppender(fa);
        return fa;
    }

    /**
     * Removes the file appender with the specified name from the root logger.
     *
     * @return true if state was changed, false if no such file appender exists and in consequence, state was not
     * changed.
     */
    public static boolean removeFileAppender(String name) {

        FileAppender fa = getFileAppender(name);

        if (fa == null) {

            return false;
        }

        Logger rootLogger = getRootLogger();

        rootLogger.removeAppender(fa);

        fa = getFileAppender(name);

        if (fa != null) {

            throw new IllegalStateException("failed to remove file appender " + fa);
        }

        return true;
    }

    /**
     * Removes the console appender with the specified name from the root logger.
     *
     * @return the console appender instance that was remove, or null if no such console appender exists and in
     * consequence, state was not changed.
     */
    public static ConsoleAppender removeConsoleAppender(String name, String expectedTarget) {

        ConsoleAppender original = getConsoleAppender(name, expectedTarget);

        if (original == null) {

            return null;
        }

        Logger rootLogger = getRootLogger();

        rootLogger.removeAppender(original);

        ConsoleAppender ca = getConsoleAppender(name, expectedTarget);

        if (ca != null) {

            throw new IllegalStateException("failed to remove console appender " + ca);
        }

        return original;
    }

    /**
     * Set a new threshold on the given appender.
     * @return true if the operation changed the appender, or false if the existing level was already appropriate
     * and no change was necessary.
     */
    public static boolean setLevel(AppenderSkeleton appender, Log4jLevel level) {

        Priority stderrPriority = appender.getThreshold();

        Level newLevel = level.getLog4jNativeLevel();

        if (stderrPriority == null || stderrPriority.isGreaterOrEqual(newLevel)) {

            if (!newLevel.equals(stderrPriority)) {

                appender.setThreshold(newLevel);
                return true;

            }
        }

        return false;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
