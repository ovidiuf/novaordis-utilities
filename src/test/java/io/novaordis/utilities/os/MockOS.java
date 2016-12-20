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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/1/16
 */
public class MockOS implements OS {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<String> commandsThatThrowException;
    private List<String> commandsThatFail;
    private List<MockCommand> commandsThatSucceed;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected MockOS() throws Exception {

        commandsThatThrowException = new ArrayList<>();
        commandsThatFail = new ArrayList<>();
        commandsThatSucceed = new ArrayList<>();
    }

    // OS implementation -----------------------------------------------------------------------------------------------

    @Override
    public OSConfiguration getConfiguration() {
        throw new RuntimeException("getConfiguration() NOT YET IMPLEMENTED");
    }

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {
        return execute(null, command);
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {

        //
        // first try the commands that throw exceptions
        //

        if (commandsThatThrowException.contains(command)) {
            throw new NativeExecutionException("SYNTHETIC");
        }

        //
        // ... then try commands that fail
        //

        if (commandsThatFail.contains(command)) {
            return new NativeExecutionResult(1, null, "synthetic failure", true, true);
        }


        //
        // these are the commands we mock and succeed
        //
        for(MockCommand c: commandsThatSucceed) {

            if (c.getCommand().equals(command)) {
                return new NativeExecutionResult(0, c.getStdout(), c.getStderr(), true, true);
            }
        }

        //
        // we simulate pwd
        //

        if ("pwd".equals(command)) {

            return new NativeExecutionResult(0, directory.toString() + "\n", null, true, true);
        }

        throw new RuntimeException("we don't know how to mock command " + command);
    }

    @Override
    public String getName() {
        return "MockOS";
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Add command to the list of the commands that "break" - they will throw a NativeExecutionException with the
     * message "SYNTHETIC".
     */
    public void addCommandThatThrowsException(String s) {

        commandsThatThrowException.add(s);
    }

    public void addCommandThatFails(String s) {

        commandsThatFail.add(s);
    }

    public void addCommandThatSucceeds(String command, String stdout, String stderr) {

        commandsThatSucceed.add(new MockCommand(command, stdout, stderr));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

    private class MockCommand {

        private String command;
        private String stderr;
        private String stdout;

        MockCommand(String command, String stdout, String stderr) {
            this.command = command;
            this.stdout = stdout;
            this.stderr  = stderr;
        }

        public String getStdout() {
            return stdout;
        }

        public String getStderr() {
            return stderr;
        }

        public String getCommand() {
            return command;
        }
    }
}
