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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void getVariable_NullValue() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("test", String.class);

        Variable v = scope.getVariable("test");
        assertNotNull(v);
        assertNull(v.get());
        assertEquals("test", v.name());
    }

    // evaluate() ------------------------------------------------------------------------------------------------------

    @Test
    public void evaluate_NoVariables() throws Exception {

        Scope scope = getScopeToTest();
        String result = scope.evaluate("gobbledygook");
        assertEquals("gobbledygook", result);
    }

    @Test
    public void evaluate_VariableNameParsing_CompleteDeclaration_VariableDefinedAndNonNull() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("b", String.class, "x");

        String result = scope.evaluate("a${b}c");

        assertEquals("axc", result);
    }

    @Test
    public void evaluate_VariableNameParsing_CompleteDeclaration_VariableDefinedButNull() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("b", String.class);

        String result = scope.evaluate("a${b}c");

        assertEquals("ac", result);
    }

    @Test
    public void evaluate_VariableNameParsing_CompleteDeclaration_VariableNotDefined() throws Exception {

        Scope scope = getScopeToTest();
        assertNull(scope.getVariable("b"));

        String result = scope.evaluate("a${b}c");

        assertEquals("a${b}c", result);
    }

    @Test
    public void evaluate_VariableNameParsing_SimpleDeclaration_VariableDefinedAndNonNull() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("b", String.class, "x");

        String result = scope.evaluate("a$b");

        assertEquals("ax", result);
    }

    @Test
    public void evaluate_VariableNameParsing_SimpleDeclaration_VariableDefinedButNull() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("b", String.class);

        String result = scope.evaluate("a$b");

        assertEquals("a", result);
    }

    @Test
    public void evaluate_VariableNameParsing_SimpleDeclaration_VariableNotDefined() throws Exception {

        Scope scope = getScopeToTest();
        assertNull(scope.getVariable("b"));

        String result = scope.evaluate("a$b");

        assertEquals("a$b", result);
    }

    @Test
    public void evaluate_VariableNameParsing_UnbalancedClosingBrace() throws Exception {

        Scope scope = getScopeToTest();

        try {

            scope.evaluate("$b}");
            fail("Should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing }"));
        }
    }

    @Test
    public void evaluate_VariableNameParsing_UnbalancedClosingBrace2() throws Exception {

        Scope scope = getScopeToTest();

        try {

            scope.evaluate("$b}c");
            fail("Should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing }"));
        }
    }

    @Test
    public void evaluate_VariableNameParsing_UnbalancedClosingBrace3() throws Exception {

        Scope scope = getScopeToTest();

        try {

            scope.evaluate("$b} ");
            fail("Should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing }"));
        }
    }

    @Test
    public void evaluate_VariableCannotBeResolved() throws Exception {

        Scope scope = getScopeToTest();
        String result = scope.evaluate("gobble${dy}gook");
        assertEquals("gobble${dy}gook", result);
    }

    @Test
    public void evaluate_VariableCannotBeResolved_TwoVariables() throws Exception {

        Scope scope = getScopeToTest();
        String result = scope.evaluate("gobble${dy}go${ok}");
        assertEquals("gobble${dy}go${ok}", result);
    }

    @Test
    public void evaluate_SimpleVariableReplacement() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("a", String.class, "gook");
        String result = scope.evaluate("gobbledy${a}");
        assertEquals("gobbledygook", result);
    }

    @Test
    public void evaluate_SimpleVariableReplacement2() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("a", String.class, "gobble");
        String result = scope.evaluate("${a}dygook");
        assertEquals("gobbledygook", result);
    }

    @Test
    public void evaluate_SimpleVariableReplacement3() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("a", String.class, "gobbledygook");
        String result = scope.evaluate("${a}");
        assertEquals("gobbledygook", result);
    }

    @Test
    public void evaluate_SimpleVariableReplacement4() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("a", String.class, "bbledygo");
        String result = scope.evaluate("go${a}ok");
        assertEquals("gobbledygook", result);
    }

    @Test
    public void evaluate_MultipleVariableReplacement() throws Exception {

        Scope scope = getScopeToTest();
        scope.declare("b", String.class, "b");
        scope.declare("d", String.class, "d");
        scope.declare("e", String.class, "e");
        scope.declare("g", String.class, "g");
        scope.declare("k", String.class, "k");
        scope.declare("l", String.class, "l");
        scope.declare("o", String.class, "o");
        scope.declare("y", String.class, "y");
        String result = scope.evaluate("${g}${o}${b}${b}${l}${e}${d}${y}${g}${o}${o}${k}");
        assertEquals("gobbledygook", result);
    }

    @Test
    public void evaluate_InvalidVariableReference() throws Exception {

        Scope scope = getScopeToTest();

        try {

            scope.evaluate("${{something}");
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("misplaced"));
            assertTrue(msg.contains("{"));
            assertTrue(msg.contains("in variable reference"));
        }
    }

    @Test
    public void evaluate_InvalidVariableReference2() throws Exception {

        Scope scope = getScopeToTest();

        try {

            scope.evaluate("${s");
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced"));
            assertTrue(msg.contains("{"));
            assertTrue(msg.contains("in variable reference"));
        }
    }

    // getVariablesDeclaredInScope() -----------------------------------------------------------------------------------

    @Test
    public void getVariablesDeclaredInScope_NoVariables() throws Exception {

        Scope scope = getScopeToTest();

        List<Variable> declaredInScope = scope.getVariablesDeclaredInScope();

        assertTrue(declaredInScope.isEmpty());
    }

    @Test
    public void getVariablesDeclaredInScope() throws Exception {

        Scope scope = getScopeToTest();

        scope.declare("z", String.class);

        List<Variable> declaredInScope = scope.getVariablesDeclaredInScope();

        assertEquals(1, declaredInScope.size());

        Variable v = declaredInScope.get(0);

        assertEquals("z", v.name());
        assertNull(v.get());

        //
        // make sure a copy was returned, not the variable itself
        //

        //noinspection unchecked
        v.set("dirty");
        assertEquals("dirty", v.get());

        List<Variable> declaredInScope2 = scope.getVariablesDeclaredInScope();

        Variable v2 = declaredInScope2.get(0);

        assertEquals("z", v2.name());
        assertNull(v2.get());

        //
        // declare another variable
        //

        scope.declare("a", String.class);

        List<Variable> declaredInScope3 = scope.getVariablesDeclaredInScope();

        assertEquals(2, declaredInScope3.size());

        Variable v3 = declaredInScope3.get(0);
        assertEquals("z", v3.name());
        assertNull(v3.get());

        Variable v4 = declaredInScope3.get(1);
        assertEquals("a", v4.name());
        assertNull(v4.get());

        //
        // make sure a copy was returned, not the variable itself
        //

        //noinspection unchecked
        v3.set("dirty");
        assertEquals("dirty", v3.get());

        //noinspection unchecked
        v4.set("dirty");
        assertEquals("dirty", v4.get());

        List<Variable> declaredInScope4 = scope.getVariablesDeclaredInScope();

        assertEquals(2, declaredInScope4.size());

        Variable v5 = declaredInScope4.get(0);
        assertEquals("z", v5.name());
        assertNull(v5.get());

        Variable v6 = declaredInScope4.get(1);
        assertEquals("a", v6.name());
        assertNull(v6.get());
    }

    @Test
    public void getVariablesDeclaredInScope_EnclosingScopesAreNotVisibleToThisMethod() throws Exception {

        Scope scope = getScopeToTest();

        Scope enclosing = getScopeToTest();

        enclosing.enclose(scope);

        enclosing.declare("color", String.class, "blue");

        assertTrue(scope.getVariablesDeclaredInScope().isEmpty());

        assertEquals("blue", scope.getVariable("color").get());

        List<Variable> declaredInScope = enclosing.getVariablesDeclaredInScope();

        assertEquals(1, declaredInScope.size());

        Variable v = declaredInScope.get(0);

        assertEquals("color", v.name());
        assertEquals("blue", v.get());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Scope getScopeToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
