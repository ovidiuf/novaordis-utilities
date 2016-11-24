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
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
abstract class OSBase implements OS {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OSBase.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Package Protected Static ----------------------------------------------------------------------------------------

    /**
     * Splits into tokens, taking into account double quotes.
     */
    static String[] split(String s) {

        List<String> tokens = new ArrayList<>();

        String token = "";
        boolean quoted = false;

        for(int i = 0; i < s.length(); i ++) {

            char c = s.charAt(i);

            if (quoted) {

                if (c == '"') {

                    //
                    // end quotes
                    //

                    tokens.add(token);
                    token = "";
                    quoted = false;
                    continue;
                }
            }
            else if (c == ' ' || c == '\t') {

                if (token.length() > 0) {
                    tokens.add(token);
                    token = "";
                }
                continue;
            }
            else if (c == '"') {

                quoted = true;
                continue;
            }

            token += c;
        }

        //
        // process last token
        //

        if (token.length() > 0) {
            tokens.add(token);
        }

        String[] result = new String[tokens.size()];
        return tokens.toArray(result);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // OS implementation -----------------------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {
        return execute(null, command);
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {

        if (directory == null) {
            directory = new File(".");
        }

        OS.logExecution(log, directory, command);

        //
        // Linux and MacOS implementations should be identical; for Windows, will override if necessary
        //

        String[] commands = split(command);

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
