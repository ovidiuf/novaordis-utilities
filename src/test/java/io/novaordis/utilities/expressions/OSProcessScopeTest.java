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

import io.novaordis.utilities.NotSupportedException;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertNotNull;
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

    /**
     * We cannot declare undefined environment variable, they must have a value, even if it is an empty string.
     * @throws Exception
     */
    @Test
    @Override
    public void declare_StringVariable_Undefined() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", String.class);
            fail("should have thrown exception");
        }
        catch(NotSupportedException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
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

    /**
     * Throws a different exception.
     */
    @Test
    public void declare_TypeInferenceBasedOnValue_Null() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", null);
            fail("should have thrown exception");
        }
        catch(NotSupportedException e) {

            String msg = e.getMessage();
            assertNotNull(msg);
        }
    }

    /**
     * Throws a different exception.
     */
    @Test
    public void declare_UnsupportedType() throws Exception {

        Class type = Void.class;

        Scope s = getScopeToTest();

        try {

            s.declare("something", type);
        }
        catch(NotSupportedException e) {

            String msg = e.getMessage();
            assertNotNull(msg);
        }
    }

    /**
     * Throws a different exception.
     */
    @Test
    public void declare_TypeInferenceBasedOnValue_UnsupportedType() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", new AtomicBoolean(true));
            fail("should have thrown exception");
        }
        catch(NotSupportedException e) {

            String msg = e.getMessage();
            assertNotNull(msg);
        }
    }

    // getVariable() test overrides ------------------------------------------------------------------------------------


    /**
     * We cannot declare undefined environment variable, they must have a value, even if it is an empty string.
     * @throws Exception
     */
    @Test
    @Override
    public void getVariable_NullValue() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", String.class);
            fail("should have thrown exception");
        }
        catch(NotSupportedException e) {

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

    // declare() -------------------------------------------------------------------------------------------------------

    /**
     * We cannot declare undefined environment variable, they must have a value, even if it is an empty string.
     * @throws Exception
     */
    @Test
    public void declare_StringVariable_ExplicitlyUndefined() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", String.class, null);
            fail("should have thrown exception");
        }
        catch(NotSupportedException e) {

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

            s.declare("test", Integer.class, 1);
            fail("should have thrown exception");
        }
        catch(NotSupportedException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
            assertTrue(msg.contains(Integer.class.toString()));
        }
    }

    // evaluate() ------------------------------------------------------------------------------------------------------

    /**
     * We cannot have "defined but null" with environment variables.
     */
    @Test
    @Override
    public void evaluate_VariableNameParsing_SimpleDeclaration_VariableDefinedButNull() throws Exception {

        //
        // noop
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

//    @Test
//    public void environmentVariableProvider() {
//
//        NortVariableProvider p = new NortVariableProvider();
//
//        EnvironmentVariableProvider original = p.getEnvironmentVariableProvider();
//
//        assertNotNull(original);
//
//        MockEnvironmentVariableProvider p2 = new MockEnvironmentVariableProvider();
//
//        p.setEnvironmentVariableProvider(p2);
//
//        EnvironmentVariableProvider p3 = p.getEnvironmentVariableProvider();
//
//        assertEquals(p3, p2);
//        assertNotEquals(p3, original);
//
//        //
//        // we don't have a parent
//        //
//        assertNull(p.getVariableProviderParent());
//
//        try {
//
//            p.setVariableProviderParent(new MockVariableProvider());
//            fail("should have thrown exception");
//        }
//        catch(UnsupportedOperationException e) {
//
//            log.info("" + e);
//        }
//    }
//
//    @Test
//    public void resolveAnEnvironmentVariable() {
//
//        MockEnvironmentVariableProvider mp = new MockEnvironmentVariableProvider();
//
//        NortVariableProvider p = new NortVariableProvider();
//        p.setEnvironmentVariableProvider(mp);
//
//        assertNull(p.getVariableValue("NO_SUCH_VARIABLE"));
//
//        String value = "some value";
//        String variableName = "SOME_VARIABLE";
//        mp.installEnvironmentVariable(variableName, value);
//
//        String s = p.getVariableValue(variableName);
//        assertEquals("some value", s);
//    }


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
