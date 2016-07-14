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

package io.novaordis.utilities.time;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/13/16
 */
public class DateFormatUtilTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(DateFormatUtilTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // hasTimeOffset() -------------------------------------------------------------------------------------------------

    @Test
    public void hasTimeOffset_ItDoesNot() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss");

        assertFalse(DateFormatUtil.hasTimeOffset(f));

        log.debug(".");
    }

    @Test
    public void hasTimeOffset_ItDoes() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");

        assertTrue(DateFormatUtil.hasTimeOffset(f));
    }

    @Test
    public void hasTimeOffset_ItDoes2() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy Z");

        assertTrue(DateFormatUtil.hasTimeOffset(f));
    }

    @Test
    public void hasTimeOffset_ItDoes3() throws Exception {

        DateFormat f = new SimpleDateFormat("MM/dd/yy Z");
        f.setTimeZone(TimeZone.getTimeZone("GMT"));

        assertTrue(DateFormatUtil.hasTimeOffset(f));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
