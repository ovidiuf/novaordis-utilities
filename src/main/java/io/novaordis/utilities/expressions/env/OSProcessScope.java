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

package io.novaordis.utilities.expressions.env;

import io.novaordis.utilities.expressions.DuplicateDeclarationException;
import io.novaordis.utilities.expressions.ScopeBase;
import io.novaordis.utilities.expressions.Variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A scope that is aware of the environment variables declared in the OS process environment.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/17/17
 */
public class OSProcessScope extends ScopeBase {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private EnvironmentVariableProvider environmentVariableProvider;

    //
    // we keep track of variables we declared to identify duplicate declarations
    //

    private List<String> declaredVariableNames;

    // Constructors ----------------------------------------------------------------------------------------------------

    public OSProcessScope() {

        this.declaredVariableNames = new ArrayList<>();
        this.environmentVariableProvider = new SystemEnvironmentVariableProvider();
    }

    // ScopeBase overrides ---------------------------------------------------------------------------------------------

    /**
     * Depending on the underlying EnvironmentVariableProvider implementation, declaring environment variables in the
     * process' environment may not be possible, and this method may throw an exception.
     */
    @Override
    public <T> Variable<T> declare(String name, Class<? extends T> type) {

        return declareEnvironmentVariable(name, type, null);
    }

    /**
     * Depending on the underlying EnvironmentVariableProvider implementation, declaring environment variables in the
     * process' environment may not be possible, and this method may throw an exception.
     */
    @Override
    public <T> Variable<T> declare(String name, T value) {

        return declareEnvironmentVariable(name, null, value);
    }

    /**
     * Depending on the underlying EnvironmentVariableProvider implementation, undeclaring environment variables in the
     * process' environment may not be possible, and this method may throw an exception.
     */
    @Override
    public Variable undeclare(String name) {

        EnvironmentVariableProxy v =  getVariable(name);

        if (v == null) {

            return null;
        }

        v.undeclare();

        environmentVariableProvider.unset(name);
        declaredVariableNames.remove(name);

        return v;
    }

    /**
     * Overrides superclass, because EnvironmentVariableProxy do not carry the value with them, but query the
     * environment every time get() is invoked.
     */
    @Override
    public EnvironmentVariableProxy getVariable(String name) {

        String value = environmentVariableProvider.getenv(name);

        if (value == null) {

            //
            // there's never an enclosing scope, we cannot control that from the JVM, so we don't delegate to the
            // enclosing scope, as it is the case generally
            //

            return null;
        }

        return new EnvironmentVariableProxy(this, name);
    }

    @Override
    public List<Variable> getVariablesDeclaredInScope() {

        if (declaredVariableNames.isEmpty()) {

            return Collections.emptyList();
        }

        List<Variable> result = new ArrayList<>();

        //noinspection Convert2streamapi
        for(String name: declaredVariableNames) {

            result.add(getVariable(name));
        }

        return result;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    EnvironmentVariableProvider getEnvironmentVariableProvider() {

        return environmentVariableProvider;
    }

    /**
     * For testing.
     */
    void setEnvironmentVariableProvider(EnvironmentVariableProvider p) {

        this.environmentVariableProvider = p;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    public <T> Variable<T> declareEnvironmentVariable(String name, Class<? extends T> type, T value) {

        //
        // cannot declare undefined environment variables
        //

        if (value == null) {

            throw new UnsupportedOperationException("cannot declare undefined environment variables");
        }

        Class effectiveType;

        if (type == null) {

            effectiveType = value.getClass();
        }
        else {

            effectiveType = type;
        }

        if (!String.class.equals(effectiveType)) {

            throw new UnsupportedOperationException("cannot declare " + effectiveType + " variables");
        }

        if (declaredVariableNames.contains(name)) {

            throw new DuplicateDeclarationException(name);
        }

        environmentVariableProvider.export(name, value.toString());

        declaredVariableNames.add(name);

        //noinspection unchecked
        return getVariable(name);
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
