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

package io.novaordis.utilities.expressions;

import io.novaordis.utilities.NotSupportedException;
import io.novaordis.utilities.env.EnvironmentVariableProvider;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/17/17
 */
public class EnvironmentVariableProxy extends StringVariable {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private OSProcessScope ownerProcessScope;
    private String valueBeforeUndeclaration;

    // Constructors ----------------------------------------------------------------------------------------------------

    protected EnvironmentVariableProxy(OSProcessScope s, String name) {

        super(name);

        this.ownerProcessScope = s;
    }

    // Overrides -------------------------------------------------------------------------------------------------------

    @Override
    public String get() {

        if (valueBeforeUndeclaration != null) {

            return valueBeforeUndeclaration;
        }

        EnvironmentVariableProvider p = ownerProcessScope.getEnvironmentVariableProvider();

        String name = name();

        String value = p.getenv(name);

        if (value == null) {

            throw new RuntimeException("NOT YET IMPLEMENTED");
        }

        return value;
    }

    @Override
    public Object set(Object value) {

        if (value == null) {

            throw new NotSupportedException("cannot set null values for environment variables");
        }

        if (!(value instanceof String)) {

            throw new NotSupportedException("cannot set non-String values for environment variables");

        }

        String sValue = (String)value;

        String name = name();

        Object old = get();

        EnvironmentVariableProvider p = ownerProcessScope.getEnvironmentVariableProvider();

        if (!sValue.equals(p.getenv(name))) {

            p.export(name, sValue);
        }

        return old;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        if (valueBeforeUndeclaration != null) {

            return "UNDECLARED " + name();
        }

        return super.toString();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    OSProcessScope getOSProcessScope() {

        return ownerProcessScope;
    }

    /**
     * Invoking this method lets the proxy know that the corresponding variable was undeclared and does not exist
     * in the environment anymore.
     */
    void setUndeclared() {

        this.valueBeforeUndeclaration = get();
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
