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

package io.novaordis.utilities.logging.log4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public class Log4jLevelTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void toLiteral_OFF() {

        assertEquals("OFF", Log4jLevel.OFF.toLiteral());
    }

    @Test
    public void toLiteral_FATAL() {

        assertEquals("FATAL", Log4jLevel.FATAL.toLiteral());
    }

    @Test
    public void toLiteral_ERROR() {

        assertEquals("ERROR", Log4jLevel.ERROR.toLiteral());
    }

    @Test
    public void toLiteral_WARN() {

        assertEquals("WARN", Log4jLevel.WARN.toLiteral());
    }

    @Test
    public void toLiteral_INFO() {

        assertEquals("INFO", Log4jLevel.INFO.toLiteral());
    }

    @Test
    public void toLiteral_DEBUG() {

        assertEquals("DEBUG", Log4jLevel.DEBUG.toLiteral());
    }

    @Test
    public void toLiteral_TRACE() {

        assertEquals("TRACE", Log4jLevel.TRACE.toLiteral());
    }

    @Test
    public void toLiteral_ALL() {

        assertEquals("ALL", Log4jLevel.ALL.toLiteral());
    }

    // fromLiteral() ---------------------------------------------------------------------------------------------------

    @Test
    public void fromLiteral() throws Exception {

        for(Log4jLevel l: Log4jLevel.values()) {

            String literal = l.toLiteral();

            assertEquals(l, Log4jLevel.fromLiteral(literal));
        }
    }

    @Test
    public void fromLiteral_InvalidVAlue() throws Exception {

        assertNull(Log4jLevel.fromLiteral("invalid value"));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
