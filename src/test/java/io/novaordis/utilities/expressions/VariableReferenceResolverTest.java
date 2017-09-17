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
import static org.junit.Assert.assertFalse;
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

    // getVariableReferences() -----------------------------------------------------------------------------------------

    @Test
    public void getVariableReferences_NoVariableReferences() throws Exception {

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String s = "gobbledygook";

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertTrue(vrs.isEmpty());
    }

    @Test
    public void getVariableReferences_NoVariableReferences2() throws Exception {

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String s = "";

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertTrue(vrs.isEmpty());
    }

    @Test
    public void getVariableReferences() throws Exception {

        String s = "a${b}c";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(1, r.getStartIndex());
        assertEquals(4, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences2() throws Exception {

        String s = "a$b";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(1, r.getStartIndex());
        assertEquals(2, r.getEndIndex());
        assertFalse(r.hasBraces());
    }

    @Test
    public void getVariableReferences_IllegalReference() throws Exception {

        String s = "$b}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing '}'"));
        }
    }

    @Test
    public void getVariableReferences_IllegalReference2() throws Exception {

        String s = "$b}c";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced closing '}'"));
        }
    }

    @Test
    public void getVariableReferences3() throws Exception {

        String s = "gobble${dy}gook";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("dy", r.getName());
        assertEquals(6, r.getStartIndex());
        assertEquals(10, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences4() throws Exception {

        String s = "gobble${dy}go${ok}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(2, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("dy", r.getName());
        assertEquals(6, r.getStartIndex());
        assertEquals(10, r.getEndIndex());
        assertTrue(r.hasBraces());

        VariableReference r2 = vrs.get(1);

        assertEquals("ok", r2.getName());
        assertEquals(13, r2.getStartIndex());
        assertEquals(17, r2.getEndIndex());
        assertTrue(r2.hasBraces());

    }

    @Test
    public void getVariableReferences5() throws Exception {

        String s = "gobbledy${a}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("a", r.getName());
        assertEquals(8, r.getStartIndex());
        assertEquals(11, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences6() throws Exception {

        String s = "${a}dygook";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("a", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(3, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences7() throws Exception {

        String s = "${a}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("a", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(3, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences8() throws Exception {

        String s = "go${a}ok";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("a", r.getName());
        assertEquals(2, r.getStartIndex());
        assertEquals(5, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences9() throws Exception {

        String s = "${g}${o}${b}${b}${l}${e}${d}${y}${g}${o}${o}${k}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(12, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("g", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(3, r.getEndIndex());
        assertTrue(r.hasBraces());

        VariableReference r2 = vrs.get(1);

        assertEquals("o", r2.getName());
        assertEquals(4, r2.getStartIndex());
        assertEquals(7, r2.getEndIndex());
        assertTrue(r2.hasBraces());

        VariableReference r3 = vrs.get(2);

        assertEquals("b", r3.getName());
        assertEquals(8, r3.getStartIndex());
        assertEquals(11, r3.getEndIndex());
        assertTrue(r3.hasBraces());

        VariableReference r4 = vrs.get(3);

        assertEquals("b", r4.getName());
        assertEquals(12, r4.getStartIndex());
        assertEquals(15, r4.getEndIndex());
        assertTrue(r4.hasBraces());

        VariableReference r5 = vrs.get(4);

        assertEquals("l", r5.getName());
        assertEquals(16, r5.getStartIndex());
        assertEquals(19, r5.getEndIndex());
        assertTrue(r5.hasBraces());

        VariableReference r6 = vrs.get(5);

        assertEquals("e", r6.getName());
        assertEquals(20, r6.getStartIndex());
        assertEquals(23, r6.getEndIndex());
        assertTrue(r6.hasBraces());

        VariableReference r7 = vrs.get(6);

        assertEquals("d", r7.getName());
        assertEquals(24, r7.getStartIndex());
        assertEquals(27, r7.getEndIndex());
        assertTrue(r7.hasBraces());

        VariableReference r8 = vrs.get(7);

        assertEquals("y", r8.getName());
        assertEquals(28, r8.getStartIndex());
        assertEquals(31, r8.getEndIndex());
        assertTrue(r8.hasBraces());

        VariableReference r9 = vrs.get(8);

        assertEquals("g", r9.getName());
        assertEquals(32, r9.getStartIndex());
        assertEquals(35, r9.getEndIndex());
        assertTrue(r9.hasBraces());

        VariableReference r10 = vrs.get(9);

        assertEquals("o", r10.getName());
        assertEquals(36, r10.getStartIndex());
        assertEquals(39, r10.getEndIndex());
        assertTrue(r10.hasBraces());

        VariableReference r11 = vrs.get(10);

        assertEquals("o", r11.getName());
        assertEquals(40, r11.getStartIndex());
        assertEquals(43, r11.getEndIndex());
        assertTrue(r11.hasBraces());

        VariableReference r12 = vrs.get(11);

        assertEquals("k", r12.getName());
        assertEquals(44, r12.getStartIndex());
        assertEquals(47, r12.getEndIndex());
        assertTrue(r12.hasBraces());
    }

    @Test
    public void getVariableReferences10() throws Exception {

        String s = "${a}b${c}d${d}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(3, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("a", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(3, r.getEndIndex());
        assertTrue(r.hasBraces());

        VariableReference r2 = vrs.get(1);

        assertEquals("c", r2.getName());
        assertEquals(5, r2.getStartIndex());
        assertEquals(8, r2.getEndIndex());
        assertTrue(r2.hasBraces());

        VariableReference r3 = vrs.get(2);

        assertEquals("d", r3.getName());
        assertEquals(10, r3.getStartIndex());
        assertEquals(13, r3.getEndIndex());
        assertTrue(r3.hasBraces());

    }

    @Test
    public void getVariableReferences_IllegalReference3() throws Exception {

        String s = "${{something}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("misplaced '{' in variable reference"));
        }
    }

    @Test
    public void getVariableReferences_IllegalReference4() throws Exception {

        String s = "${some${thing}}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("misplaced '$' in variable reference"));
        }
    }

    @Test
    public void getVariableReferences_IllegalReference5() throws Exception {

        String s = "${s";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void getVariableReferences_null() throws Exception {

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null string"));
        }
    }

    @Test
    public void getVariableReferences_IllegalReference6() throws Exception {

        String s = "something $";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("empty variable reference"));
        }
    }

    @Test
    public void getVariableReferences_IllegalReference7() throws Exception {

        String s = "a ${b something";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing closing '}'"));
        }
    }

    @Test
    public void getVariableReferences_IllegalReference8() throws Exception {

        String s = "blah${something";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.getVariableReferences(s);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void getVariableReferences11() throws Exception {

        String s = "$b/something";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(1, r.getEndIndex());
        assertFalse(r.hasBraces());
    }

    @Test
    public void getVariableReferences12() throws Exception {

        String s = "$b:something";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(1, r.getEndIndex());
        assertFalse(r.hasBraces());
    }

    @Test
    public void getVariableReferences13() throws Exception {

        String s = "$b something";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(1, r.getEndIndex());
        assertFalse(r.hasBraces());
    }

    @Test
    public void getVariableReferences14() throws Exception {

        String s = "some$thing";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("thing", r.getName());
        assertEquals(4, r.getStartIndex());
        assertEquals(9, r.getEndIndex());
        assertFalse(r.hasBraces());
    }

    @Test
    public void getVariableReferences15() throws Exception {

        String s = "a ${b} c";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(2, r.getStartIndex());
        assertEquals(5, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences16() throws Exception {

        String s = "a ${b} c ${d} ";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(2, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("b", r.getName());
        assertEquals(2, r.getStartIndex());
        assertEquals(5, r.getEndIndex());
        assertTrue(r.hasBraces());

        VariableReference r2 = vrs.get(1);

        assertEquals("d", r2.getName());
        assertEquals(9, r2.getStartIndex());
        assertEquals(12, r2.getEndIndex());
        assertTrue(r2.hasBraces());
    }

    @Test
    public void getVariableReferences17() throws Exception {

        String s = "${something}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("something", r.getName());
        assertEquals(0, r.getStartIndex());
        assertEquals(11, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    @Test
    public void getVariableReferences18() throws Exception {

        String s = "blah${something}";

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        List<VariableReference> vrs = resolver.getVariableReferences(s);

        assertEquals(1, vrs.size());

        VariableReference r = vrs.get(0);

        assertEquals("something", r.getName());
        assertEquals(4, r.getStartIndex());
        assertEquals(15, r.getEndIndex());
        assertTrue(r.hasBraces());
    }

    // resolve() -------------------------------------------------------------------------------------------------------

    @Test
    public void resolve_NoVariableReferences() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        
        String result = resolver.resolve("gobbledygook", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_NoVariableReferences2() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("", scope);
        assertEquals("", result);
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
            assertTrue(msg.contains("unbalanced closing '}'"));
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
            assertTrue(msg.contains("unbalanced closing '}'"));
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
            assertTrue(msg.contains("unbalanced closing '}'"));
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
        scope.declare("b", "b");
        scope.declare("d", "d");
        scope.declare("e", "e");
        scope.declare("g", "g");
        scope.declare("k", "k");
        scope.declare("l", "l");
        scope.declare("o", "o");
        scope.declare("y", "y");
        String result = resolver.resolve("${g}${o}${b}${b}${l}${e}${d}${y}${g}${o}${o}${k}", scope);
        assertEquals("gobbledygook", result);
    }

    @Test
    public void resolve_MultipleVariableReplacement2() throws Exception {

        Scope scope = new ScopeImpl();

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("${a}b${c}d${d}", scope);

        assertEquals("${a}b${c}d${d}", result);

        scope.declare("a", "a");

        String result2 = resolver.resolve("${a}b${c}d${d}", scope);
        assertEquals("ab${c}d${d}", result2);

        scope.declare("c", "c");

        String result3 = resolver.resolve("${a}b${c}d${d}", scope);
        assertEquals("abcd${d}", result3);

        scope.declare("d", "d");

        String result4 = resolver.resolve("${a}b${c}d${d}", scope);
        assertEquals("abcdd", result4);
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

    @Test
    public void resolve_null() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve(null, scope);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null string"));
        }
    }

    @Test
    public void resolve_InvalidVariableReference_Edge1() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("something $", scope);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("empty variable reference"));
        }
    }

    @Test
    public void resolve_InvalidVariableReference_2() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("a ${b something", scope);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing closing '}'"));
        }
    }

    @Test
    public void resolve_InvalidVariableReference_UnbalancedBrackets() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        try {

            resolver.resolve("blah${something", scope);
            fail("should have thrown exception");
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void resolve_SimpleReference_NaturalSeparator() throws Exception {

        Scope scope = new ScopeImpl();
        scope.declare("b", "blah");
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("$b/something", scope);
        assertEquals("blah/something", result);
    }

    @Test
    public void resolve_SimpleReference_NaturalSeparator2() throws Exception {

        Scope scope = new ScopeImpl();
        scope.declare("b", "blah");
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("$b:something", scope);
        assertEquals("blah:something", result);
    }

    @Test
    public void resolve_SimpleReference_NaturalSeparator3() throws Exception {

        Scope scope = new ScopeImpl();
        scope.declare("b", "blah");
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("$b something", scope);
        assertEquals("blah something", result);
    }

    @Test
    public void resolve_2() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("some$thing", scope);
        assertEquals("some$thing", result);
    }

    @Test
    public void resolve_3() throws Exception {

        Scope scope = new ScopeImpl();
        scope.declare("thing", "body");
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("some$thing", scope);
        assertEquals("somebody", result);
    }

    @Test
    public void resolve_4() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("some$thing", scope);
        assertEquals("some$thing", result);
    }

    @Test
    public void resolve_5() throws Exception {

        Scope scope = new ScopeImpl();
        scope.declare("thing", "body");
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("some$thing", scope);
        assertEquals("somebody", result);
    }

    @Test
    public void resolve_6() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("a ${b} c", scope);
        assertEquals("a ${b} c", result);
    }

    @Test
    public void resolve_7() throws Exception {

        Scope scope = new ScopeImpl();
        scope.declare("b", "b");
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("a ${b} c", scope);
        assertEquals("a b c", result);
    }

    @Test
    public void resolve_8() throws Exception {

        Scope scope = new ScopeImpl();
        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();
        String result = resolver.resolve("a ${b} c ${d} ", scope);
        assertEquals("a ${b} c ${d} ", result);
    }

    @Test
    public void resolve_9() throws Exception {

        Scope scope = new ScopeImpl();

        scope.declare("b", "b");

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("a ${b} c ${d} ", scope);
        assertEquals("a b c ${d} ", result);

        scope.declare("d", "d");

        String result2 = resolver.resolve("a ${b} c ${d} ", scope);
        assertEquals("a b c d ", result2);
    }

    @Test
    public void resolve_OnlyVariable() throws Exception {

        Scope scope = new ScopeImpl();

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("${something}", scope);

        assertEquals("${something}", result);
    }

    @Test
    public void resolve_OnlyVariable_2() throws Exception {

        Scope scope = new ScopeImpl();

        scope.declare("something", "else");

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("${something}", scope);

        assertEquals("else", result);
    }

    @Test
    public void resolve_VariableAndConstant() throws Exception {

        Scope scope = new ScopeImpl();

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("blah${something}", scope);

        assertEquals("blah${something}", result);
    }

    @Test
    public void resolve_VariableAndConstant_2() throws Exception {

        Scope scope = new ScopeImpl();

        scope.declare("something", "s");

        VariableReferenceResolver resolver = getVariableReferenceResolverToTest();

        String result = resolver.resolve("blah${something}", scope);

        assertEquals("blahs", result);
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

    // variableReferenceResolver() -------------------------------------------------------------------------------------

    @Test
    public void variableReferenceResolver_Null() throws Exception  {

        VariableReferenceResolver r = getVariableReferenceResolverToTest();

        try {

            r.getVariableReferences(null);
            fail("should have thrown exceptions");

        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null string"));
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------
    
    protected VariableReferenceResolver getVariableReferenceResolverToTest() throws Exception {
        
        return new VariableReferenceResolver();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
