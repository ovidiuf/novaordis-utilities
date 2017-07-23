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

package io.novaordis.utilities.logging;

import io.novaordis.utilities.logging.log4j.Log4jLevel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class YamlLoggerConfigurationTest extends LoggerConfigurationTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructors ----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_Null() throws Exception {

        try {
            new YamlLoggerConfiguration(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null"));
        }
    }

    @Test
    public void constructor_ArgumentNotAMap() throws Exception {

        try {
            new YamlLoggerConfiguration("something");
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("expecting a Map but got a String"));
        }
    }

    @Test
    public void constructor_ArgumentIsZeroElementMap() throws Exception {

        Map m = new HashMap<>();

        try {
            new YamlLoggerConfiguration(m);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("expecting a single element Map but got a Map with 0 elements"));
        }
    }

    @Test
    public void constructor_ArgumentIsTwoElementMap() throws Exception {

        Map<String, String> m = new HashMap<>();
        m.put("k1", "v1");
        m.put("k2", "v2");

        try {
            new YamlLoggerConfiguration(m);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("expecting a single element Map but got a Map with 2 elements"));
        }
    }

    @Test
    public void constructor_MapKeyNotAString() throws Exception {

        Map m = new HashMap<>();
        //noinspection unchecked
        m.put(1, "v1");

        try {

            new YamlLoggerConfiguration(m);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertTrue(msg.startsWith("the key of the single element Map should be a String but is a"));
        }
    }

    @Test
    public void constructor_MapValueNotAString() throws Exception {

        Map<String, Object> m = new HashMap<>();
        m.put("com.example", 1L);

        try {

            new YamlLoggerConfiguration(m);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertEquals("the value associated with logger 'com.example' is not a String: '1'", msg);
        }
    }

    @Test
    public void constructor_MapValueNotALoggingLevel() throws Exception {

        Map<String, String> m = new HashMap<>();
        m.put("com.example", "something");

        try {

            new YamlLoggerConfiguration(m);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertTrue(msg.startsWith("not a valid logging level: something"));
        }
    }

    @Test
    public void constructor() throws Exception {

        Map<String, String> m = new HashMap<>();
        m.put("com.example", "DEBUG");

        YamlLoggerConfiguration c = new YamlLoggerConfiguration(m);

        assertEquals("com.example", c.getName());
        assertEquals(Log4jLevel.DEBUG, c.getLevel());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected YamlLoggerConfiguration getLoggerConfigurationToTest() throws Exception {

        Map<String, String> m = new HashMap<>();
        m.put("mock.logger.name", "INFO");
        return new YamlLoggerConfiguration(m);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
