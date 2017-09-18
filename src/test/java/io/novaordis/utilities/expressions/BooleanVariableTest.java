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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public class BooleanVariableTest extends VariableTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public void declarationWithAssignment() throws Exception {

        Scope s = new ScopeBase();

        Variable<Boolean> v = s.declare("something", true);

        assertTrue(v instanceof BooleanVariable);

        assertEquals("something", v.name());
        assertEquals(Boolean.class, v.type());
        assertEquals(true, v.get());

    }

    @Override
    public void declarationWithoutAssignment() throws Exception {

        Scope s = new ScopeBase();

        Variable<Boolean> v = s.declare("something", Boolean.class);

        assertTrue(v instanceof BooleanVariable);

        assertEquals("something", v.name());
        assertEquals(Boolean.class, v.type());
        assertNull(v.get());
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected <T> Variable<T> getVariableToTest(String name, T optionalValue) throws Exception {

        if (optionalValue != null && !(optionalValue instanceof Boolean)) {

            throw new IllegalArgumentException("optional value must be null or Boolean");
        }

        Scope defaultScope = new ScopeBase();

        return defaultScope.declare(name, optionalValue);
    }

    @Override
    protected Object getValueToTest() {

        return true;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
