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

import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void declare_StringVariable_Undefined() throws Exception {

        Scope s = getScopeToTest();

        Variable<String> v = s.declare("test", String.class);

        assertNotNull(v);
        assertEquals("test", v.name());
        assertNull(v.get());
        assertEquals(String.class, v.type());
    }

    @Test
    public void declare_StringVariable_Defined() throws Exception {

        Scope s = getScopeToTest();

        Variable<String> v = s.declare("test", String.class, "some-value");

        assertNotNull(v);
        assertEquals("test", v.name());
        assertEquals("some-value", v.get());
        assertEquals(String.class, v.type());
    }

    @Test
    public void declare_StringVariable_Defined2() throws Exception {

        Scope s = getScopeToTest();

        Variable<String> v = s.declare("test", "some-value");

        assertNotNull(v);
        assertEquals("test", v.name());
        assertEquals("some-value", v.get());
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

        assertNotNull(s.declare("test", String.class, "test value"));

        try {

            s.declare("test", String.class, "some other test value");
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

    @Test
    public void declare_TypeInferenceBasedOnValue_Null() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", null);
            fail("should have thrown exception");
        }
        catch(IllegalTypeException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("variable type cannot be inferred"));
        }
    }

    @Test
    public void declare_TypeInferenceBasedOnValue_String() throws Exception {

        Scope s = getScopeToTest();

        Variable<String> v = s.declare("test", "something");

        assertEquals("test", v.name());
        assertEquals("something", v.get());
        assertEquals(String.class, v.type());
        assertTrue(v instanceof StringVariable);
    }

    @Test
    public void declare_TypeInferenceBasedOnValue_UnsupportedType() throws Exception {

        Scope s = getScopeToTest();

        try {

            s.declare("test", new AtomicBoolean(true));
            fail("should have thrown exception");
        }
        catch(IllegalTypeException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(AtomicBoolean.class.getName()));
        }
    }

    @Test
    public void declare_Twice() throws Exception {

        Scope s = getScopeToTest();

        s.declare("A", "something");

        try {

            s.declare("A", "something");
            fail("should have thrown exception");
        }
        catch(DuplicateDeclarationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("A"));
        }
    }

    // undeclare() -----------------------------------------------------------------------------------------------------

    @Test
    public void undeclare_NoSuchVariable() throws Exception {

        Scope s = getScopeToTest();

        Variable v = s.undeclare("i-am-pretty-sure-this-variable-does-not-exist");
        assertNull(v);
    }

    @Test
    public void undeclare() throws Exception {

        Scope s = getScopeToTest();

        s.declare("test", "something");

        assertEquals("something", s.getVariable("test").get());

        Variable v2 = s.undeclare("test");

        assertEquals("something", v2.get());

        assertNull(s.getVariable("test"));

        Variable v3 = s.undeclare("test");

        assertNull(v3);
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
    public void getVariable_FromEnclosedScope() throws Exception {

        Scope top = getScopeToTest();

        //
        // we explicitely use a generic ScopeImpl, because some scopes, like the OSProcessScope, cannot be enclosed
        //
        ScopeImpl bottom = new ScopeImpl();

        top.enclose(bottom);

        top.declare("test", String.class, "something");

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
    public void evaluate_Null() throws Exception {

        Scope scope = getScopeToTest();

        try {

            scope.evaluate(null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.equals("null string"));
        }
    }

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
            assertTrue(msg.contains("unbalanced closing '}'"));
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
            assertTrue(msg.contains("unbalanced closing '}'"));
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
            assertTrue(msg.contains("unbalanced closing '}'"));
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

            scope.evaluate("${some${thing}}");
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("misplaced"));
            assertTrue(msg.contains("$"));
            assertTrue(msg.contains("in variable reference"));
        }
    }

    @Test
    public void evaluate_InvalidVariableReference3() throws Exception {

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

    @Test
    public void evaluate_FailOnUndeclaredVariable_NoUndeclaredVariable() throws Exception {

        Scope scope = getScopeToTest();

        scope.declare("a", "A");

        String result = scope.evaluate("test ${a}", true);

        assertEquals("test A", result);
    }

    @Test
    public void evaluate_FailOnUndeclaredVariable_UndeclaredVariablePresent() throws Exception {

        Scope scope = getScopeToTest();

        assertNull(scope.getVariable("a"));

        try {

            scope.evaluate("test ${a}", true);
            fail("should have thrown exception");
        }
        catch(UndeclaredVariableException e) {

            String n = e.getUndeclaredVariableName();
            assertEquals("a", n);
        }
    }

    @Test
    public void evaluate_FailOnUndeclaredVariable_UndeclaredVariablePresent_MultipleUndeclaredVariables()
            throws Exception {

        Scope scope = getScopeToTest();

        assertNull(scope.getVariable("a"));
        assertNull(scope.getVariable("m"));
        assertNull(scope.getVariable("z"));

        try {

            scope.evaluate("test ${z}, ${m}, ${a}", true);
            fail("should have thrown exception");
        }
        catch(UndeclaredVariableException e) {

            String n = e.getUndeclaredVariableName();
            assertEquals("z", n);
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

        scope.declare("z", String.class, "something");

        List<Variable> declaredInScope = scope.getVariablesDeclaredInScope();

        assertEquals(1, declaredInScope.size());

        Variable v = declaredInScope.get(0);

        assertEquals("z", v.name());
        assertEquals("something", v.get());

        //noinspection unchecked
        v.set("dirty");
        assertEquals("dirty", v.get());

        List<Variable> declaredInScope2 = scope.getVariablesDeclaredInScope();

        Variable v2 = declaredInScope2.get(0);

        assertEquals("z", v2.name());
        assertEquals("dirty", v2.get());

        //
        // declare another variable
        //

        scope.declare("a", String.class, "something else");

        List<Variable> declaredInScope3 = scope.getVariablesDeclaredInScope();

        assertEquals(2, declaredInScope3.size());

        Variable v3 = declaredInScope3.get(0);
        assertEquals("z", v3.name());
        assertEquals("dirty", v3.get());

        Variable v4 = declaredInScope3.get(1);
        assertEquals("a", v4.name());
        assertEquals("something else", v4.get());

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
        assertEquals("dirty", v5.get());

        Variable v6 = declaredInScope4.get(1);
        assertEquals("a", v6.name());
        assertEquals("dirty", v6.get());
    }

    // legacy tests inherited from the previous implementation ---------------------------------------------------------

    @Test
    public void legacy_DoesNotSetValueToNull() throws Exception {

        Scope s = getScopeToTest();

        assertNull(s.getVariable("something"));

        Variable v = s.declare("something", "A");
        assertEquals("something", v.name());
        assertEquals("A", v.get());

        assertEquals("A", s.getVariable("something").get());

        //noinspection unchecked
        assertEquals("A", s.getVariable("something").set("B"));

        assertEquals("B", s.getVariable("something").get());

        //noinspection unchecked
        assertEquals("B", s.getVariable("something").set("C"));

        assertEquals("C", s.getVariable("something").get());

        //noinspection unchecked
        assertEquals("C", s.getVariable("something").set("D"));
    }

    @Test
    public void legacy_SetsValueToNull() throws Exception {

        Scope s = getScopeToTest();

        assertNull(s.getVariable("something"));

        Variable v = s.declare("something", "A");
        assertEquals("something", v.name());
        assertEquals("A", v.get());

        assertEquals("A", s.getVariable("something").get());

        //noinspection unchecked
        assertEquals("A", s.getVariable("something").set("B"));

        assertEquals("B", s.getVariable("something").get());

        //noinspection unchecked
        assertEquals("B", s.getVariable("something").set(null));

        assertNull(s.getVariable("something").get());

        //noinspection unchecked
        assertNull(s.getVariable("something").set("C"));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Scope getScopeToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
