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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Contains all the knowledge to turn a YAML configuration fragment into a LoggingConfiguration instance, which can be
 * used as a delegate, part of a larger configuration instance.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class YamlLoggingConfiguration implements LoggingConfiguration {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String LOGGING_KEY = "logging";

    public static final String FILE_KEY = "file";

    public static final String LOGGERS_KEY = "loggers";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private File file;
    private List<LoggerConfiguration> loggerConfigurations;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param loggingConfigurationMap the value corresponding with the LOGGING_KEY in the larger YAML configuration
     *                                tree. It is a map.
     *
     * @throws LoggingConfigurationException
     */
    public YamlLoggingConfiguration(Map loggingConfigurationMap) throws LoggingConfigurationException {

        if (loggingConfigurationMap == null) {

            throw new IllegalArgumentException("null logging configuration map");
        }

        this.loggerConfigurations = new ArrayList<>();

        parse(loggingConfigurationMap);
    }

    // LoggingConfiguration implementation -----------------------------------------------------------------------------

    @Override
    public List<LoggerConfiguration> getLoggerConfiguration() {

        return loggerConfigurations;
    }

    @Override
    public File getFile() {

        return file;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse(Map loggingConfigurationMap) throws LoggingConfigurationException {

        Object o = loggingConfigurationMap.get(FILE_KEY);

        if (o != null) {

            if (!(o instanceof String)) {

                throw new LoggingConfigurationException(
                        "'" + FILE_KEY + "' must contain a String, but it contains a " + o.getClass().getSimpleName());
            }

            file = new File((String)o);
        }

        o = loggingConfigurationMap.get(LOGGERS_KEY);

        if (o != null) {

            if (!(o instanceof List)) {

                throw new LoggingConfigurationException(
                        "'" + LOGGERS_KEY + "' must contain a List, but it contains a " + o.getClass().getSimpleName());
            }

            List l = (List)o;

            for(Object le: l) {

                LoggerConfiguration c = new YamlLoggerConfiguration(le);
                loggerConfigurations.add(c);
            }
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
