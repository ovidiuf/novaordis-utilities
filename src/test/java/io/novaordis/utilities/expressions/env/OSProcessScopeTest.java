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

package io.novaordis.utilities.expressions.env;

import io.novaordis.utilities.expressions.Scope;
import io.novaordis.utilities.expressions.ScopeTest;
import io.novaordis.utilities.expressions.StringVariable;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/17/17
 */
public class OSProcessScopeTest extends ScopeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void declare_StringVariable_Undefined() throws Exception {

        //
        // We cannot declare undefined (null) environment variable, they must have a value, even if it is an empty
        // string.
        //

        Scope s = getScopeToTest();

        try {

            s.declare("test", String.class);

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
            assertTrue(msg.contains("declare"));
            assertTrue(msg.contains("undefined"));
        }
    }

    @Test
    @Override
    public void declare_DuplicateDeclaration_DifferentTypes() throws Exception {

        //
        // noop - we cannot declare environment variables of any other type than String
        //
    }

    @Test
    @Override
    public void declare_TypeInferenceBasedOnValue_Null() throws Exception {

        Scope s = getScopeToTest();

        try {

            //
            // Throws a different exception
            //

            s.declare("test", null);

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertNotNull(msg);
        }
    }

    @Test
    @Override
    public void declare_UnsupportedType() throws Exception {

        Class type = Void.class;

        Scope s = getScopeToTest();

        try {

            //
            // Throws a different exception
            //

            s.declare("something", type);

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertNotNull(msg);
        }
    }

    @Test
    @Override
    public void declare_TypeInferenceBasedOnValue_UnsupportedType() throws Exception {

        Scope s = getScopeToTest();

        try {

            //
            // Throws a different exception
            //

            s.declare("test", new AtomicBoolean(true));

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertNotNull(msg);
        }
    }

    // getVariable() test overrides ------------------------------------------------------------------------------------

    @Test
    @Override
    public void getVariable_NullValue() throws Exception {

        //
        // We cannot declare undefined environment variable, they must have a value, even if it is an empty string.
        //

        Scope s = getScopeToTest();

        try {

            s.declare("test", String.class);

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
            assertTrue(msg.contains("undefined"));
        }
    }

    @Test
    @Override
    public void evaluate_VariableNameParsing_CompleteDeclaration_VariableDefinedButNull() throws Exception {

        //
        // noop - we cannot declare null environment variables
        //
    }

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        OSProcessScope s = new OSProcessScope();

        //
        // make sure we have a default environment variable provider
        //

        EnvironmentVariableProvider p = s.getEnvironmentVariableProvider();
        assertNotNull(p);
    }

    // declare() -------------------------------------------------------------------------------------------------------

    /**
     * We cannot declare undefined environment variable, they must have a value, even if it is an empty string.
     * @throws Exception
     */
    @Test
    public void declare_StringVariable_ExplicitlyUndefined() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", null);

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
            assertTrue(msg.contains("undefined"));
        }
    }

    /**
     * We cannot declare undefined environment variable, they must have a value, even if it is an empty string.
     * @throws Exception
     */
    @Test
    public void declare_NotStringVariable() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", 1);

            fail("should have thrown exception");
        }
        catch(UnsupportedOperationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
            assertTrue(msg.contains(Integer.class.toString()));
        }
    }

    // evaluate() ------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void evaluate_VariableNameParsing_SimpleDeclaration_VariableDefinedButNull() throws Exception {

        //
        // noop - we cannot have "defined but null" with environment variables.
        //
    }

    // legacy ----------------------------------------------------------------------------------------------------------

    @Test
    @Override
    public void legacy_SetsValueToNull() throws Exception {

        //
        // noop - we cannot set values to null
        //
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void environmentVariableProvider() throws Exception {

        OSProcessScope p = getScopeToTest();

        EnvironmentVariableProvider original = p.getEnvironmentVariableProvider();

        assertNotNull(original);

        MockEnvironmentVariableProvider p2 = new MockEnvironmentVariableProvider();

        p.setEnvironmentVariableProvider(p2);

        EnvironmentVariableProvider p3 = p.getEnvironmentVariableProvider();

        assertEquals(p3, p2);
        assertNotEquals(p3, original);

        //
        // because we are not encloseable, we don't have a parent
        //
    }

    @Test
    public void resolveAnEnvironmentVariable_MockEnvironment() throws Exception {

        OSProcessScope p = getScopeToTest();

        MockEnvironmentVariableProvider mp = new MockEnvironmentVariableProvider();

        p.setEnvironmentVariableProvider(mp);

        assertNull(p.getVariable("NO_SUCH_VARIABLE"));

        String value = "some value";
        String variableName = "SOME_VARIABLE";

        mp.installEnvironmentVariable(variableName, value);

        StringVariable v = p.getVariable(variableName);
        assertEquals("some value", v.get());
    }

    @Test
    public void resolveAnEnvironmentVariable_RealEnvironment() throws Exception {

        OSProcessScope p = getScopeToTest();

        //
        // install the real environment variable provider
        //

        p.setEnvironmentVariableProvider(new SystemEnvironmentVariableProvider());

        StringVariable v = p.getVariable("USER");

        assertNotNull(v);

        String s = v.get();
        assertNotNull(s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected OSProcessScope getScopeToTest() throws Exception {

        //
        // we inject a "write-capable" mock environment variable provider, so most of the ScopeTests will work,
        // without being forced to override them.
        //

        WriteCapableMockEnvironmentVariableProvider p = new WriteCapableMockEnvironmentVariableProvider();
        OSProcessScope s = new OSProcessScope();
        s.setEnvironmentVariableProvider(p);
        return s;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
