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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public class StringVariableTest extends VariableTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public void declarationWithAssignment() throws Exception {

        Scope s = new ScopeImpl();

        Variable<String> v = s.declare("something", String.class, "blah");

        assertEquals("something", v.name());
        assertEquals(String.class, v.type());
        assertEquals("blah", v.get());

    }

    @Override
    public void declarationWithoutAssignment() throws Exception {

        Scope s = new ScopeImpl();

        Variable<String> v = s.declare("something", String.class);

        assertEquals("something", v.name());
        assertEquals(String.class, v.type());
        assertNull(v.get());
    }

    @Override
    public void copy() throws Exception {

        Scope s = new ScopeImpl();

        Variable<String> v = s.declare("something", String.class, "blah");

        StringVariable sv = (StringVariable)v;

        StringVariable sv2 = sv.copy();

        assertFalse(sv.equals(sv2));
        assertFalse(sv2.equals(sv));

        assertEquals("something", sv2.name());
        assertEquals("blah", sv2.get());
        assertEquals(String.class, sv2.type());
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected <T> Variable<T> getVariableToTest(String name, Class<? extends T> type, T optionalValue)
            throws Exception {

        Scope defaultScope = new ScopeImpl();

        //noinspection UnnecessaryLocalVariable
        Variable v = defaultScope.declare(name, String.class, (String)optionalValue);

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
