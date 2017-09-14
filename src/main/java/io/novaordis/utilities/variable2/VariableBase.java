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
 * Basic variable implementation. It keeps track of the scopes it was declared in.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
abstract class VariableBase<T> implements Variable<T>, Cloneable {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("^[a-zA-z][a-zA-z0-9_\\-]*$");

    public static final String[] RESERVED_NAMES = {

            "null"
    };

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String name;

    private T value;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @exception IllegalNameException
     */
    VariableBase(String name) {

        if (name == null) {

            throw new IllegalNameException("null");
        }

        if (!VARIABLE_NAME_PATTERN.matcher(name).matches()) {

            throw new IllegalNameException(name);
        }

        for(String s: RESERVED_NAMES){

            if (name.equals(s)) {

                throw new IllegalNameException("reserved name: '" + name + "'");
            }
        }

        setName(name);
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
