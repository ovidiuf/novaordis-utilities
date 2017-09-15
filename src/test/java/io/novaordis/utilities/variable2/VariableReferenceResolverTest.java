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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/15/17
 */
public class VariableReferenceResolverTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // resolve() -------------------------------------------------------------------------------------------------------

    @Test
    public void resolve_NoVariables() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        
        String result = resolver.resolve("gobbledygook", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_VariableNameParsing_CompleteDeclaration_VariableDefinedAndNonNull() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("b", String.class, "x");

        String result = resolver.resolve("a${b}c", scope);

        assertEquals("axc", result);
    }

    @Test
    public void resolve_VariableNameParsing_CompleteDeclaration_VariableDefinedButNull() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("b", String.class);

        String result = resolver.resolve("a${b}c", scope);

        assertEquals("ac", result);
    }

    @Test
    public void resolve_VariableNameParsing_CompleteDeclaration_VariableNotDefined() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        assertNull(scope.getVariable("b"));

        String result = resolver.resolve("a${b}c", scope);

        assertEquals("a${b}c", result);
    }

    @Test
    public void resolve_VariableNameParsing_SimpleDeclaration_VariableDefinedAndNonNull() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("b", String.class, "x");

        String result = resolver.resolve("a$b", scope);

        assertEquals("ax", result);
    }

    @Test
    public void resolve_VariableNameParsing_SimpleDeclaration_VariableDefinedButNull() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("b", String.class);

        String result = resolver.resolve("a$b", scope);

        assertEquals("a", result);
    }

    @Test
    public void resolve_VariableNameParsing_SimpleDeclaration_VariableNotDefined() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        assertNull(scope.getVariable("b"));

        String result = resolver.resolve("a$b", scope);

        assertEquals("a$b", result);
    }

    @Test
    public void resolve_VariableNameParsing_UnbalancedClosingBrace() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("$b}", scope);
            fail("Should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing }"));
        }
    }

    @Test
    public void resolve_VariableNameParsing_UnbalancedClosingBrace2() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("$b}c", scope);
            fail("Should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing }"));
        }
    }

    @Test
    public void resolve_VariableNameParsing_UnbalancedClosingBrace3() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("$b} ", scope);
            fail("Should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing }"));
        }
    }

    @Test
    public void resolve_VariableCannotBeResolved() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("gobble${dy}gook", scope);
        assertEquals("gobble${dy}gook", result);
    }

    @Test
    public void resolve_VariableCannotBeResolved_TwoVariables() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("gobble${dy}go${ok}", scope);
        assertEquals("gobble${dy}go${ok}", result);
    }

    @Test
    public void resolve_SimpleVariableReplacement() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("a", String.class, "gook");
        String result = resolver.resolve("gobbledy${a}", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_SimpleVariableReplacement2() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("a", String.class, "gobble");
        String result = resolver.resolve("${a}dygook", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_SimpleVariableReplacement3() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("a", String.class, "gobbledygook");
        String result = resolver.resolve("${a}", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_SimpleVariableReplacement4() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("a", String.class, "bbledygo");
        String result = resolver.resolve("go${a}ok", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_MultipleVariableReplacement() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        scope.declare("b", String.class, "b");
        scope.declare("d", String.class, "d");
        scope.declare("e", String.class, "e");
        scope.declare("g", String.class, "g");
        scope.declare("k", String.class, "k");
        scope.declare("l", String.class, "l");
        scope.declare("o", String.class, "o");
        scope.declare("y", String.class, "y");
        String result = resolver.resolve("${g}${o}${b}${b}${l}${e}${d}${y}${g}${o}${o}${k}", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_InvalidVariableReference() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("${{something}", scope);
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
    public void resolve_InvalidVariableReference2() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("${some${thing}}", scope);
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
    public void resolve_InvalidVariableReference3() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("${s", scope);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced"));
            assertTrue(msg.contains("{"));
            assertTrue(msg.contains("in variable reference"));
        }
    }

    // resolveVariable() -----------------------------------------------------------------------------------------------

    @Test
    public void resolveVariable_NullScope() throws Exception {

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {
            
            resolver.resolveVariable("something", null, true);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {
            
            String msg = e.getMessage();
            assertTrue(msg.contains("null scope"));
        }
    }

    @Test
    public void resolveVariable_VariableDoesNotExistInScope_UseBraces() throws Exception {

        ScopeImpl s = new ScopeImpl();

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolveVariable("no-such-var", s, true);

        assertEquals("${no-such-var}", result);
    }

    @Test
    public void resolveVariable_VariableDoesNotExistInScope_DoNotUseBraces() throws Exception {

        ScopeImpl s = new ScopeImpl();

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolveVariable("no-such-var", s, false);

        assertEquals("$no-such-var", result);
    }

    @Test
    public void resolveVariable_VariableExistsButItHasNullValue() throws Exception {

        ScopeImpl s = new ScopeImpl();
        s.declare("a", String.class);
        assertNull(s.getVariable("a").get());

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();        

        String result = resolver.resolveVariable("a", s, true);

        assertEquals("", result);

        String result2 = resolver.resolveVariable("a", s, false);

        assertEquals("", result2);
    }

    @Test
    public void resolveVariable_VariableExistsAndItHasNullNonValue() throws Exception {


        ScopeImpl s = new ScopeImpl();
        s.declare("a", String.class, "b");

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        
        String result = resolver.resolveVariable("a", s, true);

        assertEquals("b", result);

        String result2 = resolver.resolveVariable("a", s, false);

        assertEquals("b", result2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------
    
    protected VariableReferenceResolver getVariableReferenceResolverToTest() throws Exception {
        
        return new VariableReferenceResolver();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
