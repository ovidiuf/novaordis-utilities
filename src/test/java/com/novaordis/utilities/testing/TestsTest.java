package com.novaordis.utilities.testing;

import com.novaordis.utilities.Files;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * @author <a href="mailto:ovidiu@novaordis.com">Ovidiu Feodorov</a>
 *
 * Copyright 2012 Nova Ordis LLC
 */
public class TestsTest extends Assert
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(TestsTest.class);

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    @Test
    public void noTestScratchDirectory()
    {
        String originalTestScratchProperty = System.getProperty("test.scratch.directory");

        if (originalTestScratchProperty != null)
        {
            System.clearProperty("test.scratch.directory");
            assertNull(System.getProperty("test.scratch.directory"));
        }

        try
        {
            Tests.getScratchDirectory();
            fail("should have failed with IllegalStateException");
        }
        catch(IllegalStateException e)
        {
            log.info(e.getMessage());
        }
        finally
        {
            if (originalTestScratchProperty != null)
            {
                System.setProperty("test.scratch.directory", originalTestScratchProperty);
                assertEquals(
                    originalTestScratchProperty, System.getProperty("test.scratch.directory"));
            }
        }
    }

    @Test
    public void testScratchDirectoryNotADirectory()
    {
        String originalTestScratchProperty = System.getProperty("test.scratch.directory");

        if (originalTestScratchProperty != null)
        {
            System.clearProperty("test.scratch.directory");
            assertNull(System.getProperty("test.scratch.directory"));
        }

        try
        {
            System.setProperty("test.scratch.directory",
                               "/it/is/pretty/sure/this/is/not/a/valid/directory/on/this/system");
            Tests.getScratchDirectory();
            fail("should have failed with IllegalStateException");
        }
        catch(IllegalStateException e)
        {
            log.info(e.getMessage());
        }
        finally
        {
            if (originalTestScratchProperty != null)
            {
                System.setProperty("test.scratch.directory", originalTestScratchProperty);
                assertEquals(
                    originalTestScratchProperty, System.getProperty("test.scratch.directory"));
            }
        }
    }

    //
    // createFile() tests --------------------------------------------------------------------------
    //

    @Test
    public void createFile_RelativePath() throws Exception
    {
        File f = Tests.createFile("a/b/c/test.txt", "something");

        assertNotNull(f);
        assertEquals(new File(Tests.getScratchDirectory(), "a/b/c/test.txt"), f);

        String content = Files.read(f);
        assertEquals("something", content);

        Tests.cleanup();

        assertFalse(f.isFile());
    }

    @Test
    public void createFile_AbsolutePath() throws Exception
    {
        File f = Tests.createFile("/f/g/h/test.txt", "something");

        assertNotNull(f);
        assertEquals(new File(Tests.getScratchDirectory(), "f/g/h/test.txt"), f);

        String content = Files.read(f);
        assertEquals("something", content);

        Tests.cleanup();

        assertFalse(f.isFile());
    }

    //
    // cleanup() tests -----------------------------------------------------------------------------
    //

    @Test
    public void cleanup() throws Exception
    {
        File dir = Tests.getScratchDirectory();

        Files.write(new File(dir, "a.txt"), "something");
        Files.write(new File(dir, "a/b/c/d.txt"), "something");

        assertTrue(new File(dir, "a.txt").isFile());
        assertTrue(new File(dir, "a/b/c").isDirectory());
        assertTrue(new File(dir, "a/b/c/d.txt").isFile());

        Tests.cleanup();

        assertFalse(new File(dir, "a.txt").isFile());
        assertFalse(new File(dir, "a").isDirectory());
        assertTrue(dir.isDirectory());
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
