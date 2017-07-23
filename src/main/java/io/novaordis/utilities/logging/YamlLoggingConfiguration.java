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

    // Constructors ----------------------------------------------------------------------------------------------------


    /**
     * @param loggingConfigurationMap the map extracted from the larger YAML configuration tree, which contains the
     *                                LOGGING_KEY and the rest of the logging configuration.
     *
     * @throws LoggingConfigurationException
     */
    public YamlLoggingConfiguration(Map loggingConfigurationMap) throws LoggingConfigurationException {

        if (loggingConfigurationMap == null) {

            throw new IllegalArgumentException("null logging configuration map");
        }

        Object o = loggingConfigurationMap.get(LOGGING_KEY);

        if (o == null) {

            throw new LoggingConfigurationException("missing top level '" + LOGGING_KEY + "' key");
        }
    }

    // LoggingConfiguration implementation -----------------------------------------------------------------------------

    @Override
    public List<LoggerConfiguration> getLoggerConfiguration() {

        throw new RuntimeException("getConfiguration() NOT YET IMPLEMENTED");
    }

    @Override
    public File getFile() {

        throw new RuntimeException("getFile() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
