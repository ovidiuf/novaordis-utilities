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

import java.util.Map;

/**
 * A Constant or a Variable.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/22/16
 */
public interface Token {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    String getLiteral();

    /**
     * Attempts to resolve the variable (if the underlying implementation is a variable) with values read from the
     * provider.
     *
     * If the variable is not found, the behavior of the method depends on the isFailOnMissingDefinition()
     * configuration. If the instance is configured to NOT fail on missing definition (the default behavior), the
     * variable definition will be left unchanged in the result string. If the instance is configured to fail on
     * missing definition, the method will throw VariableNotDefinedException.
     *
     * @see Token#isFailOnMissingDefinition()
     *
     * @exception VariableNotDefinedException if the instance is configured to fail if the variable is not defined, or
     * the provider is null.
     */
    String resolve(VariableProvider provider) throws VariableNotDefinedException;

    /**
     * @see Token#resolve(VariableProvider)
     */
    String resolve(Map<String, String> map) throws VariableNotDefinedException;

    boolean isFailOnMissingDefinition();

    /**
     * If set to fail on missing definition, an attempt to resolve the token when the definition is missing will
     * throw a VariableNotDefinedException. Otherwise the variable literal is left in place without any attempt to
     * resolve it.
     */
    void setFailOnMissingDefinition(boolean b);

}
