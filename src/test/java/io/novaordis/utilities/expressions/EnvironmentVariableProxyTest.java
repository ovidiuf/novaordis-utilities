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
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/17/17
 */
public class EnvironmentVariableProxyTest extends StringVariableTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    //
    // Overrides -------------------------------------------------------------------------------------------------------
    //

    @Test
    public void variableName_Null() throws Exception {

        //
        // we cannot declare environment variable with a null value, noop
        //
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // set() -----------------------------------------------------------------------------------------------------------

    @Test
    public void set_Proxy() throws Exception {

        EnvironmentVariableProxy v = getVariableToTest("A", String.class, "some value");

        OSProcessScope s = v.getOSProcessScope();

        EnvironmentVariableProvider p = s.getEnvironmentVariableProvider();

        v.set("some other value");

        String value = p.getenv("A");
        assertEquals("some other value", value);
    }

    // undeclared() ----------------------------------------------------------------------------------------------------

    @Test
    public void undeclared() throws Exception {

        EnvironmentVariableProxy v = getVariableToTest("A", String.class, "some value");

        v.setUndeclared();

        //
        // from this moment on, the variable does not take its value from the environment
        //

        v.getOSProcessScope().getEnvironmentVariableProvider().unset("A");
        assertNull(v.getOSProcessScope().getEnvironmentVariableProvider().getenv("A"));

        assertEquals("some value", v.get());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected <T> EnvironmentVariableProxy getVariableToTest(String name, Class<? extends T> type, T optionalValue)
            throws Exception {

        OSProcessScope defaultOSProcessScope = new OSProcessScope();

        WriteCapableMockEnvironmentVariableProvider mp = new WriteCapableMockEnvironmentVariableProvider();

        defaultOSProcessScope.setEnvironmentVariableProvider(mp);

        //noinspection UnnecessaryLocalVariable
        EnvironmentVariableProxy v = (EnvironmentVariableProxy)
                defaultOSProcessScope.declare(name, String.class, (String)optionalValue);

        //noinspection unchecked
        return v;
    }

    @Override
    protected Class getTypeToTest() {

        return String.class;
    }

    @Override
    protected Object getValueToTest() {

        return "some string";
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
