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
 * @since 8/1/16
 */
public class MockLinuxOS extends LinuxOS {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private MockOS delegate;


    // Constructors ----------------------------------------------------------------------------------------------------

    protected MockLinuxOS() throws Exception {

        super(null);
        delegate = new MockOS();
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {

        return delegate.execute(command);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @see MockOS#addCommandThatThrowsException(String)
     */
    public void addCommandThatThrowsException(String s) {

        delegate.addCommandThatThrowsException(s);
    }

    /**
     * @see MockOS#addCommandThatFails(String)
     */
    public void addCommandThatFails(String s) {

        delegate.addCommandThatFails(s);
    }

    /**
     * @see MockOS#addCommandThatSucceeds(String, String, String)
     */
    public void addCommandThatSucceeds(String command, String stdout, String stderr) {

        delegate.addCommandThatSucceeds(command, stdout, stderr);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
