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

package io.novaordis.utilities.env;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 12/4/16
 */
public interface EnvironmentVariableProvider {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Gets the value of the specified environment variable. An environment variable is a system-dependent external
     * named value. On UNIX systems the alphabetic case of name is typically significant, while on Microsoft Windows
     * systems it is typically not.
     *
     * @param  name the name of the environment variable
     *
     * @return the string value of the variable, or null if the variable is not defined in the system environment.
     *
     * @exception NullPointerException if name is null.
     */
    String getenv(String name);

    // Public ----------------------------------------------------------------------------------------------------------

}
