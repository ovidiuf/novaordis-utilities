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

        assertNull(p.getParent());
        assertNull(p.getValue("something"));
    }

    // setVariable -----------------------------------------------------------------------------------------------------

    @Test
    public void setVariable() throws Exception {

        VariableProvider p = getVariableProviderToTest();

        assertNull(p.getValue("something"));

        assertNull(p.setValue("something", "A"));

        assertEquals("A", p.getValue("something"));

        assertEquals("A", p.setValue("something", "B"));

        assertEquals("B", p.getValue("something"));

        assertEquals("B", p.setValue("something", null));

        assertNull(p.getValue("something"));

        assertNull(p.setValue("something", "C"));
    }

    // hierarchy -------------------------------------------------------------------------------------------------------

    @Test
    public void hierarchy() throws Exception {

        VariableProvider parent = getVariableProviderToTest();

        assertNull(parent.getParent());

        VariableProvider childOne = getVariableProviderToTest();

        assertNull(childOne.getParent());

        VariableProvider childTwo = getVariableProviderToTest();

        assertNull(childTwo.getParent());


        childOne.setParent(parent);
        assertEquals(parent, childOne.getParent());

        childTwo.setParent(parent);
        assertEquals(parent, childTwo.getParent());


        //
        // verify hierarchical resolution
        //

        parent.setValue("variableA", "valueA");

        assertEquals("valueA", parent.getValue("variableA"));
        assertEquals("valueA", childOne.getValue("variableA"));
        assertEquals("valueA", childTwo.getValue("variableA"));

        //
        // local values supersede the values above them in the hierarchy
        //

        childOne.setValue("variableA", "valueB");

        assertEquals("valueA", parent.getValue("variableA"));
        assertEquals("valueB", childOne.getValue("variableA"));
        assertEquals("valueA", childTwo.getValue("variableA"));

        childTwo.setValue("variableA", "valueC");

        assertEquals("valueA", parent.getValue("variableA"));
        assertEquals("valueB", childOne.getValue("variableA"));
        assertEquals("valueC", childTwo.getValue("variableA"));
    }


    // Package protected -----------------------------------------------------------------------------------------------

    protected abstract VariableProvider getVariableProviderToTest() throws Exception;

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
