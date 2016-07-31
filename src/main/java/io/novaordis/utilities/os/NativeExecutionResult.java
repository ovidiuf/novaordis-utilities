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

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return true the corresponding command execution was a success - it completed and returned a zero status code.
     */
    public boolean isSuccess() {

        throw new RuntimeException("isSuccess() NOT YET IMPLEMENTED");
    }

    /**
     * @return the content sent by the corresponding command to stdout during the execution.
     */
    public String getStdout() {

        throw new RuntimeException("getStdout() NOT YET IMPLEMENTED");

    }

    /**
     * @return the content sent by the corresponding command to stderr during the execution.
     */
    public String getStderr() {

        throw new RuntimeException("getStderr() NOT YET IMPLEMENTED");
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
