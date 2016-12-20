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

package io.novaordis.utilities.os;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 12/19/16
 */
public class MockLogger implements Logger {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String debugContent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockLogger() {

        this.debugContent = null;
    }

    // Logger implementation -------------------------------------------------------------------------------------------

    @Override
    public String getName() {
        throw new RuntimeException("getName() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isTraceEnabled() {
        throw new RuntimeException("isTraceEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(String msg) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(String format, Object arg) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(String format, Object... arguments) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(String msg, Throwable t) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        throw new RuntimeException("isTraceEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(Marker marker, String msg) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        throw new RuntimeException("trace() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isDebugEnabled() {
        throw new RuntimeException("isDebugEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(String msg) {

        if (debugContent == null) {

            debugContent = "";
        }

        debugContent += msg;
    }

    @Override
    public void debug(String format, Object arg) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(String format, Object... arguments) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(String msg, Throwable t) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        throw new RuntimeException("isDebugEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(Marker marker, String msg) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        throw new RuntimeException("debug() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isInfoEnabled() {
        throw new RuntimeException("isInfoEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(String msg) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(String format, Object arg) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(String format, Object... arguments) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(String msg, Throwable t) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        throw new RuntimeException("isInfoEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(Marker marker, String msg) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        throw new RuntimeException("info() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isWarnEnabled() {
        throw new RuntimeException("isWarnEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(String msg) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(String format, Object arg) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(String format, Object... arguments) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(String msg, Throwable t) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        throw new RuntimeException("isWarnEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(Marker marker, String msg) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        throw new RuntimeException("warn() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isErrorEnabled() {
        throw new RuntimeException("isErrorEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(String msg) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(String format, Object arg) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(String format, Object... arguments) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(String msg, Throwable t) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        throw new RuntimeException("isErrorEnabled() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(Marker marker, String msg) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        throw new RuntimeException("error() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return will return null if nothting has been logged since the instance construction or the last
     * getDebugContent() call.
     */
    public String getDebugContent() {

        if (debugContent == null) {
            return null;
        }

        String c = debugContent;
        debugContent = null;
        return c;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
