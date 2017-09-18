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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/16/17
 */
public class VariableReference {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;

    private boolean hasBraces;

    private int startIndex;
    private int endIndex;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param name the variable name.
     *
     * @param startIndex in the enclosing string.
     *
     * @param endIndex in the enclosing string - is the index of last character of the reference (which may be '}' or
     *                 the last character of the name).
     *
     * @param hasBraces whether the variable reference is specified using the long format ${var} or short format $var.
     *
     * @exception IllegalNameException if the name is illegal.
     */
    public VariableReference(String name, int startIndex, int endIndex, boolean hasBraces) {

        this.name = Variable.validateVariableName(name);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.hasBraces = hasBraces;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String getName() {

        return name;
    }

    public int getStartIndex() {

        return startIndex;
    }

    public int getEndIndex() {

        return endIndex;
    }

    public boolean hasBraces() {

        return hasBraces;
    }

    @Override
    public String toString() {

        return startIndex + ":$" + (hasBraces ? "{" : "") + name + (hasBraces ? "}:" : ":") + endIndex;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
