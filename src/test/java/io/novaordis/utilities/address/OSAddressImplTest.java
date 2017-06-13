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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class OSAddressImplTest extends AddressImplTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // copy mutation ---------------------------------------------------------------------------------------------------

    @Test
    public void copy_Mutation() throws Exception {

        OSAddressImpl a = new OSAddressImpl("test://someuser:somepasswd@somehost:1234");

        OSAddressImpl a2 = a.copy();

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

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected OSAddressImpl getAddressToTest() throws Exception {

        return new OSAddressImpl("test://someuser:somepassword@somehost:1234");
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
