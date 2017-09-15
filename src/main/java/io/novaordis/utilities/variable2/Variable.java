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

import java.util.regex.Pattern;

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

    //
    // Important: modifications to this patter should be kept in sync with the implementation of validVariableNameChar()
    //            below.
    //
    String VARIABLE_NAME_PATTERN_STRING = "[a-zA-z][a-zA-z0-9_\\-\\.]*";

    Pattern VARIABLE_NAME_PATTERN = Pattern.compile("^" + VARIABLE_NAME_PATTERN_STRING + "$");

    String[] RESERVED_NAMES = {

            "null"
    };

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return the valid variable name.
     *
     * @throws IllegalNameException in case of illegal variable name. The message contains the variable name.
     */
    static String validateVariableName(String s) {

        if (s == null) {

            throw new IllegalNameException("null");
        }

        if (!VARIABLE_NAME_PATTERN.matcher(s).matches()) {

            throw new IllegalNameException(s);
        }

        for(String rv: RESERVED_NAMES){

            if (rv.equals(s)) {

                throw new IllegalNameException("reserved name: '" + s + "'");
            }
        }

        return s;
    }

    /**
     * Important: modifications to this method should be kept in sync with the content of the VARIABLE_NAME_PATTERN_STRING, above.
     *
     * @see Variable#VARIABLE_NAME_PATTERN_STRING
     */
    static boolean validVariableNameChar(char c) {

        return
                ((int)'a' <= c && c <= (int)'z') ||
                        ((int)'A' <= c && c <= (int)'Z') ||
                        ((int)'0' <= c && c <= (int)'9') ||
                        c == '-' ||
                        c == '_' ||
                        c == '.';
    }

    /**
     * A character that indicates the end (implicit or explicit) of a variable reference.
     *
     * Example: '}', ' ', '/', etc.
     */
    static boolean variableReferenceTerminator(char c) {

        return '}' == c || ' ' == c || '/' == c || ':' == c;
    }

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

    /**
     * @return the previous value associated with the variable or null if there wasn't any.
     */
    T set(T value);

}
