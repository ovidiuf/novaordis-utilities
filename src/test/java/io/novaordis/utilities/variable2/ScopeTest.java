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

package io.novaordis.utilities.variable2;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public abstract class ScopeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // declare() -------------------------------------------------------------------------------------------------------

    @Test
    public void declare_StringVariable() throws Exception {

        Scope s = getScopeToTest();

        Variable<String> v = s.declare("test", String.class);

        assertNotNull(v);
        assertEquals("test", v.name());
        assertNull(v.get());
        assertEquals(String.class, v.type());
    }

    @Test
    public void declare_UnsupportedType() throws Exception {

        Class type = Void.class;

        Scope s = getScopeToTest();

        try {
            s.declare("something", type);

        }
        catch(IllegalTypeException e) {

            String msg = e.getMessage();
            assertEquals(type.toString(), msg);
        }
    }

    @Test
    public void declare_DuplicateDeclaration_SameType() throws Exception {

        Scope s = getScopeToTest();

        assertNotNull(s.declare("test", String.class));

        try {

            s.declare("test", String.class);
            fail("should have thrown exception");
        }
        catch(DuplicateDeclarationException e) {

            String msg = e.getMessage();
            assertEquals("test", msg);

        }
    }

    @Test
    public void declare_DuplicateDeclaration_DifferentTypes() throws Exception {

        Scope s = getScopeToTest();

        assertNotNull(s.declare("test", String.class));

        try {

            s.declare("test", Integer.class);
            fail("should have thrown exception");
        }
        catch(DuplicateDeclarationException e) {

            String msg = e.getMessage();
            assertEquals("test", msg);

        }
    }

    // scope visibility rules ------------------------------------------------------------------------------------------

    @Test
    public void visibility() throws Exception {

        //
        // Once a variable is declared in a scope, it becomes visible to all enclosed scopes (scopes that have this
        // scope as parent) but it is not visible to this scope's enclosing scopes.
        //

        Scope topMost = getScopeToTest();

        Scope intermediate = getScopeToTest();

        topMost.enclose(intermediate);

        Scope bottomMost = getScopeToTest();

        intermediate.enclose(bottomMost);

        Variable<String> v = intermediate.declare("test", String.class, "something");

        assertEquals("something", v.get());

        //
        // variable is not visible in the enclosing scope
        //

        Variable variableInTopMost = topMost.getVariable("test");
        assertNull(variableInTopMost);

        //
        // variable is visible (obviously) in the declaring scope
        //

        Variable variableInIntermediate = intermediate.getVariable("test");
        assertNotNull(variableInIntermediate);
        assertEquals("something", variableInIntermediate.get());

        //
        // variable is visible in the enclosed scope
        //

        Variable variableInBottomMost = bottomMost.getVariable("test");
        assertNotNull(variableInBottomMost);
        assertEquals("something", variableInBottomMost.get());
    }

    @Test
    public void visibility2() throws Exception {

        Scope upper = new ScopeImpl();
        Scope intermediate = new ScopeImpl();
        Scope lower = new ScopeImpl();

        upper.enclose(intermediate);
        intermediate.enclose(lower);

        intermediate.declare("color", String.class, "blue");

        assertNull(upper.getVariable("color"));
        assertEquals("blue", intermediate.getVariable("color").get());
        assertEquals("blue", lower.getVariable("color").get());
    }

    // getVariable() ---------------------------------------------------------------------------------------------------

    @Test
    public void getVariable_NoVariableWithTheNameDeclaredInScope() throws Exception {

        Scope s = getScopeToTest();

        assertNull(s.getVariable("no-such-variable"));
    }

    @Test
    public void getVariable_VariableDeclaredInScope() throws Exception {

        Scope s = getScopeToTest();

        s.declare("test", String.class, "something");

        Variable v = s.getVariable("test");

        assertNotNull(v);

        assertEquals("something", v.get());
    }

    @Test
    public void getVariable_VariableDeclaredOneLevelUp() throws Exception {

        Scope top = getScopeToTest();
        Scope bottom = getScopeToTest();

        top.enclose(bottom);

        top.declare("test", String.class, "something");

        Variable v = bottom.getVariable("test");

        assertNotNull(v);

        assertEquals("something", v.get());
    }

    @Test
    public void getVariable_VariableDeclaredTwoLevelsUp() throws Exception {

        Scope top = getScopeToTest();
        Scope intermediate = getScopeToTest();
        Scope bottom = getScopeToTest();

        top.enclose(intermediate);
        intermediate.enclose(bottom);

        top.declare("test", String.class, "something");

        Variable v = intermediate.getVariable("test");
        assertNotNull(v);
        assertEquals("something", v.get());

        Variable v2 = bottom.getVariable("test");
        assertNotNull(v2);
        assertEquals("something", v2.get());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Scope getScopeToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
