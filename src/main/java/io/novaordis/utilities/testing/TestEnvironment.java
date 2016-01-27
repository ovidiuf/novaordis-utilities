package io.novaordis.utilities.testing;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class TestEnvironment
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TestEnvironment.class);

    // Static --------------------------------------------------------------------------------------

    /**
     * Returns a File to be used as test scratch directory. If the method returns successfully,
     * the test scratch directory exists.
     */
    public static File getTestScratchDirectory() throws Exception
    {
        String s = System.getProperty("test.scratch.directory");

        if (s == null)
        {
            log.error("'test.scratch.directory' not set! Set it and try again.");
            throw new Exception("'test.scratch.directory' not set! Set it and try again.");
        }

        File f = new File(s);

        if (!f.exists())
        {
            // try to create the directory
            if (!f.mkdir())
            {
                log.error("Could not create directory " + f);
                throw new Exception("Could not create directory " + f);
            }
        }

        return f;
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
