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
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/13/17
 */
class ScopeImpl implements Scope {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // variable declarations - these instances are never exposes, only copies; they are maintain in the order of
    // the declaration
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

//    @Override
//    public List<Variable> getDeclaredVariables() {
//        throw new RuntimeException("getDeclaredVariables() NOT YET IMPLEMENTED");
//    }

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

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    void setParent(Scope parent) {

        this.parent = parent;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
