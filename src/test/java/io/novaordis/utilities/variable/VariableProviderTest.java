/*
 * Copyright (c) 2016 Nova Ordis LLC
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

package io.novaordis.utilities.variable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/22/16
 */
public abstract class VariableProviderTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // construction ----------------------------------------------------------------------------------------------------

    @Test
    public void construction() throws Exception {

        VariableProvider p = getVariableProviderToTest();

        assertNull(p.getVariableProviderParent());
        assertNull(p.getVariableValue("something"));
    }

    // setVariable -----------------------------------------------------------------------------------------------------

    @Test
    public void setVariable() throws Exception {

        VariableProvider p = getVariableProviderToTest();

        assertNull(p.getVariableValue("something"));

        assertNull(p.setVariableValue("something", "A"));

        assertEquals("A", p.getVariableValue("something"));

        assertEquals("A", p.setVariableValue("something", "B"));

        assertEquals("B", p.getVariableValue("something"));

        assertEquals("B", p.setVariableValue("something", null));

        assertNull(p.getVariableValue("something"));

        assertNull(p.setVariableValue("something", "C"));
    }

    // hierarchy -------------------------------------------------------------------------------------------------------

    @Test
    public void hierarchy() throws Exception {

        VariableProvider parent = getVariableProviderToTest();

        assertNull(parent.getVariableProviderParent());

        VariableProvider childOne = getVariableProviderToTest();

        assertNull(childOne.getVariableProviderParent());

        VariableProvider childTwo = getVariableProviderToTest();

        assertNull(childTwo.getVariableProviderParent());


        childOne.setVariableProviderParent(parent);
        assertEquals(parent, childOne.getVariableProviderParent());

        childTwo.setVariableProviderParent(parent);
        assertEquals(parent, childTwo.getVariableProviderParent());


        //
        // verify hierarchical resolution
        //

        parent.setVariableValue("variableA", "valueA");

        assertEquals("valueA", parent.getVariableValue("variableA"));
        assertEquals("valueA", childOne.getVariableValue("variableA"));
        assertEquals("valueA", childTwo.getVariableValue("variableA"));

        //
        // local values supersede the values above them in the hierarchy
        //

        childOne.setVariableValue("variableA", "valueB");

        assertEquals("valueA", parent.getVariableValue("variableA"));
        assertEquals("valueB", childOne.getVariableValue("variableA"));
        assertEquals("valueA", childTwo.getVariableValue("variableA"));

        childTwo.setVariableValue("variableA", "valueC");

        assertEquals("valueA", parent.getVariableValue("variableA"));
        assertEquals("valueB", childOne.getVariableValue("variableA"));
        assertEquals("valueC", childTwo.getVariableValue("variableA"));
    }


    // Package protected -----------------------------------------------------------------------------------------------

    protected abstract VariableProvider getVariableProviderToTest() throws Exception;

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
