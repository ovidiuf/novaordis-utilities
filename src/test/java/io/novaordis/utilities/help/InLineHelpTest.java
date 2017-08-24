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

package io.novaordis.utilities.help;

import io.novaordis.utilities.UserErrorException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/24/17
 */
public class InLineHelpTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void get_NoHelpFile_ApplicationNameSpecified() throws Exception {

        try {

            InLineHelp.get("something", "i-am-sure-there-is-no-such-file-on-classpath.txt");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.matches("^no.*file found on the classpath.*"));
            assertTrue(msg.contains("i-am-sure-there-is-no-such-file-on-classpath.txt"));
            assertTrue(msg.contains("something"));
        }
    }

    @Test
    public void get_NoHelpFile_ApplicationNameNotSpecified() throws Exception {

        try {

            InLineHelp.get(null, "i-am-sure-there-is-no-such-file-on-classpath.txt");
            fail("should have thrown exception");
        }
        catch(UserErrorException e) {

            String msg = e.getMessage();
            assertTrue(msg.matches("^no.*file found on the classpath.*"));
            assertTrue(msg.contains("i-am-sure-there-is-no-such-file-on-classpath.txt"));
            assertTrue(msg.contains("application"));
        }
    }

    @Test
    public void get() throws Exception {

        String content = InLineHelp.get("something", "test-help.txt");
        assertTrue(content.contains("This is test in-line help."));
    }

    @Test
    public void get_DefaultHelpFileName() throws Exception {

        String content = InLineHelp.get("something");
        assertTrue(content.contains("synthetic help in help.txt"));
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
