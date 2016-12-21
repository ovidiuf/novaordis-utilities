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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/22/16
 */
public class VariableTest extends TokenTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(VariableTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Overrides -------------------------------------------------------------------------------------------------------

    @Test
    public void resolve_ConfiguredToFail_NullProvider() throws Exception {

        Variable v = getTokenToTest();

        v.setFailOnMissingDefinition(true);

        try {
            v.resolve((VariableProvider) null);
            fail("should throw exception");
        }
        catch(VariableNotDefinedException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("\"" + v.getName() + "\" not defined, missing provider", msg);
            assertEquals(v.getName(), e.getVariableName());
        }
    }

    @Test
    public void resolve_ConfiguredToFail_NullMap() throws Exception {

        Variable v = getTokenToTest();

        v.setFailOnMissingDefinition(true);

        try {
            //noinspection unchecked
            v.resolve((Map) null);
            fail("should throw exception");
        }
        catch(VariableNotDefinedException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("\"" + v.getName() + "\" not defined, missing map", msg);
            assertEquals(v.getName(), e.getVariableName());
        }
    }

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        Variable v = new Variable("test");
        assertEquals("test", v.getName());
        assertFalse(v.isFailOnMissingDefinition());
    }

    @Test
    public void constructor_FailOnMissingVariable() throws Exception {

        Variable v = new Variable("test", true);
        assertEquals("test", v.getName());
        assertTrue(v.isFailOnMissingDefinition());
    }

    // resolve() -------------------------------------------------------------------------------------------------------

    @Test
    public void resolve_NullProvider() throws Exception {

        Variable v = new Variable("something");

        String s = v.resolve((VariableProvider)null);

        assertEquals("${something}", s);
    }

    @Test
    public void resolve() throws Exception {

        Variable v = new Variable("something");

        VariableProviderImpl p = new VariableProviderImpl();

        String s = v.resolve(p);

        assertEquals("${something}", s);

        p.setVariableValue("something", "blah");

        assertEquals("blah", v.resolve(p));
    }

    @Test
    public void resolve_Map_KeyExists() throws Exception {

        Variable v = new Variable("something");

        Map<String, String> map = new HashMap<>();
        map.put("something", "blah");

        String s = v.resolve(map);
        assertEquals("blah", s);
    }

    @Test
    public void resolve_Map_KeyDoesNotExist() throws Exception {

        Variable v = new Variable("something");

        Map<String, String> map = new HashMap<>();
        map.put("somethingelse", "blah");

        String s = v.resolve(map);
        assertEquals("${something}", s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected Variable getTokenToTest() throws Exception {

        return new Variable("test");
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
