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
 * Basic variable implementation. It keeps track of the scopes it was declared in.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
abstract class VariableBase<T> implements Variable<T>, Cloneable {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;

    private T value;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @exception IllegalNameException
     */
    VariableBase(String name) {

        setName(Variable.validateVariableName(name));
    }

    // Variable implementation -----------------------------------------------------------------------------------------

    @Override
    public String name() {

        return name;
    }

    @Override
    public T get() {

        return value;
    }

    @Override
    public void set(T value) {

        this.value = value;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public Object clone() {

        try {

            Object o = super.clone();

            ((VariableBase)o).name = this.name;
            ((VariableBase)o).value = this.value;

            return o;
        }
        catch(CloneNotSupportedException e) {

            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {

        return name + "=" + value;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected void setName(String name) {

        this.name = name;
    }

    protected VariableBase copy() {

        return (VariableBase) this.clone();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
