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
 * @see OSConfiguration
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/16
 */
public class LinuxOSConfiguration implements OSConfiguration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private int memoryPageSize;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * This is where the instance caches all the values, and this is where the instance has a chance to throw an
     * exception, if something goes wrong.
     *
     * The constructor must be package protected, we are not supposed to invoke it from outside the package. The proper
     * way to get a LinuxOSConfiguration instance is through LinuxOS.getConfiguration();
     *
     * @see LinuxOS#getConfiguration()
     *
     * @throws OSConfigurationException if we encounter a problem while trying to read the configuration.
     */
    LinuxOSConfiguration(LinuxOS linuxOS) throws OSConfigurationException {

        NativeExecutionResult result;

        try {

            result = linuxOS.execute("getconf PAGESIZE");
        }
        catch (Exception e) {
            throw new OSConfigurationException("failed to run getconf PAGESIZE", e);
        }

        if (!result.isSuccess()) {
            throw new OSConfigurationException("failed to get the system memory page size: " + result.getStderr());
        }

        String s = result.getStdout().trim();

        try {

            memoryPageSize = Integer.parseInt(s);
        }
        catch(Exception e) {
            throw new OSConfigurationException(
                    "system memory page size read from the system is not an integer: " + s, e);
        }
    }

    // OSConfiguration implementation ----------------------------------------------------------------------------------

    @Override
    public int getMemoryPageSize() {

        return memoryPageSize;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
