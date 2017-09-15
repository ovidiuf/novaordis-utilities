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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public class ScopeImpl implements Scope {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // variable declarations in this scope - these instances are never exposes, only copies; they are maintain in the
    // order of the declaration
    private List<Variable> declarations;

    //
    // the parent of this scope - is the directly enclosing scope. If the parent is null, this scope is not
    // enclosed within anything
    //
    private Scope parent;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ScopeImpl() {

        this.declarations = new ArrayList<>();
        this.parent = null;
    }

    // Scope implementation --------------------------------------------------------------------------------------------

    @Override
    public <T> Variable<T> declare(String name, Class<? extends T> type) {

        return declare(name, type, null);
    }

    @Override
    public <T> Variable<T> declare(String name, Class<? extends T> type, T value) {

        //
        // we cannot have two variables with the same name declared in scope
        //

        for(Variable d: declarations) {

            if (name.equals(d.name())) {

                throw new DuplicateDeclarationException(name);
            }
        }

        Variable<T> v;

        if (String.class.equals(type)) {

            //noinspection unchecked
            v = new StringVariable(name);
        }
        else {

            throw new IllegalTypeException(type.toString());
        }

        v.set(value);

        declarations.add(v);

        //noinspection unchecked
        return v;
    }

    @Override
    public List<Variable> getVariablesDeclaredInScope() {

        if (declarations.isEmpty()) {

            return Collections.emptyList();
        }

        List<Variable> result = new ArrayList<>();

        //noinspection Convert2streamapi
        for(Variable d: declarations) {

            result.add(((VariableBase)d).copy());
        }

        return result;
    }

    @Override
    public Variable getVariable(String name) {

        for(Variable d: declarations) {

            if (d.name().equals(name)) {

                return ((VariableBase)d).copy();
            }
        }

        //
        // not in this scope, search up
        //

        if (parent == null) {

            return null;
        }

        return parent.getVariable(name);
    }

    @Override
    public void enclose(Scope scope) {

        ((ScopeImpl)scope).setParent(this);
    }

    @Override
    public String evaluate(String stringContainingVariables) {

        StringBuilder sb = new StringBuilder();

        int from = 0;
        int to = 0;

        boolean expectingOptionalLeftBrace = false;
        boolean optionalLeftBraceFound = false;
        String variableName = null;

        for(; to < stringContainingVariables.length(); to ++) {

            char c = stringContainingVariables.charAt(to);

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
                    else {

                        from = to;
                    }

                    variableName = Variable.validateVariableName(variableName);
                    String s = toStringValue(variableName, optionalLeftBraceFound);
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

                sb.append(stringContainingVariables.substring(from, to));
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

            sb.append(toStringValue(variableName, false));
        }
        else {

            sb.append(stringContainingVariables.substring(from, to));
        }

        return sb.toString();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    void setParent(Scope parent) {

        this.parent = parent;
    }

    /**
     * @param  useBraces if the variable is not declared in scope, the string representation is the variable reference
     *                   which is rendered surrounded by braces or not, depending the value of this flag.
     * @return the string value of a variable whose name was specified.
     */
    String toStringValue(String variableName, boolean useBraces) {

        String s;

        Variable v = getVariable(variableName);

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

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
