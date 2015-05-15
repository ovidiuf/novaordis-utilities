package com.novaordis.utilities.testing;

import com.novaordis.utilities.Files;

import java.io.File;

/**
 * Replacement for FileTestBase.
 *
 * Use Tests.createFile(...) functions to create files and directories in the scratch area and
 * Tests.cleanup() to clean the area - consider wrapping the call into an @After or @AfterClass
 * annotated method.
 *
 * Example:
 *
 * <tt>
 *
 *     @AfterClass
 *     public static void cleanup() throws Exception
 *     {
 *         Tests.cleanup();
 *     }
 *
 * </tt>
 *
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class Tests
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    /**
     * @throws IllegalStateException if "test.scratch.directory" is not defined, or it points
     *         to a non-existent/non-writable directory.
     */
    public static File getScratchDirectory()
    {
        String s = System.getProperty("test.scratch.directory");

        if (s == null)
        {
            throw new IllegalStateException("'test.scratch.directory' not defined");
        }

        File dir = new File(s);

        if (!dir.isDirectory())
        {
            throw new IllegalStateException("'test.scratch.directory' (" + dir + ") does not exist or is not a directory");
        }

        if (!dir.canWrite())
        {
            throw new IllegalStateException("'test.scratch.directory' (" + dir + ") cannot be written");
        }

        return dir;
    }

    public static File getScratchDir()
    {
        return getScratchDirectory();
    }

    /**
     * Recursively removes the content of the "test.scratch.directory"
     * (but not the directory itself).
     *
     * @throws Exception if anything prevents it to do a proper job.
     */
    public static void cleanup() throws Exception
    {
        File dir = Tests.getScratchDirectory();

        if (!Files.rmdir(dir, false))
        {
            throw new Exception("failed to clean " + dir);
        }
    }

    /**
     * Creates a file relative to 'test.scratch.directory'. It does not matter if the path is
     * relative or absolute, it is appended to 'test.scratch.directory' anyway. Will create
     * intermediary directories if they don't exist.
     *
     * Note that the files won't be automatically deleted when the test completes, consider using
     * a @After or @AfterClass JUnit annotation on a method that calls Tests.cleanup();
     *
     * @return the File that was created.
     */
    public static File createFile(String fileName, String content) throws Exception
    {
        File scratchDir = getScratchDirectory();
        File target = new File(scratchDir, fileName);
        if (Files.write(target, content, false))
        {
            return target;
        }
        else
        {
            throw new Exception("failed to write " + target);
        }
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    private Tests()
    {
    }

    // Inner classes -------------------------------------------------------------------------------
}
