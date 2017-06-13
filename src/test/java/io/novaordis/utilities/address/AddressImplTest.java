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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class AddressImplTest extends AddressTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor_default() throws Exception {

        AddressImpl a = new AddressImpl("testhost");

        assertNull(a.getProtocol());
        assertEquals("testhost", a.getHost());
        assertNull(a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("testhost", a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword() throws Exception {

        AddressImpl a = new AddressImpl("1.2.3.4:8888");

        assertNull(a.getProtocol());
        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort().intValue());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4:8888", a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword_NoPort() throws Exception {

        AddressImpl a = new AddressImpl("1.2.3.4");

        assertNull(a.getProtocol());
        assertEquals("1.2.3.4", a.getHost());
        assertNull(a.getPort());
        assertNull(a.getUsername());
        assertNull(a.getPassword());
        assertEquals("1.2.3.4", a.getLiteral());
    }

    @Test
    public void constructor_NoUsernamePassword_InvalidPort() throws Exception {

        try {

            new AddressImpl("1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_NoUsernamePassword_PortOutOfBounds() throws Exception {

        try {

            new AddressImpl("1.2.3.4:-1");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("port value out of bounds"));
        }
    }

    @Test
    public void constructor_NoUsernamePassword_PortOutOfBounds2() throws Exception {

        try {

            new AddressImpl("1.2.3.4:0");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("port value out of bounds"));
        }
    }


    @Test
    public void constructor_NoUsernamePassword_PortOutOfBounds3() throws Exception {

        try {

            new AddressImpl("1.2.3.4:65536");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("port value out of bounds"));
        }
    }

    @Test
    public void constructor_UsernameAndPassword_InvalidPort() throws Exception {

        try {

            new AddressImpl("admin:admin123@1.2.3.4:blah");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("invalid port"));
        }
    }

    @Test
    public void constructor_UsernameAndPassword_MissingPort() throws Exception {

        try {

            new AddressImpl("admin:admin123@1.2.3.4:");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing port"));
        }
    }

    @Test
    public void constructor_UsernameAndPassword_MissingPort2() throws Exception {

        try {

            new AddressImpl("admin:admin123@1.2.3.4:    ");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing port"));
        }
    }

    @Test
    public void constructor_InvalidAddress_MissingPassword() throws Exception {

        try {

            new AddressImpl("admin@1.2.3.4:2222");
            fail("should have thrown exception");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("missing password"));
        }
    }

    @Test
    public void constructor() throws Exception {

        AddressImpl a = new AddressImpl("admin:admin123@1.2.3.4:8888");

        assertNull(a.getProtocol());
        assertEquals("1.2.3.4", a.getHost());
        assertEquals(8888, a.getPort().intValue());
        assertEquals("admin", a.getUsername());
        assertEquals("admin123", new String(a.getPassword()));
        assertEquals("admin@1.2.3.4:8888", a.getLiteral());
    }

    @Test
    public void constructor_Protocol() throws Exception {

        AddressImpl a = new AddressImpl("jmx://admin:adminpasswd@1.2.3.4:2345");

        assertEquals("jmx", a.getProtocol());
        assertEquals("1.2.3.4", a.getHost());
        assertEquals(2345, a.getPort().intValue());
        assertEquals("adminpasswd", new String(a.getPassword()));
        assertEquals("admin", a.getUsername());
        assertEquals("admin@1.2.3.4:2345", a.getLiteral());
    }

    @Test
    public void constructor_EmptyHost() throws Exception {

        try {

            new AddressImpl("test://:2345");
            fail("should have thrown exceptions");
        }
        catch(AddressException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("empty host name"));
        }
    }

    @Test
    public void constructor_Components() throws Exception {

        AddressImpl a = new AddressImpl("test", "someuser", "somepassword", "somehost", 7777);

        assertEquals("test", a.getProtocol());
        assertEquals("someuser", a.getUsername());
        assertEquals("somepassword", new String(a.getPassword()));
        assertEquals("somehost", a.getHost());
        assertEquals(7777, a.getPort().intValue());
    }

    // equals() --------------------------------------------------------------------------------------------------------

    @Test
    public void testEquals() throws Exception {

        AddressImpl a = new AddressImpl("test://admin:passwd1@somehost:1234");
        AddressImpl a2 = new AddressImpl("test://admin:passwd2@somehost:1234");

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void testEquals2() throws Exception {

        AddressImpl a = new AddressImpl("test://somehost");
        AddressImpl a2 = new AddressImpl("test://somehost");

        assertEquals(a, a2);
        assertEquals(a2, a);
    }

    @Test
    public void testEquals3() throws Exception {

        AddressImpl a = new AddressImpl("somehost");

        //noinspection ObjectEqualsNull
        assertFalse(a.equals(null));
    }

    @Test
    public void nonEqualityOnNullProtocol() throws Exception {

        AddressImpl a = new AddressImpl("1.2.3.4");
        AddressImpl a2 = new AddressImpl("1.2.3.4");

        assertFalse(a.equals(a2));
        assertFalse(a2.equals(a));
    }

    // hashCode() ------------------------------------------------------------------------------------------------------

    @Test
    public void testHashCode() throws Exception {

        AddressImpl a = new AddressImpl("test://admin:passwd1@somehost:1234");
        AddressImpl a2 = new AddressImpl("test://admin:passwd2@somehost:1235");

        assertTrue(a != a2);
    }

    // copy mutation ---------------------------------------------------------------------------------------------------

    @Test
    public void copy_Mutation() throws Exception {

        AddressImpl a = new AddressImpl("test://someuser:somepasswd@somehost:1234");

        AddressImpl a2 = a.copy();

        assertEquals("test", a2.getProtocol());
        assertEquals("someuser", a2.getUsername());
        assertEquals("somepasswd", new String(a2.getPassword()));
        assertEquals("somehost", a2.getHost());
        assertEquals(1234, a2.getPort().intValue());


        char[] p = a2.getPassword();
        p[0] = 'd';

        assertEquals("domepasswd", new String(a2.getPassword()));
        assertEquals("somepasswd", new String(a.getPassword()));
    }

    // getLiteral() ----------------------------------------------------------------------------------------------------

    @Test
    public void getLiteral_PortSpecifiedInOriginalLiteral() throws Exception {

        AddressImpl a = new AddressImpl("test://someuser:somepasswd@testhost:1234");
        String literal = a.getLiteral();
        assertEquals("someuser@testhost:1234", literal);
    }

    @Test
    public void getLiteral_PortSpecifiedInOriginalLiteral2() throws Exception {

        AddressImpl a = new AddressImpl("test://testhost:1234");
        String literal = a.getLiteral();
        assertEquals("testhost:1234", literal);
    }

    @Test
    public void getLiteral_PortSpecifiedInOriginalLiteral3() throws Exception {

        AddressImpl a = new AddressImpl("testhost:1234");
        String literal = a.getLiteral();
        assertEquals("testhost:1234", literal);
    }

    @Test
    public void getLiteral_PortNotSpecifiedInOriginalLiteral() throws Exception {

        AddressImpl a = new AddressImpl("test://someuser:somepasswd@testhost");
        String literal = a.getLiteral();
        assertEquals("someuser@testhost", literal);
    }

    @Test
    public void getLiteral_PortNotSpecifiedInOriginalLiteral2() throws Exception {

        AddressImpl a = new AddressImpl("test://testhost");
        String literal = a.getLiteral();
        assertEquals("testhost", literal);
    }

    @Test
    public void getLiteral_PortNotSpecifiedInOriginalLiteral3() throws Exception {

        AddressImpl a = new AddressImpl("testhost");
        String literal = a.getLiteral();
        assertEquals("testhost", literal);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected AddressImpl getAddressToTest() throws Exception {

        return new AddressImpl("test://someuser:somepassword@somehost:1234");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
