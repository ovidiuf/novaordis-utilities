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
 * The IllegalReferenceException exception carries the variable name - or what it is thought to be the variable name -
 * extracted from the variable reference literal that caused it, in addition to the general message.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
public class IllegalReferenceException extends VariableException {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    //
    // the variable reference, including the ${} that caused the exception, even if it's invalid (in this case, is the
    // string that is thought to be the variable reference
    //
    private String variableName;

    // Constructors ----------------------------------------------------------------------------------------------------

    public IllegalReferenceException(String variableName, String msg) {

        super(msg);

        this.variableName = variableName;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * The variable name - or what it is thought to be the variable name - extracted from the variable reference literal
     * that caused the exception.
     */
    public String getVariableName() {

        return variableName;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
