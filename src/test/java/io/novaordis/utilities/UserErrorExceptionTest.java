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

package io.novaordis.utilities;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserErrorExceptionTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(UserErrorExceptionTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    @Test
    public void constructor() throws Exception {

        try {

            throw new UserErrorException();
        }
        catch (UserErrorException e) {

            assertNull(e.getMessage());
            assertNull(e.getCause());
        }
    }

    @Test
    public void constructor2() throws Exception {

        try {

            throw new UserErrorException("test");
        }
        catch (UserErrorException e) {
            assertEquals("test", e.getMessage());
            assertNull(e.getCause());
        }
    }

    @Test
    public void constructor3() throws Exception {

        try {

            throw new UserErrorException(new RuntimeException());
        }
        catch (UserErrorException e) {

            assertEquals("RuntimeException", e.getMessage());
            assertTrue(e.getCause() instanceof RuntimeException);
        }
    }

    @Test
    public void constructor4() throws Exception {

        try {

            throw new UserErrorException("test", new RuntimeException());
        }
        catch (UserErrorException e) {
            assertEquals("test: RuntimeException", e.getMessage());
            assertTrue(e.getCause() instanceof RuntimeException);
        }
    }

    @Test
    public void ifWrappingACauseDelegateToTheCauseMessage() throws Exception {

        Exception cause = new Exception("something");

        Exception e = new UserErrorException(cause);

        String s = e.getMessage();

        assertEquals("Exception something", s);
    }

    @Test
    public void ifWrappingACauseDelegateToTheCauseMessage_MessageOverride() throws Exception {

        Exception cause = new Exception("something");

        Exception e = new UserErrorException("our own message", cause);

        assertEquals("our own message: Exception something", e.getMessage());
    }

    // message composition rules ---------------------------------------------------------------------------------------

    @Test
    public void messageCompositionRules_NoArgConstructor() throws Exception {

        assertNull(new UserErrorException().getMessage());
    }

    @Test
    public void messageCompositionRules_ExplicitNullMessage() throws Exception {

        assertNull(new UserErrorException((String) null).getMessage());
    }

    @Test
    public void messageCompositionRules_NoCause() throws Exception {

        UserErrorException e = new UserErrorException("test");
        assertEquals("test", e.getMessage());
    }

    @Test
    public void messageCompositionRules_RecursiveCause_NullBottomMostMessage_NullExplicitMessage() throws Exception {

        NoClassDefFoundError ncdfe = new NoClassDefFoundError(null);

        ClassNotFoundException cnfe = new ClassNotFoundException("will be ignored", ncdfe);

        UserErrorException uee = new UserErrorException(cnfe);

        String msg = uee.getMessage();

        log.info(msg);

        assertEquals("NoClassDefFoundError", msg);
    }

    @Test
    public void messageCompositionRules_RecursiveCause_NonNullBottomMostMessage_NullExplicitMessage() throws Exception {

        NoClassDefFoundError ncdfe = new NoClassDefFoundError("some/class/Definition");

        ClassNotFoundException cnfe = new ClassNotFoundException("will be ignored", ncdfe);

        UserErrorException uee = new UserErrorException(cnfe);

        String msg = uee.getMessage();

        log.info(msg);

        assertEquals("NoClassDefFoundError some/class/Definition", msg);
    }

    @Test
    public void messageCompositionRules_RecursiveCause_NullBottomMostMessage_NotNullExplicitMessage() throws Exception {

        NoClassDefFoundError ncdfe = new NoClassDefFoundError(null);

        ClassNotFoundException cnfe = new ClassNotFoundException("will be ignored", ncdfe);

        UserErrorException uee = new UserErrorException("this should surface in the error message", cnfe);

        String msg = uee.getMessage();

        log.info(msg);

        assertEquals("this should surface in the error message: NoClassDefFoundError", msg);
    }

    @Test
    public void messageCompositionRules_RecursiveCause_NonNullBottomMostMessage_NotNullExplicitMessage()
            throws Exception {

        NoClassDefFoundError ncdfe = new NoClassDefFoundError("some/class/Definition");

        ClassNotFoundException cnfe = new ClassNotFoundException("will be ignored", ncdfe);

        UserErrorException uee = new UserErrorException("this should surface in the error message", cnfe);

        String msg = uee.getMessage();

        log.info(msg);

        assertEquals("this should surface in the error message: NoClassDefFoundError some/class/Definition", msg);
    }

    // getOriginalCause() ----------------------------------------------------------------------------------------------

    @Test
    public void getOriginalCause_Null() throws Exception {

        UserErrorException e = new UserErrorException();

        assertNull(e.getOriginalCause());
    }

    @Test
    public void getOriginalCause_OneLevel() throws Exception {

        IllegalStateException e = new IllegalStateException();

        UserErrorException ue = new UserErrorException(e);

        assertEquals(e, ue.getOriginalCause());
    }

    @Test
    public void getOriginalCause_TwoLevels() throws Exception {

        IllegalStateException e = new IllegalStateException();

        IllegalStateException e2 = new IllegalStateException(e);

        UserErrorException ue = new UserErrorException(e2);

        assertEquals(e, ue.getOriginalCause());
    }


    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
