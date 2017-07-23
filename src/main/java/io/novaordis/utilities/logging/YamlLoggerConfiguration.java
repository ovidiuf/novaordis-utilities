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

import java.util.Map;

/**
 * The configuration associated with a log4j Logger.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class YamlLoggerConfiguration implements LoggerConfiguration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;
    private Log4jLevel level;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param o - the "loggers" list element returned by the Yaml parser. We expect it to be a Map<String, String>
     *          with one element, where the key is the logger definition and the value is the logging level.
     */
    public YamlLoggerConfiguration(Object o) throws LoggingConfigurationException {

        if (o == null) {

            throw new IllegalArgumentException("null logger configuration");
        }

        parse(o);
    }

    // LoggerConfiguration implementation ------------------------------------------------------------------------------

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Log4jLevel getLevel() {

        return level;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse(Object o) throws LoggingConfigurationException {

        if (!(o instanceof Map)) {

            throw new LoggingConfigurationException("expecting a Map but got a " + o.getClass().getSimpleName());
        }

        Map m = (Map)o;

        if (m.size() != 1) {

            throw new LoggingConfigurationException(
                    "expecting a single element Map but got a Map with " + m.size() + " elements");
        }

        Object oKey = m.keySet().iterator().next();

        if (!(oKey instanceof String)) {

            throw new LoggingConfigurationException(
                    "the key of the single element Map should be a String but is a " + oKey.getClass().getSimpleName());
        }

        this.name = (String) oKey;

        Object value = m.get(name);

        if (!(value instanceof String)) {

            throw new LoggingConfigurationException(
                    "the value associated with logger '" + name + "' is not a String: '" + value + "'");
        }

        this.level = Log4jLevel.fromLiteral((String)value);

        if (this.level == null) {

            throw new LoggingConfigurationException("not a valid logging level: " + value);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
