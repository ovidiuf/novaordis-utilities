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
 * See https://kb.novaordis.com/index.php/Nova_Ordis_Utilities_Environment_Variable_Support
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 12/4/16
 */
public interface EnvironmentVariableProvider {

    // Constants -------------------------------------------------------------------------------------------------------

    String ENVIRONMENT_VARIABLE_PROVIDER_CLASS_NAME_SYSTEM_PROPERTY = "env.variable.provider.class.name";

    // Static ----------------------------------------------------------------------------------------------------------

    EnvironmentVariableProvider[] INSTANCE = new EnvironmentVariableProvider[1];

    /**
     * @return the default EnvironmentVariableProvider for this JVM. If "env.variable.provider.class.name" (the exact
     * string is defined by ENVIRONMENT_VARIABLE_PROVIDER_CLASS_NAME_SYSTEM_PROPERTY) is defined and points to a fully
     * qualified class name that implements EnvironmentVariableProvider, it will be instantiated, cached and used.
     * If the system property is not defined, SystemEnvironmentVariableProvider will be used.
     *
     * @exception IllegalStateException if failure to instantiate the custom class is encountered.
     */
    static EnvironmentVariableProvider getInstance() {

        synchronized (EnvironmentVariableProvider.class) {

            if (INSTANCE[0] != null) {
                return INSTANCE[0];
            }

            String className = System.getProperty(ENVIRONMENT_VARIABLE_PROVIDER_CLASS_NAME_SYSTEM_PROPERTY);

            if (className == null) {

                INSTANCE[0] = new SystemEnvironmentVariableProvider();
                return INSTANCE[0];
            }

            try {

                Class c = Class.forName(className);
                EnvironmentVariableProvider instance = (EnvironmentVariableProvider)c.newInstance();
                INSTANCE[0] = instance;
                return INSTANCE[0];
             }
            catch(Exception e) {

                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * Clears the cached EnvironmentVariableProvider instance.
     */
    static void reset() {

        synchronized (EnvironmentVariableProvider.class) {

            INSTANCE[0] = null;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

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


}
