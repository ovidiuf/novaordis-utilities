package com.novaordis.utilities.testing;

import org.apache.log4j.Logger;
import junit.framework.AssertionFailedError;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2009 Ovidiu Feodorov
 */
public class TestBaseTest extends TestBase
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TestBaseTest.class);

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    public void testAssertEqualsByteArray_BothNull() throws Exception
    {
        try
        {
            assertEquals((byte[])null, (byte[])null);
            throw new RuntimeException(
                "the above assertion should fail by trowing an AssertionFailedError");
        }
        catch(AssertionFailedError e)
        {
            log.info("STDOUT: " + e.getMessage());
        }
    }

    public void testAssertEqualsByteArray_FirstNull() throws Exception
    {
        try
        {
            assertEquals(null, new byte[0]);
            throw new RuntimeException(
                "the above assertion should fail by trowing an AssertionFailedError");
        }
        catch(AssertionFailedError e)
        {
            log.info("STDOUT: " + e.getMessage());
        }
    }

    public void testAssertEqualsByteArray_SecondNull() throws Exception
    {
        try
        {
            assertEquals(new byte[0], null);
            throw new RuntimeException(
                "the above assertion should fail by trowing an AssertionFailedError");
        }
        catch(AssertionFailedError e)
        {
            log.info("STDOUT: " + e.getMessage());
        }
    }

    public void testAssertEqualsByteArray_Emtpy() throws Exception
    {
        assertEquals(new byte[0], new byte[0]);
    }

    public void testAssertEqualsByteArray_DifferentLength() throws Exception
    {
        try
        {
            assertEquals(new byte[1], new byte[2]);
            throw new RuntimeException(
                "the above assertion should fail by trowing an AssertionFailedError");
        }
        catch(AssertionFailedError e)
        {
            log.info("STDOUT: " + e.getMessage());
        }
    }

    public void testAssertEqualsByteArray_SameLength_Empty() throws Exception
    {
        assertEquals(new byte[5], new byte[5]);
    }

    public void testAssertEqualsByteArray_SameLength_DifferOnLastByte() throws Exception
    {
        byte[] b = new byte[] { 1, 2, 3 };
        byte[] b2 = new byte[] { 1, 2, 4 };

        try
        {
            assertEquals(b, b2);
            throw new RuntimeException(
                "the above assertion should fail by trowing an AssertionFailedError");
        }
        catch(AssertionFailedError e)
        {
            log.info("STDOUT: " + e.getMessage());
        }
    }

    public void testAssertEqualsByteArray() throws Exception
    {
        byte[] b = new byte[] { 1, 2, 3, 4, 5 };
        byte[] b2 = new byte[] { 1, 2, 3, 4, 5 };

        assertEquals(b, b2);
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
