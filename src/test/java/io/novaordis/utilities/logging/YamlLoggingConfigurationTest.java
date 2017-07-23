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
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class YamlLoggingConfigurationTest extends LoggingConfigurationTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_NullMap() throws Exception {

        try {

            new YamlLoggingConfiguration(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null logging configuration map"));
        }
    }

    @Test
    public void constructor_MissingTopLevelLoggingKey() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("something", "something else");

        try {

            new YamlLoggingConfiguration(map);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            assertEquals("missing top level '" + YamlLoggingConfiguration.LOGGING_KEY + "' key", msg);
        }
    }

    @Test
    public void constructor_Reference() throws Exception {

        File reference = new File(System.getProperty("basedir"),
                "src/test/resources/data/logging/reference-yaml-configuration-fragment.yaml");
        assertTrue(reference.isFile());
        FileInputStream fis = new FileInputStream(reference);
        Yaml yaml = new Yaml();
        Map m = (Map)yaml.load(fis);
        fis.close();

        YamlLoggingConfiguration c = new YamlLoggingConfiguration(m);

        File file = c.getFile();
        assertEquals(new File("/tmp/some-file.log"), file);

        List<LoggerConfiguration> lcs = c.getLoggerConfiguration();

        assertEquals(2, lcs.size());

        LoggerConfiguration lc = lcs.get(0);
        assertEquals("com.example", lc.getName());
        assertEquals(Log4jLevel.INFO, lc.getLevel());

        LoggerConfiguration lc2 = lcs.get(1);
        assertEquals("org.example2", lc2.getName());
        assertEquals(Log4jLevel.TRACE, lc2.getLevel());
    }

    @Test
    public void constructor_Embedded() throws Exception {

        File reference = new File(System.getProperty("basedir"),
                "src/test/resources/data/logging/yaml-configuration-fragment-2.yaml");
        assertTrue(reference.isFile());
        FileInputStream fis = new FileInputStream(reference);
        Yaml yaml = new Yaml();
        Map m = (Map)yaml.load(fis);
        fis.close();

        Map m2 = (Map)m.get("superstructure");

        YamlLoggingConfiguration c = new YamlLoggingConfiguration(m2);

        File file = c.getFile();
        assertEquals(new File("/tmp/some-file.log"), file);

        List<LoggerConfiguration> lcs = c.getLoggerConfiguration();

        assertEquals(2, lcs.size());

        LoggerConfiguration lc = lcs.get(0);
        assertEquals("com.example", lc.getName());
        assertEquals(Log4jLevel.INFO, lc.getLevel());

        LoggerConfiguration lc2 = lcs.get(1);
        assertEquals("org.example2", lc2.getName());
        assertEquals(Log4jLevel.TRACE, lc2.getLevel());
    }

    @Test
    public void constructor_LoggersDoesNotContainAList() throws Exception {

        File reference = new File(System.getProperty("basedir"),
                "src/test/resources/data/logging/yaml-configuration-fragment-3.yaml");
        assertTrue(reference.isFile());
        FileInputStream fis = new FileInputStream(reference);
        Yaml yaml = new Yaml();
        Map m = (Map)yaml.load(fis);
        fis.close();

        try {

            new YamlLoggingConfiguration(m);
            fail("should have thrown exception");
        }
        catch(LoggingConfigurationException e) {

            String msg = e.getMessage();
            String expected =
                    "'" + YamlLoggingConfiguration.LOGGERS_KEY + "' must contain a List, but it contains a String";
            assertEquals(expected, msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected YamlLoggingConfiguration getLoggingConfigurationToTest() throws Exception {

        File reference = new File(System.getProperty("basedir"),
                "src/test/resources/data/logging/reference-yaml-configuration-fragment.yaml");

        assertTrue(reference.isFile());

        FileInputStream fis = new FileInputStream(reference);

        Yaml yaml = new Yaml();

        Map m = (Map)yaml.load(fis);

        fis.close();

        return new YamlLoggingConfiguration(m);
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
