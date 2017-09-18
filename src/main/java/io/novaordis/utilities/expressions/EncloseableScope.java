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
 * A scope that can be enclosed in other scopes. Once enclosed in a parent scope, the scope's variables become subject
 * to the scoped visibility rules.
 *
 * A generic scope is not encloseable by default, unless it implements this interface.
 *
 * https://kb.novaordis.com/index.php/Nova_Ordis_Variables#Scopes_and_Visibility
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 9/18/17
 */
public interface EncloseableScope extends Scope {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the closest enclosing scope May return null if this scope is not enclosed in any other scope.
     */
    Scope getEnclosing();

    void setParent(Scope parent);


}
