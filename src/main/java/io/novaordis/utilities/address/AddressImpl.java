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
public class AddressImpl implements Address {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final String PROTOCOL_SEPARATOR = "://";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String protocol;
    private String host;
    private Integer port;
    private String username;
    private char[] password;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param address the string representation of the address in the format:
     *                protocol://[username[:password]]@hostname[:port]
     *
     * @throws AddressException
     */
    public AddressImpl(String address) throws AddressException {

        parse(address);
    }

    // Address implementation ------------------------------------------------------------------------------------------

    @Override
    public String getProtocol() {

        return protocol;
    }

    @Override
    public String getHost() {

        return host;
    }

    @Override
    public Integer getPort() {

        return port;
    }

    @Override
    public String getUsername() {

        return username;
    }

    @Override
    public char[] getPassword() {

        return password;
    }

    @Override
    public String getLiteral() {

        String s = "";

        if (username != null) {

            s += username + "@";
        }

        s += host;

        if (port != null) {

            s += ":";
            s += port;
        }

        return s;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o)  {

        if (this == o) {

            return true;
        }

        if (!(o instanceof AddressImpl)) {

            return false;
        }

        AddressImpl that = (AddressImpl)o;

        if (this.protocol == null) {

            return false;
        }

        if (!this.protocol.equals(that.protocol)) {

            return false;
        }

        if (this.host == null) {

            return false;
        }

        if (!this.host.equals(that.host)) {

            return false;
        }

        if (this.username == null) {

            if (that.username != null) {

                return false;
            }
        }
        else {

            if (!this.username.equals(that.username)) {

                return false;
            }
        }

        if (this.port == null) {

            if (that.port != null) {

                return false;
            }
        }
        else {

            if (!this.port.equals(that.port)) {

                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {

        return getLiteral().hashCode();
    }

    @Override
    public String toString() {

        String s = "";

        if (username != null) {

            s += username + ":***@";
        }

        s += host;

        if (port != null) {

            s += ":";
            s += port;
        }

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected void setProtocol(String p) {

        this.protocol = p;
    }

    // Private ---------------------------------------------------------------------------------------------------------

    private void parse(String address) throws AddressException {

        if (address == null) {

            throw new IllegalArgumentException("null address");
        }

        int i = address.indexOf(PROTOCOL_SEPARATOR);

        if (i != -1) {

            String p = address.substring(0, i);
            setProtocol(p);
            address = address.substring(i + PROTOCOL_SEPARATOR.length());
        }

        i = address.indexOf('@');
        String hostAndPort;

        if (i != -1) {

            //
            // username and password
            //

            String usernameAndPassword = address.substring(0, i);
            hostAndPort = address.substring(i + 1);

            i = usernameAndPassword.indexOf(':');

            if (i == -1) {

                throw new AddressException("missing password");
            }

            this.username = usernameAndPassword.substring(0, i);
            this.password = usernameAndPassword.substring(i + 1).toCharArray();
        }
        else {

            hostAndPort = address;
        }

        i = hostAndPort.indexOf(':');

        if (i == -1) {

            //
            // no port
            //

            host = hostAndPort.trim();
        }
        else {

            host = hostAndPort.substring(0, i).trim();

            String s = hostAndPort.substring(i + 1);

            try {

                port = Integer.parseInt(s);
            }
            catch(Exception e) {

                throw new AddressException("invalid port \"" + s + "\"");
            }

            if (port <= 0 || port >= 65536) {

                throw new AddressException("port value out of bounds: " + port);
            }
        }


        if (host == null || host.isEmpty()) {

            throw new AddressException("empty host name");
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
