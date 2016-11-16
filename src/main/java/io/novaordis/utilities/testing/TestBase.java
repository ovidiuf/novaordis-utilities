package io.novaordis.utilities.testing;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * A base class for all tests.
 * 
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class TestBase extends TestCase
{
    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TestBase.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Byte array quality comparison.
     */
    public static void assertEquals(byte[] b, byte[] b2) {

        if (b == null) {
            fail("not equal, first operand is null");
        }

        if (b2 == null) {
            fail("not equal, second operand is null");
        }

        if (b.length != b2.length) {
            fail("lenghts differ (" + b.length + " vs. " + b2.length + ")");
        }

        for(int i = 0; i < b.length; i ++) {
            if (b[i] != b2[i]) {
                fail("arrays differ at byte " + i + " (" + (int)b[i] + " vs. " + (int)b2[i] + ")");
            }
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}



