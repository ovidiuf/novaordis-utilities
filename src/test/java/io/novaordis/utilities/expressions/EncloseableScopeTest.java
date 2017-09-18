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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/18/17
 */
public abstract class EncloseableScopeTest extends ScopeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // getVariable() ---------------------------------------------------------------------------------------------------

    @Test
    public void getVariable_VariableDeclaredOneLevelUp() throws Exception {

        Scope top = getScopeToTest();
        EncloseableScope bottom = getScopeToTest();

        top.enclose(bottom);

        top.declare("test", "something");

        Variable v = bottom.getVariable("test");

        assertNotNull(v);

        assertEquals("something", v.get());
    }

    @Test
    public void getVariable_VariableDeclaredTwoLevelsUp() throws Exception {

        Scope top = getScopeToTest();
        EncloseableScope intermediate = getScopeToTest();
        EncloseableScope bottom = getScopeToTest();

        top.enclose(intermediate);
        intermediate.enclose(bottom);

        top.declare("test", "something");

        Variable v = intermediate.getVariable("test");
        assertNotNull(v);
        assertEquals("something", v.get());

        Variable v2 = bottom.getVariable("test");
        assertNotNull(v2);
        assertEquals("something", v2.get());
    }

    // scope visibility and value rules --------------------------------------------------------------------------------

    @Test
    public void visibility() throws Exception {

        //
        // Once a variable is declared in a scope, it becomes visible to all enclosed scopes (scopes that have this
        // scope as parent) but it is not visible to this scope's enclosing scopes.
        //

        Scope topMost = getScopeToTest();

        EncloseableScope intermediate = getScopeToTest();

        topMost.enclose(intermediate);

        EncloseableScope bottomMost = getScopeToTest();

        intermediate.enclose(bottomMost);

        Variable<String> v = intermediate.declare("test", "something");

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

        Scope upper = getScopeToTest();
        EncloseableScope intermediate = getScopeToTest();
        EncloseableScope lower = getScopeToTest();

        upper.enclose(intermediate);
        intermediate.enclose(lower);

        intermediate.declare("color", "blue");

        assertNull(upper.getVariable("color"));
        assertEquals("blue", intermediate.getVariable("color").get());
        assertEquals("blue", lower.getVariable("color").get());
    }

    @Test
    public void valueRelativeToScope() throws Exception {

        Scope upper = getScopeToTest();

        EncloseableScope inter = getScopeToTest();

        EncloseableScope lower = getScopeToTest();

        upper.enclose(inter);

        inter.enclose(lower);

        upper.declare("color", "red");
        inter.declare("color", "yellow");
        lower.declare("color", "blue");

        assertEquals("red", upper.getVariable("color").get());
        assertEquals("yellow", inter.getVariable("color").get());
        assertEquals("blue", lower.getVariable("color").get());

        lower.undeclare("color");

        assertEquals("red", upper.getVariable("color").get());
        assertEquals("yellow", inter.getVariable("color").get());
        assertEquals("yellow", lower.getVariable("color").get());

        inter.undeclare("color");

        assertEquals("red", upper.getVariable("color").get());
        assertEquals("red", inter.getVariable("color").get());
        assertEquals("red", lower.getVariable("color").get());
    }

    @Test
    public void valueRelativeToScope_set() throws Exception {

        Scope upper = getScopeToTest();

        EncloseableScope inter = getScopeToTest();

        EncloseableScope lower = getScopeToTest();

        upper.enclose(inter);

        inter.enclose(lower);

        upper.declare("color", "red");
        inter.declare("color", "yellow");
        lower.declare("color", "blue");

        assertEquals("red", upper.getVariable("color").get());
        assertEquals("yellow", inter.getVariable("color").get());
        assertEquals("blue", lower.getVariable("color").get());

        //noinspection unchecked
        upper.getVariable("color").set("dark red");

        assertEquals("dark red", upper.getVariable("color").get());
        assertEquals("yellow", inter.getVariable("color").get());
        assertEquals("blue", lower.getVariable("color").get());

        //noinspection unchecked
        inter.getVariable("color").set("dark yellow");

        assertEquals("dark red", upper.getVariable("color").get());
        assertEquals("dark yellow", inter.getVariable("color").get());
        assertEquals("blue", lower.getVariable("color").get());

        //noinspection unchecked
        lower.getVariable("color").set("dark blue");

        assertEquals("dark red", upper.getVariable("color").get());
        assertEquals("dark yellow", inter.getVariable("color").get());
        assertEquals("dark blue", lower.getVariable("color").get());

        upper.undeclare("color");

        assertNull(upper.getVariable("color"));
        assertEquals("dark yellow", inter.getVariable("color").get());
        assertEquals("dark blue", lower.getVariable("color").get());
    }

    // getVariablesDeclaredInScope() -----------------------------------------------------------------------------------

    @Test
    public void getVariablesDeclaredInScope_EnclosingScopesAreNotVisibleToThisMethod() throws Exception {

        EncloseableScope scope = getScopeToTest();

        Scope enclosing = getScopeToTest();

        enclosing.enclose(scope);

        enclosing.declare("color", "blue");

        assertTrue(scope.getVariablesDeclaredInScope().isEmpty());

        assertEquals("blue", scope.getVariable("color").get());

        List<Variable> declaredInScope = enclosing.getVariablesDeclaredInScope();

        assertEquals(1, declaredInScope.size());

        Variable v = declaredInScope.get(0);

        assertEquals("color", v.name());
        assertEquals("blue", v.get());
    }

    // undeclare() -----------------------------------------------------------------------------------------------------

    @Test
    public void undeclare_FromEnclosedScope() throws Exception {

        EncloseableScope enclosed = getScopeToTest();

        Scope enclosing = new ScopeImpl();

        enclosing.enclose(enclosed);

        //
        // declare a variable in the enclosing scope and attempt to undeclared it from the enclosed scope
        //

        enclosing.declare("A", "A value");

        Variable v = enclosed.getVariable("A");
        assertNotNull(v);
        assertEquals("A value", v.get());

        //
        // attempt to undeclare the variable from the enclosed scope
        //

        Variable v2 = enclosed.undeclare("A");

        //
        // nothing to undeclare
        //
        assertNull(v2);

        //
        // variable is still there
        //
        Variable v3 = enclosed.getVariable("A");
        assertNotNull(v3);
        assertEquals("A value", v3.get());
    }

    // legacy ----------------------------------------------------------------------------------------------------------

    @Test
    public void legacy2() throws Exception {

        EncloseableScope parent = getScopeToTest();

        assertNull(parent.getEnclosing());

        EncloseableScope childOne = getScopeToTest();

        assertNull(childOne.getEnclosing());

        EncloseableScope childTwo = getScopeToTest();

        assertNull(childTwo.getEnclosing());

        parent.enclose(childOne);
        assertEquals(parent, childOne.getEnclosing());

        parent.enclose(childTwo);
        assertEquals(parent, childTwo.getEnclosing());

        //
        // verify hierarchical resolution
        //

        parent.declare("variableA", "valueA");

        assertEquals("valueA", parent.getVariable("variableA").get());
        assertEquals("valueA", childOne.getVariable("variableA").get());
        assertEquals("valueA", childTwo.getVariable("variableA").get());

        //
        // local values supersede the values above them in the hierarchy, but only if the values were declared locally
        //

        //
        // 'variableA' was declared in parent, so changing the value in children changes its value in parent
        //
        //noinspection unchecked
        childOne.getVariable("variableA").set("valueB");

        assertEquals("valueB", parent.getVariable("variableA").get());
        assertEquals("valueB", childOne.getVariable("variableA").get());
        assertEquals("valueB", childTwo.getVariable("variableA").get());

        //
        // 'variableA' was declared in parent, so changing the value in children changes its value in parent
        //

        //noinspection unchecked
        childTwo.getVariable("variableA").set("valueC");

        assertEquals("valueC", parent.getVariable("variableA").get());
        assertEquals("valueC", childOne.getVariable("variableA").get());
        assertEquals("valueC", childTwo.getVariable("variableA").get());

        //
        // if I declare the same variable in an enclosed scope, the modifications do not propagate
        //

        childOne.declare("variableA", "valueD");

        assertEquals("valueC", parent.getVariable("variableA").get());
        assertEquals("valueD", childOne.getVariable("variableA").get());
        assertEquals("valueC", childTwo.getVariable("variableA").get());

        //
        // changing a scoped variable does not affect the value outside the scope and enclosed scopes
        //

        //noinspection unchecked
        childOne.getVariable("variableA").set("valueE");

        assertEquals("valueC", parent.getVariable("variableA").get());
        assertEquals("valueE", childOne.getVariable("variableA").get());
        assertEquals("valueC", childTwo.getVariable("variableA").get());

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected abstract EncloseableScope getScopeToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
