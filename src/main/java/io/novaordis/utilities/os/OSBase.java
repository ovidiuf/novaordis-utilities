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

import java.io.InputStream;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
abstract class OSBase implements OS {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // OS implementation -----------------------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {

        //
        // Linux and MacOS implementations should be identical; for Windows, will override if necessary
        //

        String[] commands = command.split(" +");

        try {

            //
            // TODO naive implementation, does not account for limited buffers, etc, must revisit ...
            //

            Process p = new ProcessBuilder().command(commands).start();

            int exitCode = p.waitFor();

            InputStream is = p.getInputStream();
            InputStream es = p.getErrorStream();

            StringBuilder inputBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();

            int c;
            while((c = is.read()) != -1) {
                inputBuilder.append((char) c);
            }

            while((c = es.read()) != -1) {
                errorBuilder.append((char) c);
            }

            String input = inputBuilder.toString();
            if (input.isEmpty()) {
                input = null;
            }

            String error = errorBuilder.toString();
            if (error.isEmpty()) {
                error = null;
            }

            return new NativeExecutionResult(exitCode, input, error);
        }
        catch(Exception e) {

            throw new NativeExecutionException("failed to execute command \"" + command + "\"", e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String s = getClass().getSimpleName();
        return s.substring(0, s.length() - 2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}