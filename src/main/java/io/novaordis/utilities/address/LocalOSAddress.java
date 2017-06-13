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
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class LocalOSAddress implements OSAddress {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PROTOCOL = "localOS";
    public static final String HOST = "localhost";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    public LocalOSAddress() {
    }

    // Address implementation ------------------------------------------------------------------------------------------

    @Override
    public String getProtocol() {

        return PROTOCOL;
    }

    @Override
    public void setProtocol(String protocol) {

        if (!PROTOCOL.equals(protocol)) {

            throw new IllegalArgumentException("cannot change protocol to " + protocol);
        }
    }

    @Override
    public String getHost() {

        return HOST;
    }

    @Override
    public Integer getPort() {

        return null;
    }

    @Override
    public String getUsername() {

        return null;
    }

    @Override
    public char[] getPassword() {

        return null;
    }

    @Override
    public String getLiteral() {

        return PROTOCOL + "://";
    }

    @Override
    public LocalOSAddress copy() {

        return new LocalOSAddress();
    }

    @Override
    public boolean equals(Object o) {

        return o instanceof LocalOSAddress;
    }

    @Override
    public int hashCode() {

        return 1;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
