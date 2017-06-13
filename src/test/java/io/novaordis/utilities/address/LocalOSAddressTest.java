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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/12/17
 */
public class LocalOSAddressTest extends OSAddressTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // Overrides

    @Test
    @Override
    public void setProtocol() throws Exception {

        LocalOSAddress a = new LocalOSAddress();

        a.setProtocol(LocalOSAddress.PROTOCOL);

        try {
            a.setProtocol("something");
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("cannot"));
        }
    }

    @Test
    public void twoDifferentInstancesAreEqual() throws Exception {

        LocalOSAddress a = new LocalOSAddress();
        LocalOSAddress a2 = new LocalOSAddress();

        assertTrue(a.equals(a2));
        assertTrue(a2.equals(a));
    }

    @Test
    public void hashCodeTest() throws Exception {

        LocalOSAddress a = new LocalOSAddress();

        assertEquals(1, a.hashCode());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    @Override
    protected OSAddress getAddressToTest() throws Exception {

        return new LocalOSAddress();
    }

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
