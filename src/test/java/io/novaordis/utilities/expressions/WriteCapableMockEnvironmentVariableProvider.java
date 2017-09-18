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

package io.novaordis.utilities.expressions;

import io.novaordis.utilities.env.EnvironmentVariableProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/17/17
 */
public class WriteCapableMockEnvironmentVariableProvider implements EnvironmentVariableProvider {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Map<String, String> mockEnvironment;

    // Constructors ----------------------------------------------------------------------------------------------------

    public WriteCapableMockEnvironmentVariableProvider() {

        this.mockEnvironment = new HashMap<>();
    }

    // EnvironmentVariableProvider implementation ----------------------------------------------------------------------

    @Override
    public String getenv(String name) {

        if (name == null) {

            //
            // as per interface description
            //
            throw new NullPointerException("name");
        }

        //
        // must return null if the environment variable is not declared
        //

        //noinspection UnnecessaryLocalVariable
        String s = mockEnvironment.get(name);
        return s;
    }

    @Override
    public void export(String name, String value) {

        mockEnvironment.put(name, value);
    }

    @Override
    public void unset(String name) {

        mockEnvironment.remove(name);
    }


    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
