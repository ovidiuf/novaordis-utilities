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
import java.util.Collections;
import java.util.Iterator;
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

    private VariableReferenceResolver variableReferenceResolver;

    // Constructors ----------------------------------------------------------------------------------------------------

    public ScopeImpl() {

        this.variableReferenceResolver = new VariableReferenceResolver();
        this.declarations = new ArrayList<>();
        this.parent = null;
    }

    // Scope implementation --------------------------------------------------------------------------------------------

    @Override
    public <T> Variable<T> declare(String name, Class<? extends T> type) {

        if (type == null) {

            throw new IllegalTypeException("variable type cannot be inferred");
        }

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
    public <T> Variable<T> declare(String name, T value) {

        if (value == null) {

            throw new IllegalTypeException("variable type cannot be inferred");
        }

        Class type  = value.getClass();

        //noinspection unchecked
        return declare(name, type, value);
    }

    @Override
    public Variable undeclare(String name) {

        for(Iterator<Variable> i = declarations.iterator(); i.hasNext(); ) {

            Variable v = i.next();

            if (v.name().equals(name)) {

                i.remove();
                return v;
            }
        }

        return null;
    }

    @Override
    public List<Variable> getVariablesDeclaredInScope() {

        if (declarations.isEmpty()) {

            return Collections.emptyList();
        }

        List<Variable> result = new ArrayList<>();

        //noinspection Convert2streamapi
        for(Variable d: declarations) {

            result.add(d);
        }

        return result;
    }

    @Override
    public Variable getVariable(String name) {

        for(Variable d: declarations) {

            if (d.name().equals(name)) {

                return d;
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
    public Scope getEnclosing() {

        return parent;
    }

    @Override
    public String evaluate(String stringWithVariableReferences) {

        try {

            return evaluate(stringWithVariableReferences, false);
        }
        catch(UndeclaredVariableException e) {

            //
            // this is abnormal, we cannot throw UndeclaredVariableException if we requested the method not to fail
            // on undeclared references
            //

            throw new IllegalStateException(e);
        }
    }

    @Override
    public String evaluate(String stringWithVariableReferences, boolean failOnUndeclaredVariable)
            throws UndeclaredVariableException {

        return variableReferenceResolver.resolve(stringWithVariableReferences, this, failOnUndeclaredVariable);
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
