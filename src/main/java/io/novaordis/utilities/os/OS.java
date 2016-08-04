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
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
public interface OS {

    // Constants -------------------------------------------------------------------------------------------------------

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

    NativeExecutionResult execute(String command) throws NativeExecutionException;

    /**
     * "Linux", "MacOS", "Windows".
     */
    String getName();

}
