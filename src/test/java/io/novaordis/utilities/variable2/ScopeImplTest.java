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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public class ScopeImplTest extends ScopeTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // toStringValue() -------------------------------------------------------------------------------------------------

    @Test
    public void toStringValue_VariableDoesNotExistInScope_UseBraces() throws Exception {

        ScopeImpl s = new ScopeImpl();

        String result = s.toStringValue("no-such-var", true);

        assertEquals("${no-such-var}", result);
    }

    @Test
    public void toStringValue_VariableDoesNotExistInScope_DoNotUseBraces() throws Exception {

        ScopeImpl s = new ScopeImpl();

        String result = s.toStringValue("no-such-var", false);

        assertEquals("$no-such-var", result);
    }

    @Test
    public void toStringValue_VariableExistsButItHasNullValue() throws Exception {

        ScopeImpl s = new ScopeImpl();
        s.declare("a", String.class);
        assertNull(s.getVariable("a").get());

        String result = s.toStringValue("a", true);

        assertEquals("", result);

        String result2 = s.toStringValue("a", false);

        assertEquals("", result2);
    }

    @Test
    public void toStringValue_VariableExistsAndItHasNullNonValue() throws Exception {


        ScopeImpl s = new ScopeImpl();
        s.declare("a", String.class, "b");

        String result = s.toStringValue("a", true);

        assertEquals("b", result);

        String result2 = s.toStringValue("a", false);

        assertEquals("b", result2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected Scope getScopeToTest() throws Exception {

        return new ScopeImpl();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
