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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/22/16
 */
public class StringWithVariables {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Fast scanner that establishes whether the given string contains variable references (valid or invalid).
     *
     * If the string does not contain variable references, it does not make sense to parse it and build a
     * StringWithVariables instance out of it.
     *
     * This is a speed optimization.
     *
     * The implementation is insensitive to null, will return false for it.
     */
    public static boolean containsVariableReferences(String s) {

        if (s == null) {

            return false;
        }

        for(int i = 0; i < s.length(); i ++) {

            char c = s.charAt(i);

            if (c == '$') {

                if (i == s.length() - 1) {

                    return false;
                }

                if (s.charAt(i + 1) == '{') {

                    return true;
                }
            }
        }

        return false;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private String literal;
    private List<Token> tokens;
    private boolean failOnMissingDefinition;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * The underlying variables will NOT fail on missing definition.
     *
     * @see StringWithVariables(String, boolean)
     */
    public StringWithVariables(String stringWithVariables) throws VariableFormatException {

        this(stringWithVariables, false);
    }

    /**
     * @param failOnMissingDefinition if true, a missing variable definition triggers an exception. If false, the
     *                                variable will be simply replaced with its literal.
     */
    public StringWithVariables(String stringWithVariables, boolean failOnMissingDefinition)
            throws VariableFormatException {

        if (stringWithVariables == null) {
            throw new IllegalArgumentException("null argument");
        }

        this.literal = stringWithVariables;
        this.tokens = new ArrayList<>();
        this.failOnMissingDefinition = failOnMissingDefinition;
        parse();
    }


    // Public ----------------------------------------------------------------------------------------------------------

    public String resolve(VariableProvider provider) throws VariableNotDefinedException {

        String s = "";

        for(Token t: tokens) {

            s += t.resolve(provider);
        }

        return s;
    }

    /**
     * @param keyValuePairs a source of key/value pairs that can be used to resolve variables.
     */
    public String resolve(Map<String, String> keyValuePairs) throws VariableNotDefinedException {

        String s = "";

        for(Token t: tokens) {

            s += t.resolve(keyValuePairs);
        }

        return s;
    }

    /**
     * @param keyValuePairs a list of successive key and value pairs: the key is used as variable name and the
     *                            subsequent value as variable value. If the number of arguments is odd, the last
     *                            key is ignored.
     */
    public String resolve(String ... keyValuePairs) throws VariableNotDefinedException {

        Map<String, String> map = new HashMap<>();

        for(int i = 0; i < keyValuePairs.length; i = i + 2) {

            String key = keyValuePairs[i];

            if (i + 1 == keyValuePairs.length) {
                break;
            }

            String value = keyValuePairs[i + 1];
            map.put(key, value);
        }

        return resolve(map);
    }

    public String getLiteral() {
        return literal;
    }

    /**
     * Default is false.
     * @return true if an attempt to resolve the underlying variables will fail on missing definition, false otherwise.
     */
    public boolean isFailOnMissingDefinition() {

        return failOnMissingDefinition;
    }

    @Override
    public String toString() {
        return literal;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    List<Token> getTokens() {
        return tokens;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse() throws VariableFormatException {

        int crt = 0;
        int i;

        while(crt < literal.length()) {

            i = literal.indexOf("${", crt);

            if (i == -1 || i != crt) {

                //
                // constant
                //

                i = i != -1 ? i : literal.length();
                tokens.add(new Constant(literal.substring(crt, i)));
                crt = i;
            }
            else {

                //
                // variable
                //

                //
                // find the end bracket
                //

                crt = i;

                i = literal.indexOf('}', crt);

                if (i == -1) {

                    throw new VariableFormatException("invalid variable reference, missing closing bracket");
                }

                tokens.add(new Variable(literal.substring(crt + 2, i), failOnMissingDefinition));

                crt = i + 1;
            }

        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
