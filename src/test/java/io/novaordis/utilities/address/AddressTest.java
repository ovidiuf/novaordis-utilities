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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public abstract class AddressTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        Address a = getAddressToTest();

        assertNotNull(a.getProtocol());
        assertNotNull(a.getHost());
        Integer port = a.getPort();
        
        if (port != null) {

            assertTrue(port > 0);
            assertTrue(port < 65536);
        }

        String literal = a.getLiteral();
        assertNotNull(literal);
    }

    @Test
    public void equality() throws Exception {

        Address a = getAddressToTest();
        Address a2 = getAddressToTest();

        assertTrue(a.equals(a2));
        assertTrue(a2.equals(a));

        String literal = a.getLiteral();
        String literal2 = a2.getLiteral();
        assertEquals(literal, literal2);
    }

    @Test
    public void nonEquality() throws Exception {

        Address a = getAddressToTest();
        Object o = new Object();

        assertFalse(a.equals(o));
        assertFalse(o.equals(a));
    }

    @Test
    public void equalityAndNull() throws Exception {

        Address a = getAddressToTest();

        final Object nullReference = null;
        assertFalse(a.equals(nullReference));
    }

    @Test
    public void copy() throws Exception {

        Address a = getAddressToTest();

        Address a2 = a.copy();

        assertFalse(a == a2);

        assertTrue(a.equals(a2));
        assertTrue(a2.equals(a));

        String protocol = a.getProtocol();

        if (protocol != null) {

            assertEquals(protocol, a2.getProtocol());
        }

        String host = a.getHost();

        assertEquals(host, a2.getHost());

        Integer port = a.getPort();

        if (port != null) {

            assertEquals(port, a2.getPort());
        }

        String username = a.getUsername();

        if (username != null) {

            assertEquals(username, a2.getUsername());
        }

        char[] password = a.getPassword();

        if (password != null) {

            assertEquals(new String(password), new String(a2.getPassword()));
        }
    }

    @Test
    public void setProtocol() throws Exception {

        String protocol = "some-random-protocol";

        Address a = getAddressToTest();

        assertNotEquals(protocol, a.getProtocol());

        a.setProtocol(protocol);

        assertEquals(protocol, a.getProtocol());
    }

    @Test
    public void setPort() throws Exception {

        Integer port = 54362;

        Address a = getAddressToTest();

        assertNotEquals(port, a.getPort());

        a.setPort(port);

        assertEquals(port, a.getPort());
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Address getAddressToTest() throws Exception;

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
