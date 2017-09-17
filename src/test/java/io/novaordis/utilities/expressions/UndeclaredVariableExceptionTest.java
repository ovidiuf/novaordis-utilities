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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/17/17
 */
public class UndeclaredVariableExceptionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullUndeclaredVariableName() throws Exception {

        try {

            //noinspection ThrowableInstanceNeverThrown
            new UndeclaredVariableException(null, "something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertEquals("null undeclared variable name", msg);
        }
    }

    @Test
    public void constructor_NullUndeclaredVariableName_NoMessage() throws Exception {

        UndeclaredVariableException e = new UndeclaredVariableException("a", null);

        assertEquals("a", e.getUndeclaredVariableName());
        assertNull(e.getMessage());
    }

    @Test
    public void constructor_NullUndeclaredVariableName_oMessage() throws Exception {

        UndeclaredVariableException e =  new UndeclaredVariableException("a", "b");

        assertEquals("a", e.getUndeclaredVariableName());
        assertEquals("b", e.getMessage());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
