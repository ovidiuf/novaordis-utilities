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
 * A variable reference resolver. It is capable of resolving variable references using the variable values pulled
 * from a scope, or specified in-line.
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
     * Given a variable name, the method returns the string representation of the variable value in scope, or the
     * literal representation of the variable reference ("${var_name}") if the variable is not declared. If the variable
     * is declared, but it has a null value, the value is interpreted automatically as the empty string.
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
     * @exception UndeclaredVariableException if 'failOnUndeclaredVariable' is true an dat least one undeclared variable
     * is identified. If multiple variables are present, UndeclaredVariableException will carry the name of the first
     * identified undeclared variable.
     */
    public String resolveVariableInScope(
            String variableName, Scope scope, boolean failOnUndeclaredVariable, boolean useBraces)
            throws UndeclaredVariableException {

        if (scope == null) {

            throw new IllegalArgumentException("null scope");
        }

        Variable v = scope.getVariable(variableName);

        Object value;

        if (v == null) {

            if (failOnUndeclaredVariable) {

                throw new UndeclaredVariableException(variableName, null);
            }

            value = null;
        }
        else {

            value = v.get();

            if (value == null) {

                value = "";
            }
        }

        return resolveVariableInLine(variableName, value, useBraces);
    }

    /**
     * Given a variable name, and a value that may or may not be null, the method returns the string representation of
     * the resolved variable reference, subject to settings such as "failOnNull" or "useBraces", as described below.
     *
     * @param longFormat if true, and the provided value is null, the method produces the variable reference
     *                   representation in long format (${variableName}). If false, and the provided value is null,
     *                   the method produces the variable reference representation in short format ($variableName)
     *
     * @return the value converted to string, or the variable representation if null.
     */
    public String resolveVariableInLine(String variableName, Object value, boolean longFormat) {

        if (value != null) {

            return value.toString();
        }

        String s;

        if (longFormat) {

            s = "${";
        }
        else {

            s = "$";
        }


        s += variableName;

        if (longFormat) {

            s += "}";
        }

        return s;
    }

    /**
     * Resolves variable references, if the corresponding variables are declared, or leaves the references unchanged
     * if the corresponding variables are not declared (or fails, if failOnUndeclaredVariable is true).
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
    public String resolve(String stringWithVariableReferences, boolean failOnUndeclaredVariable, Scope scope)
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
            String s = resolveVariableInScope(r.getName(), scope, failOnUndeclaredVariable, r.hasBraces());
            sb.append(s);
            i = r.getEndIndex() + 1;
        }

        if (i <= stringWithVariableReferences.length()) {

            sb.append(stringWithVariableReferences.substring(i));
        }

        return sb.toString();
    }

    /**
     * In-line variable resolving.
     *
     * @param failOnUndeclaredVariable if true, and one of the variable names is not mentioned among arguments,
     *                                 throws UndeclaredVariableException
     *
     * @throws UndeclaredVariableException if if true, and one of the variable names is not mentioned among arguments.
     */
    public String resolve(
            String stringWithVariableReferences,
            boolean failOnUndeclaredVariable,
            String firstVariableName,
            Object firstValue,
            Object... theRestOfVariableNameAndValuePairsInOrder)
            throws UndeclaredVariableException {

        if (stringWithVariableReferences == null) {

            throw new IllegalArgumentException("null string");
        }

        List<VariableReference> references = getVariableReferences(stringWithVariableReferences);

        if (references.isEmpty()) {

            return stringWithVariableReferences;
        }

        int optionalPairs =
                theRestOfVariableNameAndValuePairsInOrder == null ? 0 :
                        theRestOfVariableNameAndValuePairsInOrder.length / 2;

        //
        // replace
        //

        StringBuilder sb = new StringBuilder();

        int i = 0;

        for(VariableReference r: references) {

            sb.append(stringWithVariableReferences.substring(i, r.getStartIndex()));

            String variableName = r.getName();

            Object value = null;

            boolean variableNameFound = false;

            //
            // scan for the first occurrence of a variable name that matches
            //

            if (variableName.equals(firstVariableName)) {

                variableNameFound = true;
                value = firstValue;
            }
            else {

                for(int j = 0; j < optionalPairs; j ++) {

                    if (!(theRestOfVariableNameAndValuePairsInOrder[2 * j] instanceof String)) {

                        throw new IllegalArgumentException(
                                "argument " + theRestOfVariableNameAndValuePairsInOrder[2 * j] +
                                        " is supposed to be a String variable name");
                    }

                    String optionalVariableName = (String)theRestOfVariableNameAndValuePairsInOrder[2 * j];

                    if (variableName.equals(optionalVariableName)) {

                        variableNameFound = true;
                        value = theRestOfVariableNameAndValuePairsInOrder[2 * j + 1];
                    }
                }
            }

            if (!variableNameFound) {

                if (failOnUndeclaredVariable) {

                    throw new UndeclaredVariableException(variableName, null);
                }
            }

            String s = resolveVariableInLine(variableName, value, r.hasBraces());

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
