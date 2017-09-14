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

package io.novaordis.utilities.variable2;

/**
 * A scope for variables. All scopes are hierarchical by default, they may have zero or one parents.
 *
 * Variables favor values assigned in the current scope over values assigned in enclosing scopes. However, if no
 * value was assigned in the current scope, the variable returns the value assigned in the closest scope up the
 * hierarchy.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public interface Scope {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * A variable declaration without assignment. A declaration in a scople is the only way to create variable
     * instances.
     *
     * @return the variable <b>declared</b> in this scope.
     *
     * @exception IllegalTypeException if variable of the specified type are not supported.
     */
    <T> Variable<T> declare(String name, Class<? extends T> type);

    /**
     * A variable declaration with assignment. A declaration in a scope is the only way to create variable instances.
     *
     * @return the variable <b>declared</b> in this scope.
     *
     * @exception IllegalTypeException if variable of the specified type are not supported.
     */
    <T> Variable<T> declare(String name, Class<? extends T> type, T value);

    /**
     * @return a variable declared in this scope, or within the closest enclosing scope in which the variable was
     * declared, that matches the name. If the variable was not declared in scope, the method will return null. If the
     * variable was declared, but it has a null value, getVariable() will return a non-null instance with a null value.
     */
    Variable getVariable(String name);

    /**
     * Declare an enclosed scope.
     */
    void enclose(Scope scope);

    /**
     * Resolves variables and evaluates expressions.
     *
     * @exception IllegalNameException if the variable name references are not valid variable names.
     */
    String evaluate(String stringContainingVariables);

}
