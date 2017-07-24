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
import java.util.Collections;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/23/17
 */
public class MockLoggingConfiguration implements LoggingConfiguration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private File file;
    private List<LoggerConfiguration> lcs;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockLoggingConfiguration() {

        this.lcs = Collections.emptyList();
    }

    // LoggingConfiguration implementation -----------------------------------------------------------------------------

    @Override
    public List<LoggerConfiguration> getLoggerConfiguration() {

        return lcs;
    }

    @Override
    public File getFile() {

        return file;
    }

    // Public ----------------------------------------------------------------------------------------------------------
    
    public void setFile(File file) {

        this.file = file;
    }

    public void setLoggerConfiguration(List<LoggerConfiguration> lcs) {

        this.lcs = lcs;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
