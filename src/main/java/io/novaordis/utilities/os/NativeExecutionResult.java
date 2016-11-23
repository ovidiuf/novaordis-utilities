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

    private int exitStatus;
    private String stdout;
    private String stderr;


    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param exitStatus an exit status that must be between 0 and 255. Anything else will trigger an
     *                   IllegalArgumentException.
     * @param stdoutContent - null is acceptable. Multi-line content is acceptable.
     * @param stderrContent- null is acceptable. Multi-line content is acceptable.
     *
     * @exception IllegalArgumentException
     */
    public NativeExecutionResult(int exitStatus, String stdoutContent, String stderrContent) {

        if (exitStatus < 0 || exitStatus > 255) {
            throw new IllegalArgumentException("illegal exit status " + exitStatus);
        }

        this.exitStatus = exitStatus;
        this.stdout = stdoutContent;
        this.stderr = stderrContent;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true the corresponding command execution was a success - it completed and returned a zero exit status.
     */
    public boolean isSuccess() {

        return exitStatus == 0;
    }

    /**
     * @return true the corresponding command execution was a failure - it completed and returned a non-zero exit status.
     */
    public boolean isFailure() {

        return !isSuccess();
    }

    /**
     * @return the content sent by the corresponding command to stdout during the execution. May return null.
     */
    public String getStdout() {

        return stdout;
    }

    /**
     * @return the content sent by the corresponding command to stderr during the execution. May return null.
     */
    public String getStderr() {

        return stderr;
    }

    /**
     * @return the corresponding native command exit status. Guaranteed to return a valid value between 0 and 255.
     */
    public int getExitStatus() {

        return exitStatus;
    }

    @Override
    public String toString() {

        return "" + exitStatus;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
