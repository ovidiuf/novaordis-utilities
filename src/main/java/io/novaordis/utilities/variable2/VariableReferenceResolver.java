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
     * @return the string value of a variable whose name was specified.
     */
    public String resolveVariable(String variableName, Scope scope, boolean useBraces) {

        if (scope == null) {

            throw new IllegalArgumentException("null scope");
        }

        String s;

        Variable v = scope.getVariable(variableName);

        if (v == null) {

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

    /**
     * Resolves variable references, if the corresponding variables are declared, or leaves the references unchanged
     * if the corresponding variables are not declared.
     *
     * @exception IllegalNameException
     * @exception IllegalReferenceException
     * @exception IllegalArgumentException on null arguments
     */
    public String resolve(String stringWithVariableReferences, Scope scope) {

        if (stringWithVariableReferences == null) {

            throw new IllegalArgumentException("null string");
        }

        StringBuilder sb = new StringBuilder();

        int from = 0;
        int to = 0;

        boolean expectingOptionalLeftBrace = false;
        boolean optionalLeftBraceFound = false;
        String variableName = null;

        for(; to < stringWithVariableReferences.length(); to ++) {

            char c = stringWithVariableReferences.charAt(to);

            if (expectingOptionalLeftBrace) {

                from = to;
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

                    if (c == '}') {

                        if (!optionalLeftBraceFound) {

                            throw new IllegalReferenceException("unbalanced closing }");
                        }

                        from = to + 1;
                    }
                    else if (optionalLeftBraceFound) {

                        //
                        // got a terminator, but not '}', this is illegal
                        //

                        throw new IllegalReferenceException("missing closing }");
                    }
                    else {

                        from = to;
                    }

                    variableName = Variable.validateVariableName(variableName);
                    String s = resolveVariable(variableName, scope, optionalLeftBraceFound);
                    sb.append(s);
                    variableName = null;
                }
                else {

                    throw new IllegalReferenceException("misplaced '" + c + "' in variable reference");
                }
            }
            else if ('$' == c) {

                //
                // variable detected
                //

                sb.append(stringWithVariableReferences.substring(from, to));
                expectingOptionalLeftBrace = true;
            }
        }

        if (variableName != null) {

            if (optionalLeftBraceFound) {

                throw new IllegalReferenceException("unbalanced '{' in variable reference");
            }

            //
            // the string ends with a variable name declared with simplified notation
            //

            sb.append(resolveVariable(variableName, scope, false));
        }
        else if (expectingOptionalLeftBrace) {

            throw new IllegalReferenceException("empty variable reference");
        }
        else {

            sb.append(stringWithVariableReferences.substring(from, to));
        }

        return sb.toString();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
