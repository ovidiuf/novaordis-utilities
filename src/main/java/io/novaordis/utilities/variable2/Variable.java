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
 * A scoped, typed and named placeholder for values. Variable instances can only be created by declaring them in a
 * scope, with Scope.declare(). Once declared in a scope, the variable is available to all enclosed scopes.The same
 * variable may be declared in more than one scope, and may have different values in different scopes.
 *
 * Values are assigned to a variable with set() and retrieved with get(). The value of a variable may be null.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public interface Variable<T> {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * A variable name must start with a letter and consists in letters, digits, '_' and '-', and must not belong
     * to the set of reserved names.
     *
     * Never null.
     */
    String name();

    /**
     * The variable type, never null.
     */
    Class<? extends T> type();

    T get();

    void set(T value);

}
