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

import java.util.ArrayList;
import java.util.List;

/**
 * A variable reference resolver.
 *
 * It is thread safe.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/15/17
 */
public class VariableReferenceResolver {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Given a variable name, the method returns the string representation of the variable value in scope, or the
     * literal representation of the variable reference ("${var_name}") if the variable is not declared.
     *
     * @param  useBraces if the variable is not declared in scope, the string representation is the variable reference
     *                   which is rendered surrounded by braces or not, depending the value of this flag.
     *
     * @param failOnUndeclaredVariable if true, and a variable reference cannot be resolved in scope, the method will
     *                              throw UndeclaredVariableException. If false, and a variable reference cannot be
     *                              resolved in scope, the method will maintain the variable reference unchanged in
     *                              the result string.
     *
     * @return the string value of a variable whose name was specified.
     *
     * @exception UndeclaredVariableException if 'failOnUndeclaredVariable' is true an at least one undeclared variable
     * is identified. If multiple variables are present, UndeclaredVariableException will carry the name of the first
     * identified undeclared variable.

     */
    public String resolveVariable(
            String variableName, Scope scope, boolean useBraces, boolean failOnUndeclaredVariable)
            throws UndeclaredVariableException {

        if (scope == null) {

            throw new IllegalArgumentException("null scope");
        }

        String s;

        Variable v = scope.getVariable(variableName);

        if (v == null) {

            if (failOnUndeclaredVariable) {

                throw new UndeclaredVariableException(variableName, null);
            }

            if (useBraces) {

                s = "${";
            }
            else {

                s = "$";
            }

            s += variableName;

            if (useBraces) {

                s += "}";
            }
        }
        else {

            Object value = v.get();

            //
            // null value translates to empty space
            //

            if (value == null) {

                value = "";
            }

            s = value.toString();
        }

        return s;
    }

    public List<VariableReference> getVariableReferences(String stringWithVariableReferences) {

        if (stringWithVariableReferences == null) {

            throw new IllegalArgumentException("null string");
        }

        List<VariableReference> references = new ArrayList<>();

        int i = 0;
        int startIndex = -1;

        boolean expectingOptionalLeftBrace = false;
        boolean optionalLeftBraceFound = false;
        String variableName = null;

        for(; i < stringWithVariableReferences.length(); i++) {

            char c = stringWithVariableReferences.charAt(i);

            if (expectingOptionalLeftBrace) {

                variableName = "";
                expectingOptionalLeftBrace = false;

                if ('{' == c) {

                    optionalLeftBraceFound = true;
                    continue;
                }
            }

            if (variableName != null) {

                if (Variable.validVariableNameChar(c)) {

                    variableName += c;
                }
                else if (Variable.variableReferenceTerminator(c)){

                    //
                    // end of variable name
                    //

                    int endIndex = i;

                    if (c == '}') {

                        if (!optionalLeftBraceFound) {

                            throw new IllegalReferenceException(variableName, "unbalanced closing '}'");
                        }
                    }
                    else if (optionalLeftBraceFound) {

                        //
                        // got a terminator, but not '}', this is illegal
                        //

                        throw new IllegalReferenceException(variableName, "missing closing '}'");
                    }
                    else {

                        endIndex --;
                    }

                    VariableReference r =
                            new VariableReference(variableName, startIndex, endIndex, optionalLeftBraceFound);

                    references.add(r);
                    variableName = null;
                }
                else {

                    throw new IllegalReferenceException(variableName, "misplaced '" + c + "' in variable reference");
                }
            }
            else if ('$' == c) {

                //
                // variable detected
                //

                expectingOptionalLeftBrace = true;
                startIndex = i;
            }
        }

        if (variableName != null) {

            if (optionalLeftBraceFound) {

                throw new IllegalReferenceException(variableName, "unbalanced '{' in variable reference");
            }

            //
            // the string ends with a variable name declared with simplified notation
            //

            VariableReference r = new VariableReference(variableName, startIndex, i - 1, false);
            references.add(r);
        }
        else if (expectingOptionalLeftBrace) {

            throw new IllegalReferenceException("", "empty variable reference");
        }

        return references;
    }

    /**
     * Resolves variable references, if the corresponding variables are declared, or leaves the references unchanged
     * if the corresponding variables are not declared.
     *
     * @param failOnUndeclaredVariable if true, and a variable reference cannot be resolved in scope, the method will
     *                              throw UndeclaredVariableException. If false, and a variable reference cannot be
     *                              resolved in scope, the method will maintain the variable reference unchanged in
     *                              the result string.
     *
     * @exception UndeclaredVariableException if 'failOnUndeclaredVariable' is true an at least one undeclared variable
     * is identified. If multiple variables are present, UndeclaredVariableException will carry the name of the first
     * identified undeclared variable.
     *
     * @exception IllegalNameException
     * @exception IllegalReferenceException
     * @exception IllegalArgumentException on null arguments
     */
    public String resolve(String stringWithVariableReferences, Scope scope, boolean failOnUndeclaredVariable)
            throws UndeclaredVariableException {

        if (stringWithVariableReferences == null) {

            throw new IllegalArgumentException("null string");
        }

        List<VariableReference> references = getVariableReferences(stringWithVariableReferences);

        if (references.isEmpty()) {

            return stringWithVariableReferences;
        }

        //
        // replace
        //

        StringBuilder sb = new StringBuilder();

        int i = 0;

        for(VariableReference r: references) {

            sb.append(stringWithVariableReferences.substring(i, r.getStartIndex()));
            String s = resolveVariable(r.getName(), scope, r.hasBraces(), failOnUndeclaredVariable);
            sb.append(s);
            i = r.getEndIndex() + 1;
        }

        if (i <= stringWithVariableReferences.length()) {

            sb.append(stringWithVariableReferences.substring(i));
        }

        return sb.toString();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
