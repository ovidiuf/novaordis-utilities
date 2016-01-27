package io.novaordis.utilities.testing;

import io.novaordis.utilities.Files;
import org.apache.log4j.Logger;

import java.util.Random;
import java.io.File;

import junit.framework.Assert;

/**
 * Note: this class is deprecated, it's incompatible with the use of @Test annotations. Port
 * the file functionality to a Tests static class.
 *
 * @see Tests
 *
 * A base class for all tests that interact in a way or another with files in the scratch directory
 * and need file cleanup on tear down.
 *
 * Subclassing this guarantees that the scratch directory is left clean on tearDown(). If a specific
 * test *does not* want scratch cleaned after it, must set:
 *
 * <tt>
 *     cleanScratch = false;
 * </tt>
 * 
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
@Deprecated
public class FileTestBase extends TestBase
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(FileTestBase.class);
    private static final Random random = new Random();

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    protected boolean cleanScratch;

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    /**
     * @param filename the file name of the original file, relative to $PROJECT_HOME/test/data.
     *
     * @return the File in the scratch area.
     */
    public static File copyToScratch(String filename)
    {
        File scratch = Tests.getScratchDirectory();
        File src = new File("src/test/resources/data/" + filename);
        Assert.assertTrue(src.isFile());

        File dest = new File(scratch, filename);

        Assert.assertTrue(Files.cp(src, dest));

        return dest;
    }

    /**
     * @return a random name with no extension.
     */
    public static String getRandomFileName(String hint)
    {
        return getRandomFileName(hint, null);
    }

    /**
     * @return a random directory name.
     */
    public static String getRandomDirectoryName()
    {
        return getRandomDirectoryName(null);
    }

    /**
     * @return a random directory name based on hint.
     */
    public static String getRandomDirectoryName(String hint)
    {
        String prefix = "dir-";
        prefix = hint == null ? prefix : prefix + hint;
        return getRandomFileName(prefix, null);
    }

    /**
     * @param extension can be null, in which case the name will use a random extension, or not at
     *        all. DO NOT specify a dot, it will be automatically added.
     */
    public static String getRandomFileName(String hint, String extension)
    {
        String name = hint + "-" + System.currentTimeMillis() + "-" + random.nextLong();

        if (extension != null)
        {
            name = name + "." + extension;
        }

        return name;
    }

    public static String getRandomContent(String hint)
    {
        return hint + "#" + System.currentTimeMillis() + "#" + random.nextLong();
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    protected void setUp() throws Exception
    {
        super.setUp();
        Assert.assertTrue(Tests.getScratchDirectory().isDirectory());
        cleanScratch = true;
    }

    protected void tearDown() throws Exception
    {
        if (cleanScratch)
        {
            Assert.assertTrue(Files.rmdir(Tests.getScratchDirectory(), false));
        }
        else
        {
            log.warn(Tests.getScratchDirectory() + " not cleaned");
        }
        
        super.tearDown();
    }

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
