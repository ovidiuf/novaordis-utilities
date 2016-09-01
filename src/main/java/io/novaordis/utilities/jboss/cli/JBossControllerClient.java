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

package io.novaordis.utilities.jboss.cli;

/**
 * Stateful JBoss controller client, requires connection via the connect() method, and it can be disconnected, thus
 * releasing resources. Local connection is possible, if the controller is located on the same host, for local
 * connection username and password should be null. The user should be part of the management realm.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 8/31/16
 */
public interface JBossControllerClient {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Connects the client to the controller.
     *
     * @throws CliException
     */
    void connect() throws CliException;

    /**
     * Disconnects the client, and releases resources.
     */
    void disconnect();

    /**
     * Sends a command into the controller that returns the value of the specified attribute, as String.
     *
     * null can be returned if the attribute is not defined.
     *
     * @throws CliException if the path is invalid (does not exist on controller)
     */
    String getAttributeValue(String path, String attributeName) throws CliException;



}
