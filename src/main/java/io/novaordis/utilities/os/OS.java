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
 * A proxy for the underlying operating system, to be used for native command execution.
 *
 * Implementations must correctly implement equals() and hashCode().
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public interface OS {

    // Constants -------------------------------------------------------------------------------------------------------

    //
    // Conventional OS names
    //

    String MacOS = "MacOS";
    String Linux = "Linux";
    String Windows = "Windows";

    // Static ----------------------------------------------------------------------------------------------------------

    static OS getInstance() throws Exception {

        String osName = System.getProperty("os.name");

        if (osName == null) {
            throw new IllegalStateException("'os.name' system property not available");
        }

        String lcOsName = osName.toLowerCase();

        if (lcOsName.contains("mac")) {
            return new MacOS();
        }
        else if (lcOsName.contains("linux")) {
            return new LinuxOS();
        }
        else if (lcOsName.contains("windows")) {
            return new WindowsOS();
        }
        else {
            throw new IllegalStateException("unrecognized 'os.name' value " + osName);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @see OSConfiguration
     */
    OSConfiguration getConfiguration();

    /**
     * Recommended usage pattern:
     *

     try {

        NativeExecutionResult result = os.execute(commandName);

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
     */
    NativeExecutionResult execute(String command) throws NativeExecutionException;

    /**
     * "Linux" (OS.Linux), "MacOS" (OS.MacOS), "Windows" (OS.Windows).
     *
     * @see OS#Linux
     * @see OS#MacOS
     * @see OS#Windows
     */
    String getName();

}
