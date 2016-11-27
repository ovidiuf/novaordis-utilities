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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/22/16
 */
public class VariableProviderImpl implements VariableProvider {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private VariableProvider parent;

    private Map<String, String> localValues;

    // Constructors ----------------------------------------------------------------------------------------------------

    public VariableProviderImpl() {
        localValues = new HashMap<>();
    }

    // VariableProvider implementation ---------------------------------------------------------------------------------

    @Override
    public String getVariableValue(String variableName) {

        String localValue = localValues.get(variableName);

        //
        // if found, local values have priority
        //

        if (localValue != null) {

            return localValue;
        }

        //
        // if not found, and we have a parent, delegate to the parent
        //

        if (parent == null) {

            return null;
        }

        return parent.getVariableValue(variableName);
    }

    @Override
    public String setVariableValue(String variableName, String variableValue) {

        return localValues.put(variableName, variableValue);
    }

    @Override
    public VariableProvider getVariableProviderParent() {

        return parent;
    }

    @Override
    public void setVariableProviderParent(VariableProvider parent) {

        this.parent = parent;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
