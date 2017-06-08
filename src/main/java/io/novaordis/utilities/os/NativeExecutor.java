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

package io.novaordis.utilities.os;

import java.io.File;

/**
 * Encapsulates the capability of executing an O/S command. Most common implementation is the local OS, but OSes
 * executing on remote hosts may also do the same thing.
 *
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/8/17
 */
public interface NativeExecutor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @see NativeExecutor#execute(File, String)
     */
    NativeExecutionResult execute(String command) throws NativeExecutionException;

    /**
     * @param directory the directory to execute the command into, relative to the file system of the target
     *                  executor. null has the semantics of executing the command in current directory.
     *
     * @param command A double quote enclosed sequence is sent to the underlying API as one string.
     *
     * Recommended usage pattern:
     *
    <pre>

    try {

        NativeExecutionResult result = impl.execute(commandName);

        if (result.isSuccess()) {
            stdout = result.getStdout();
        }
        else {
            log.warn(result.getStderr());
        }
    }
    catch(NativeExecutionException e) {

        String msg = e.getMessage();
        String warningMsg = msg != null ? msg : "";
        Throwable cause = e.getCause();
        if (cause != null) {
            String causeMsg = cause.getClass().getSimpleName();
            if (cause.getMessage() != null) {
                causeMsg += ": " + cause.getMessage();
            }
            warningMsg += ", " + causeMsg;
        }
        log.warn(warningMsg);
    }

    </pre>

    @see NativeExecutor#execute(String)
     */
    NativeExecutionResult execute(File directory, String command) throws NativeExecutionException;


}
