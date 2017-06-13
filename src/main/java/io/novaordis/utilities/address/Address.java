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

package io.novaordis.utilities.address;

/**
 * Provides a unique identity to a coarse grained addressable resource.
 *
 * Used so far to identify network services identified by a protocol and host name, and optionally username/password,
 * and port. The "coarse grained" characterization was introduced to emphasize that we don't support paths.
 *
 * Examples:
 *
 * the operating system on the local host
 * ssh://sshuser:sshpasswd@remotehost:22
 *
 * jmx://admin:adminpasswd@1.2.3.4:8888
 * jbosscli://admin:adminpasswd@4.5.6.7:9999
 * http://localhost:80
 *
 * All implementations must correctly implement equals() and hashCode().
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public interface Address {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * May return null, but in this case, equals() cannot be used.
     */
    String getProtocol();

    void setProtocol(String protocol);

    /**
     * Must never return null.
     */
    String getHost();

    /**
     * Must always return a valid port number or null when the address does not support the concept of a port (as is the
     * case for LocalOSAddress).
     */
    Integer getPort();

    /**
     * May return null.
     */
    String getUsername();

    /**
     * May return null.
     */
    char[] getPassword();

    /**
     * The literal representation of the address. Must never return null.
     */
    String getLiteral();

    Address copy();

    //
    // implementations must correctly implement equals()
    //

    @Override
    boolean equals(Object o);

    //
    // implementations must correctly implement hashCode()
    //

    @Override
    int hashCode();


}
