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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public class NativeExecutionResult {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private int exitCode;
    private String stdout;
    private String stderr;
    private boolean stdoutDebugLoggingWasEnabled;
    private boolean stderrDebugLoggingWasEnabled;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param exitCode an exit status that must be between 0 and 255. Anything else will trigger an
     *                   IllegalArgumentException.
     * @param stdoutContent - null is acceptable. Multi-line content is acceptable.
     * @param stderrContent- null is acceptable. Multi-line content is acceptable.
     * @param stdoutDebugLoggingWasEnabled set to "true" if the stdout DEBUG logging was enabled at the OS
     *                                     implementation level so the stdout content was DEBUG logged already. Useful
     *                                     if we try to avoid duplicate logging.
     * @param stderrDebugLoggingWasEnabled set to "true" if the stderr DEBUG logging was enabled at the OS
     *                                     implementation level so the stderr content was DEBUG logged already. Useful
     *                                     if we try to avoid duplicate logging.
     *
     * @exception IllegalArgumentException
     */
    public NativeExecutionResult(int exitCode, String stdoutContent, String stderrContent,
                                 boolean stdoutDebugLoggingWasEnabled, boolean stderrDebugLoggingWasEnabled) {

        if (exitCode < 0 || exitCode > 255) {
            throw new IllegalArgumentException("illegal exit status " + exitCode);
        }

        this.exitCode = exitCode;
        this.stdout = stdoutContent;
        this.stderr = stderrContent;
        this.stdoutDebugLoggingWasEnabled = stdoutDebugLoggingWasEnabled;
        this.stderrDebugLoggingWasEnabled = stderrDebugLoggingWasEnabled;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true the corresponding command execution was a success - it completed and returned a zero exit status.
     */
    public boolean isSuccess() {

        return exitCode == 0;
    }

    /**
     * @return true the corresponding command execution was a failure - it completed and returned a non-zero exit status.
     */
    public boolean isFailure() {

        return !isSuccess();
    }

    /**
     * @return the content sent by the corresponding command to stdout during the execution. Anything that command
     * writes at stdout, even if it is just a new line character, will be returned. However, if the command does
     * not write anything to stdout and closes the stream, or writes an empty string to stdout - which is
     * equivalent to writing nothing - and closes the stream, getStdout() will return null.
     */
    public String getStdout() {

        return stdout;
    }

    /**
     * @return the content sent by the corresponding command to stderr during the execution. Anything that command
     * writes at stderr, even if it is just a new line character, will be returned. However, if the command does
     * not write anything to stderr and closes the stream, or writes an empty string to stderr - which is
     * equivalent to writing nothing - and closes the stream, getStderr() will return null.
     */
    public String getStderr() {

        return stderr;
    }

    /**
     * @return true if stdout DEBUG logging was enabled at the OS implementation level so the stdout content was DEBUG
     * logged already. Useful if we try to avoid duplicate logging.
     */
    public boolean wasStdoutDebugLoggingEnabled() {

        return stdoutDebugLoggingWasEnabled;
    }

    /**
     * @return true if stderr DEBUG logging was enabled at the OS implementation level so the stderr content was DEBUG
     * logged already. Useful if we try to avoid duplicate logging.
     */
    public boolean wasStderrDebugLoggingEnabled() {

        return stderrDebugLoggingWasEnabled;
    }

    /**
     * @return the corresponding native command exit code. Guaranteed to return a valid value between 0 and 255.
     */
    public int getExitCode() {

        return exitCode;
    }

    @Override
    public String toString() {

        return "" + exitCode;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
