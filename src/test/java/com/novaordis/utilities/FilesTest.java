package com.novaordis.utilities;

import com.novaordis.utilities.testing.FileTestBase;
import com.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class FilesTest extends FileTestBase
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(FilesTest.class);

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    public void testIsEmpty_NoSuchDirectory() throws Exception
    {
        File noSuchDir = new File(Tests.getScratchDirectory(), getRandomFileName("testIsEmpty"));
        assertFalse(noSuchDir.isDirectory());
        assertFalse(noSuchDir.isFile());

        assertFalse(Files.isEmpty(noSuchDir));
    }

    public void testIsEmpty_ExistingFile() throws Exception
    {
        File file = new File(Tests.getScratchDirectory(), getRandomFileName("testIsEmpty", "txt"));
        assertTrue(file.createNewFile());
        assertTrue(file.isFile());

        assertFalse(Files.isEmpty(file));
    }

    public void testIsEmpty_EmptyDirectory() throws Exception
    {
        File dir = new File(Tests.getScratchDirectory(), getRandomFileName("testIsEmptyDir"));
        assertFalse(dir.isDirectory());
        assertTrue(dir.mkdir());

        assertTrue(Files.isEmpty(dir));
    }

    public void testIsEmpty_NonEmptyDirectory() throws Exception
    {
        File dir =
            new File(Tests.getScratchDirectory(), getRandomFileName("testIsEmptyDir") + "/content");
        assertTrue(dir.mkdirs());

        assertFalse(Files.isEmpty(dir.getParentFile()));
    }

    public void testIsEmpty_NonEmptyDirectory2() throws Exception
    {
        File dir = new File(Tests.getScratchDirectory(), getRandomFileName("testIsEmptyDir"));
        assertFalse(dir.isDirectory());
        assertTrue(dir.mkdir());
        assertTrue(new File(dir, "content.txt").createNewFile());

        assertFalse(Files.isEmpty(dir));
    }

    public void testWrite_DestinationIsDirectory() throws Exception
    {
        assertFalse(Files.write(Tests.getScratchDirectory(), "", true));
    }

    //
    // Files.write() tests -------------------------------------------------------------------------
    //

    public void testWrite() throws Exception
    {
        File target = new File(Tests.getScratchDirectory(), getRandomFileName("tw", "txt"));
        assertFalse(target.isFile());

        String content = getRandomContent("tw");
        assertTrue(Files.write(target, content, true));

        String s = Files.read(target);
        assertEquals(content, s);
    }

    public void testWrite_EnclosingDirectoryNotExistent() throws Exception
    {
        File scratch = Tests.getScratchDirectory();

        File enclosingDir = new File(scratch, "a/b/c");
        assertFalse(enclosingDir.isDirectory());

        File target = new File(enclosingDir, getRandomFileName("tw", "txt"));
        assertFalse(target.isFile());

        String content = getRandomContent("tw");

        assertTrue(Files.write(target, content, true));

        assertTrue(enclosingDir.isDirectory());

        String s = Files.read(target);
        assertEquals(content, s);
    }

    //
    // Files.read() tests --------------------------------------------------------------------------
    //

    public void testRead() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("", s);
    }

    public void testRead2() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("\n", s);
    }

    public void testRead3() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("a");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("a", s);
    }

    public void testRead4() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("a\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("a\n", s);
    }

    public void testRead5() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("\n\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("\n\n", s);
    }

    public void testRead6() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("a\n\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("a\n\n", s);
    }

    public void testRead7() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abc");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abc", s);
    }

    public void testRead8() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abcd");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abcd", s);
    }

    public void testRead9() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abcde");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abcde", s);
    }

    public void testRead10() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("ab\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("ab\n", s);
    }

    public void testRead11() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abc\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abc\n", s);
    }

    public void testRead12() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abcd\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abcd\n", s);
    }

    public void testRead13() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abcdef\nab\n\n\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abcdef\nab\n\n\n", s);
    }

    public void testRead14() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("read", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abcdabcdabcd\n");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f, 4);
        assertEquals("abcdabcdabcd\n", s);
    }

    public void testRead_SingleLineNoCR() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("singleline", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.print("abc");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("abc", s);
    }

    public void testRead_SingleLineWithCR() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("singleline-with-cr", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.println("abc");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("abc\n", s);
    }

    public void testRead_SingleLineWithCR2() throws Exception
    {
        File f = new File(Tests.getScratchDirectory(), getRandomFileName("singleline-with-cr", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.println("abc");
            pw.println("efg");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals("abc\nefg\n", s);
    }


    public void testRead_Multiline() throws Exception
    {
        String content = "abc\nxyz\n123\n";

        File f = new File(Tests.getScratchDirectory(), getRandomFileName("multiline", "txt"));

        FileWriter fw = null;
        PrintWriter pw = null;

        try
        {
            fw = new FileWriter(f);
            pw = new PrintWriter(fw);

            pw.println("abc");
            pw.println("xyz");
            pw.println("123");
            pw.flush();
        }
        finally
        {
            if (pw != null)
            {
                pw.close();
            }

            if (fw != null)
            {
                fw.close();
            }
        }

        String s = Files.read(f);
        assertEquals(content, s);
    }

    public void testRmdir_File() throws Exception
    {
        File file = new File(Tests.getScratchDirectory(), getRandomFileName("testrm", "txt"));
        assertTrue(file.createNewFile());
        assertTrue(file.isFile());

        assertFalse(Files.rmdir(file, false));
        assertTrue(file.isFile());

        assertFalse(Files.rmdir(file, true));
        assertTrue(file.isFile());
    }

    public void testRmdir_IncludingRoot() throws Exception
    {
        File root = new File(Tests.getScratchDirectory(), getRandomFileName("testrmdir-root"));
        assertTrue(root.mkdir());
        File file1 = new File(root, "file1.txt");
        assertTrue(file1.createNewFile());
        File dir1 = new File(root, "dir1");
        assertTrue(dir1.mkdir());
        File file2 = new File(dir1, "file2.txt");
        assertTrue(file2.createNewFile());
        File dir2 = new File(dir1, "dir2");
        assertTrue(dir2.mkdir());
        File file3 = new File(dir2, "file3.txt");
        assertTrue(file3.createNewFile());
        File dir3 = new File(dir2, "dir3");
        assertTrue(dir3.mkdir());

        assertTrue(dir3.isDirectory());
        assertTrue(file3.isFile());

        assertTrue(Files.rmdir(root, true));

        // make sure scratch is empty
        assertTrue(Files.isEmpty(Tests.getScratchDirectory()));
    }

    public void testRmdir_WithoutRoot() throws Exception
    {
        File root = new File(Tests.getScratchDirectory(), getRandomFileName("testrmdir-root"));
        assertTrue(root.mkdir());
        File file1 = new File(root, "file1.txt");
        assertTrue(file1.createNewFile());
        File dir1 = new File(root, "dir1");
        assertTrue(dir1.mkdir());
        File file2 = new File(dir1, "file2.txt");
        assertTrue(file2.createNewFile());
        File dir2 = new File(dir1, "dir2");
        assertTrue(dir2.mkdir());
        File file3 = new File(dir2, "file3.txt");
        assertTrue(file3.createNewFile());
        File dir3 = new File(dir2, "dir3");
        assertTrue(dir3.mkdir());

        assertTrue(dir3.isDirectory());
        assertTrue(file3.isFile());

        assertTrue(Files.rmdir(root, false));

        assertTrue(root.isDirectory());

       // make sure root is empty
        assertTrue(Files.isEmpty(root));
    }

    public void testRmdir_RootInclusive() throws Exception
    {
        File scratchDir = Tests.getScratchDirectory();

        File root = new File(scratchDir, getRandomFileName("root"));

        File dir1 = new File(root, "dir1");
        File dir2 = new File(root, "dir2");
        File dir3 = new File(root, "dir3");

        assertTrue(dir1.mkdirs());
        assertTrue(dir2.mkdir());
        assertTrue(dir3.mkdir());

        File dir4 = new File(dir1, "dir4");
        File dir5 = new File(dir2, "dir5");

        assertTrue(dir4.mkdir());
        assertTrue(dir5.mkdir());

        File file1 = new File(dir4, "file1.txt");
        File file2 = new File(dir5, "file2.txt");

        assertTrue(Files.write(file1, "something", false));
        assertTrue(Files.write(file2, "something else", false));

        assertTrue(Files.rmdir(root, true));

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(dir5.exists());
        assertFalse(dir4.exists());
        assertFalse(dir3.exists());
        assertFalse(dir2.exists());
        assertFalse(dir1.exists());
        assertFalse(root.exists());
    }

    public void testRmdir_ButNoRoot() throws Exception
    {
        File scratchDir = Tests.getScratchDirectory();

        File root = new File(scratchDir, getRandomFileName("root"));

        File dir1 = new File(root, "dir1");
        File dir2 = new File(root, "dir2");
        File dir3 = new File(root, "dir3");

        assertTrue(dir1.mkdirs());
        assertTrue(dir2.mkdir());
        assertTrue(dir3.mkdir());

        File dir4 = new File(dir1, "dir4");
        File dir5 = new File(dir2, "dir5");

        assertTrue(dir4.mkdir());
        assertTrue(dir5.mkdir());

        File file1 = new File(dir4, "file1.txt");
        File file2 = new File(dir5, "file2.txt");

        assertTrue(Files.write(file1, "something", false));
        assertTrue(Files.write(file2, "something else", false));

        assertTrue(Files.rmdir(root, false));

        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertFalse(dir5.exists());
        assertFalse(dir4.exists());
        assertFalse(dir3.exists());
        assertFalse(dir2.exists());
        assertFalse(dir1.exists());
        assertTrue(root.exists());

        assertTrue(root.delete());
    }
    
    public void testCp_DestinationFileExists() throws Exception
    {
        File src = new File(Tests.getScratchDirectory(), getRandomFileName("testcp-src-1", "txt"));
        String srcContent = getRandomContent("testcp SOURCE random content");
        assertTrue(Files.write(src, srcContent));

        File dest = new File(Tests.getScratchDirectory(), getRandomFileName("testcp-dest", "txt"));
        String destContent = getRandomContent("testcp DESTINATION random content");
        assertTrue(Files.write(dest, destContent));

        assertFalse(srcContent.equals(destContent));

        assertTrue(Files.cp(src, dest));

        String copied = Files.read(dest);
        assertEquals(srcContent, copied);
    }

    public void testCp_DestinationIsDirectory() throws Exception
    {
        File src = new File(Tests.getScratchDirectory(), getRandomFileName("testcp-src-2", "txt"));
        String srcContent = getRandomContent("testcp SOURCE random content");
        assertTrue(Files.write(src, srcContent));

        File dest = new File(Tests.getScratchDirectory(), getRandomFileName("testcp-dest-dir"));
        assertTrue(dest.mkdir());
        assertTrue(Files.isEmpty(dest));

        assertTrue(Files.cp(src, dest));

        File destFile = new File(dest, src.getName());
        assertTrue(destFile.exists());
        String copied = Files.read(destFile);
        assertEquals(srcContent, copied);
    }

    public void testCp_DestinationIsNothing() throws Exception
    {
        File src = new File(Tests.getScratchDirectory(), getRandomFileName("testcp-src-3", "txt"));
        String srcContent = getRandomContent("testcp SOURCE random content");
        assertTrue(Files.write(src, srcContent));

        File dest = new File(Tests.getScratchDirectory(), "dir1/dir2/dir3/file.txt");
        assertFalse(dest.exists());

        assertTrue(Files.cp(src, dest));

        assertTrue(dest.exists());
        String copied = Files.read(dest);
        assertEquals(srcContent, copied);
    }

    public void testTokenize_InvalidAbsolutePath() throws Exception
    {
        try
        {
            Files.tokenize(new File("/.."));
            fail("should've failed");
        }
        catch(IllegalArgumentException e)
        {
            log.debug(e.getMessage());
        }
    }

    public void testTokenize_InvalidAbsolutePath2() throws Exception
    {
        try
        {
            Files.tokenize(new File("/./.."));
            fail("should've failed");
        }
        catch(IllegalArgumentException e)
        {
            log.debug(e.getMessage());
        }
    }

    public void testTokenize_InvalidAbsolutePath3() throws Exception
    {
        try
        {
            Files.tokenize(new File("/a/../.."));
            fail("should've failed");
        }
        catch(IllegalArgumentException e)
        {
            log.debug(e.getMessage());
        }
    }

    public void testTokenize_InvalidAbsolutePath4() throws Exception
    {
        try
        {
            Files.tokenize(new File("/a/b/c/../../././.././.."));
            fail("should've failed");
        }
        catch(IllegalArgumentException e)
        {
            log.debug(e.getMessage());
        }
    }

    public void testTokenize_Root() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/"));
        assertTrue(toks.isEmpty());
    }

    public void testTokenize_Root2() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/."));
        assertTrue(toks.isEmpty());
    }

    public void testTokenize_Root3() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/./"));
        assertTrue(toks.isEmpty());
    }

    public void testTokenize_Root4() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/./a/.."));
        assertTrue(toks.isEmpty());
    }

    public void testTokenize_ValidAbsolutePath() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/a/b/c/"));

        assertEquals(3, toks.size());
        assertEquals("a", toks.get(0));
        assertEquals("b", toks.get(1));
        assertEquals("c", toks.get(2));
    }

    public void testTokenize_ValidAbsolutePath2() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/a/b/c"));

        assertEquals(3, toks.size());
        assertEquals("a", toks.get(0));
        assertEquals("b", toks.get(1));
        assertEquals("c", toks.get(2));
    }

    public void testTokenize_ValidAbsolutePath3() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/a/b/c/../.././././.."));
        assertTrue(toks.isEmpty());
    }

    public void testTokenize_ValidAbsolutePath4() throws Exception
    {
        List<String> toks = Files.tokenize(new File("/a/b/c/.././././.."));
        assertEquals(1, toks.size());
        assertEquals("a", toks.get(0));
    }

    public void testTokenize_ValidRelativePath() throws Exception
    {
        List<String> toks = Files.tokenize(new File("."));

        // at this point, toks should contain the "user.dir" content.

        String userDir = System.getProperty("user.dir");

        String myPath = "";
        for(Iterator<String> i = toks.iterator(); i.hasNext(); )
        {
            myPath += i.next();

            if (i.hasNext())
            {
                myPath += File.separator;
            }
        }

        log.debug("userDir: " + userDir);
        log.debug("myPath:  " + myPath);
        assertTrue(userDir.indexOf(myPath) != -1);
    }

    public void testTokenize_InvalidValidRelativePath() throws Exception
    {
        // we count the number of components of "user.dir"

        String userDir = System.getProperty("user.dir");
        File crtFile = new File(userDir);

        int count = 0;
        while(crtFile.getParentFile() != null)
        {
            count ++;
            crtFile = crtFile.getParentFile();
        }

        // we create a relative path that exceeds the count with one

        String invalidRelPath = "";
        for(int i = 0; i <= count; i++)
        {
            invalidRelPath += "..";

            if (i < count)
            {
               invalidRelPath += "/";
            }
        }

        try
        {
            Files.tokenize(new File(invalidRelPath));
            fail("should've failed");
        }
        catch(IllegalArgumentException e)
        {
            log.debug(e.getMessage());
        }
    }

    public void testTokenize_ValidUpwardsRelativePath() throws Exception
    {
        // we count the number of components of "user.dir"

        String userDir = System.getProperty("user.dir");
        File crtFile = new File(userDir);

        int count = 0;
        while(crtFile.getParentFile() != null)
        {
            count ++;
            crtFile = crtFile.getParentFile();
        }

        // we create a relative path containing the exact number of ".."

        String invalidRelPath = "";
        for(int i = 0; i < count; i++)
        {
            invalidRelPath += "..";

            if (i < count - 1)
            {
               invalidRelPath += "/";
            }
        }

        List<String> toks = Files.tokenize(new File(invalidRelPath));

        // we should go to the absolute root
        assertTrue(toks.isEmpty());
    }

    public void testTokenize_ValidRelativePath2() throws Exception
    {
        List<String> toks = Files.tokenize(new File("a/b/c"));

        // we count the number of components of "user.dir"

        String userDir = System.getProperty("user.dir");
        File crtFile = new File(userDir);

        int count = 0;
        while(crtFile.getParentFile() != null)
        {
            count ++;
            crtFile = crtFile.getParentFile();
        }

        assertEquals(count + 3, toks.size());
        assertEquals("a", toks.get(count));
        assertEquals("b", toks.get(count + 1));
        assertEquals("c", toks.get(count + 2));
    }

    public void testTokenize_ValidRelativePath3() throws Exception
    {
        List<String> toks = Files.tokenize(new File("a/b/./c"));

        // we count the number of components of "user.dir"

        String userDir = System.getProperty("user.dir");
        File crtFile = new File(userDir);

        int count = 0;
        while(crtFile.getParentFile() != null)
        {
            count ++;
            crtFile = crtFile.getParentFile();
        }

        assertEquals(count + 3, toks.size());
        assertEquals("a", toks.get(count));
        assertEquals("b", toks.get(count + 1));
        assertEquals("c", toks.get(count + 2));
    }

    public void testTokenize_ValidRelativePath4() throws Exception
    {
        List<String> toks = Files.tokenize(new File("a/b/../c"));

        // we count the number of components of "user.dir"

        String userDir = System.getProperty("user.dir");
        File crtFile = new File(userDir);

        int count = 0;
        while(crtFile.getParentFile() != null)
        {
            count ++;
            crtFile = crtFile.getParentFile();
        }

        assertEquals(count + 2, toks.size());
        assertEquals("a", toks.get(count));
        assertEquals("c", toks.get(count + 1));
    }

    public void testTokenize_ValidRelativePath5() throws Exception
    {
        List<String> toks = Files.tokenize(new File("a/"));

        // we count the number of components of "user.dir"

        String userDir = System.getProperty("user.dir");
        File crtFile = new File(userDir);

        int count = 0;
        while(crtFile.getParentFile() != null)
        {
            count ++;
            crtFile = crtFile.getParentFile();
        }

        assertEquals(count + 1, toks.size());
        assertEquals("a", toks.get(count));
    }

    public void testRelativePath_NoRelationship() throws Exception
    {
        assertNull(Files.relativePath(new File("/a/b/c"), new File("/d")));
    }

    public void testRelativePath_SameFile() throws Exception
    {
        assertEquals("", Files.relativePath(new File("/a/b/c"), new File("/a/b/c")));
    }

    public void testRelativePath_SameFile2() throws Exception
    {
        assertEquals("", Files.relativePath(new File("."), new File(".")));
    }

    public void testRelativePath_SameFile3() throws Exception
    {
        assertEquals("", Files.relativePath(new File("a/b/c"), new File("a/b/c")));
    }

    public void testRelativePath_SameFile4() throws Exception
    {
        assertEquals("", Files.relativePath(new File("a/../b/"), new File("b")));
    }

    public void testRelativePath_SameFile5() throws Exception
    {
        assertEquals("", Files.relativePath(new File("a//b/c"), new File("a/./b/./c")));
    }

    public void testRelativePath_NotADescendant() throws Exception
    {
        assertNull(Files.relativePath(new File("/a/b/c"), new File("/a/b/d")));
    }

    public void testRelativePath_NotADescendant3() throws Exception
    {
        assertNull(Files.relativePath(new File("/a/b/c"), new File("/a/b")));
    }

    public void testMkdirOneElement() throws Exception
    {
        File scratch = Tests.getScratchDir();
        String lementOne = getRandomFileName("blah");

        File f = new File(scratch, lementOne);

        assertFalse(f.isFile());
        assertFalse(f.isDirectory());

        assertTrue(Files.mkdir(f));

        assertFalse(f.isFile());
        assertTrue(f.isDirectory());
    }

    public void testMkdirTwoElements() throws Exception
    {
        File scratch = Tests.getScratchDir();
        String elementOne = getRandomFileName("blah");
        String elementTwo = getRandomFileName("bluh");

        File f = new File(scratch, elementOne);
        File f2 = new File(scratch, elementOne + "/" + elementTwo);

        assertFalse(f.isFile());
        assertFalse(f.isDirectory());

        assertTrue(Files.mkdir(f2));

        assertFalse(f.isFile());
        assertTrue(f.isDirectory());
        assertFalse(f2.isFile());
        assertTrue(f2.isDirectory());
    }

    public void testMkdirElementsAlreadyExisting() throws Exception
    {
        File scratch = Tests.getScratchDir();
        String elementOne = getRandomFileName("blah");
        String elementTwo = getRandomFileName("bluh");

        File f = new File(scratch, elementOne);
        File f2 = new File(scratch, elementOne + "/" + elementTwo);

        assertFalse(f.isFile());
        assertFalse(f.isDirectory());

        assertTrue(Files.mkdir(f));

        assertFalse(f.isFile());
        assertTrue(f.isDirectory());
        assertFalse(f2.isFile());
        assertFalse(f2.isDirectory());

        assertTrue(Files.mkdir(f2));

        assertFalse(f.isFile());
        assertTrue(f.isDirectory());
        assertFalse(f2.isFile());
        assertTrue(f2.isDirectory());
    }

    public void testGetExtension_NullPath() throws Exception
    {
        try
        {
            Files.getExtension(null);
            fail("should have failed with NPE");
        }
        catch(NullPointerException e)
        {
            log.debug(">>> " + e.getMessage());
        }
    }

    public void testGetExtension_EmptyPath() throws Exception
    {
        assertNull(Files.getExtension(""));
    }

    public void testGetExtension_NoExtensionFileName() throws Exception
    {
        assertNull(Files.getExtension("a"));
    }

    public void testGetExtension_EmptyExtensionFileName() throws Exception
    {
        assertEquals("", Files.getExtension("a."));
    }

    public void testGetExtension_ValidExtensionFileName() throws Exception
    {
        assertEquals("b", Files.getExtension("a.b"));
    }

    public void testGetExtension_NoExtensionFileNameWithinAPath() throws Exception
    {
        assertNull(Files.getExtension("a/b"));
    }

    public void testGetExtension_EmptyExtensionFileNameWithinAPath() throws Exception
    {
        assertEquals("", Files.getExtension("a/b."));
    }

    public void testGetExtension_ValidExtensionFileNameWithinAPath() throws Exception
    {
        assertEquals("c", Files.getExtension("a/b.c"));
    }

    public void testGetExtension_LastDotInNonTerminalPathComponent() throws Exception
    {
        assertNull(Files.getExtension("a/b.c/d"));
    }


    // alternative implementation of relativePath() ------------------------------------------------

    public void testRelativePathExperimental() throws Exception
    {
        File ancestor = new File("C:\\a");
        File descendent = new File("C:\\a\\b");

        String path = Files.relativePathExperimental(ancestor, descendent);
        assertEquals("b", path);
    }

    public void testRelativePathExperimental_2() throws Exception
    {
        File ancestor = new File("C:\\a");
        File descendent = new File("C:\\a\\b\\c");

        String path = Files.relativePathExperimental(ancestor, descendent);
        assertEquals("b/c", path);
    }

    public void testRelativePathExperimental_3() throws Exception
    {
        File ancestor = new File("C:\\");
        File descendent = new File("C:\\a\\b\\c");

        String path = Files.relativePathExperimental(ancestor, descendent);
        assertEquals("a/b/c", path);
    }

    public void testRelativePathExperimental_NoRelationshipWhatsoever() throws Exception
    {
        File ancestor = new File("B:\\");
        File descendent = new File("C:\\a\\b\\c");

        assertNull(Files.relativePathExperimental(ancestor, descendent));
    }

    // writeSnapshot -------------------------------------------------------------------------------

    /**
     * If the system property that specifies the snapshot directory is not set, writeSnapshot()
     * should return null and warn.
     */
    public void testWriteSnapshot_NoSystemPropertySet() throws Exception
    {
        assertNull(Files.writeSnapshot("doesntmatter", 3, "doesntmatter"));
    }

    public void testWriteSnapshot_NullDirectory() throws Exception
    {
        assertNull(Files.writeSnapshot(null, "doesntmatter", 3, "doesntmatter"));
    }

    public void testWriteSnapshot_NonExistentDirectory() throws Exception
    {
        File d = new File(Tests.getScratchDir(), getRandomDirectoryName("snapshot"));
        assertFalse(d.isDirectory());

        File d2 = new File(d, "layer2");
        assertFalse(d2.isDirectory());

        long t0 = System.currentTimeMillis();

        String s = Files.writeSnapshot(d2, "blah", 3, "bluh");

        long t1 = System.currentTimeMillis();

        assertTrue(d2.isDirectory());

        File[] files = d2.listFiles();
        assertEquals(1, files.length);

        File f = files[0];

        assertEquals(s, f.getAbsolutePath());

        String name = f.getName();
        assertTrue(name.startsWith("blah."));

        name = name.substring("blah.".length());

        long lname = Long.parseLong(name);
        assertTrue(lname >= t0);
        assertTrue(lname <= t1);

        assertEquals("bluh", Files.read(f));
    }

    public void testWriteSnapshot_KeepLast3() throws Exception
    {
        File d = new File(Tests.getScratchDir(), getRandomDirectoryName("snapshot"));
        assertTrue(d.mkdir());

        String s = Files.writeSnapshot(d, "blah", 3, "1");
        assertTrue(new File(s).getName().startsWith("blah."));

        File[] files = d.listFiles();
        assertEquals(1, files.length);

        Thread.sleep(2);

        s = Files.writeSnapshot(d, "blah", 3, "2");
        assertTrue(new File(s).getName().startsWith("blah."));

        files = d.listFiles();
        assertEquals(2, files.length);

        Thread.sleep(2);

        s = Files.writeSnapshot(d, "blah", 3, "3");
        assertTrue(new File(s).getName().startsWith("blah."));

        files = d.listFiles();
        assertEquals(3, files.length);

        Thread.sleep(2);

        s = Files.writeSnapshot(d, "blah", 3, "4");
        assertTrue(new File(s).getName().startsWith("blah."));
        
        files = d.listFiles();
        assertEquals(3, files.length);

        // order the files

        List<File> fs = Arrays.asList(files);
        Collections.sort(fs, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return f1.getName().compareTo(f2.getName());
            }
        });

        assertEquals("2", Files.read(fs.get(0)));
        assertEquals("3", Files.read(fs.get(1)));
        assertEquals("4", Files.read(fs.get(2)));

        Thread.sleep(2);

        s = Files.writeSnapshot(d, "blah", 3, "5");
        assertTrue(new File(s).getName().startsWith("blah."));

        files = d.listFiles();
        assertEquals(3, files.length);

        // order the files

        fs = Arrays.asList(files);
        Collections.sort(fs, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return f1.getName().compareTo(f2.getName());
            }
        });

        assertEquals("3", Files.read(fs.get(0)));
        assertEquals("4", Files.read(fs.get(1)));
        assertEquals("5", Files.read(fs.get(2)));

        Thread.sleep(2);

        s = Files.writeSnapshot(d, "blah", 2, "6");
        assertTrue(new File(s).getName().startsWith("blah."));

        files = d.listFiles();
        assertEquals(2, files.length);

        // order the files

        fs = Arrays.asList(files);
        Collections.sort(fs, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return f1.getName().compareTo(f2.getName());
            }
        });

        assertEquals("5", Files.read(fs.get(0)));
        assertEquals("6", Files.read(fs.get(1)));
    }

    public void testWriteSnapshot_MakeSureDifferentPrefixesAreIgnored() throws Exception
    {
        File d = new File(Tests.getScratchDir(), getRandomDirectoryName("snapshot"));
        assertTrue(d.mkdir());

        assertTrue(Files.write(new File(d, "bluh.xxx"), "bluh"));

        Files.writeSnapshot(d, "blah", 1, "1");
        File[] files = d.listFiles();
        assertEquals(2, files.length);

        // order the files

        List<File> fs = Arrays.asList(files);
        Collections.sort(fs, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return f1.getName().compareTo(f2.getName());
            }
        });

        assertEquals("1", Files.read(fs.get(0)));
        assertEquals("bluh", Files.read(fs.get(1)));

        Files.writeSnapshot(d, "blah", 1, "2");
        files = d.listFiles();
        assertEquals(2, files.length);

        // order the files

        fs = Arrays.asList(files);
        Collections.sort(fs, new Comparator<File>()
        {
            public int compare(File f1, File f2)
            {
                return f1.getName().compareTo(f2.getName());
            }
        });

        assertEquals("2", Files.read(fs.get(0)));
        assertEquals("bluh", Files.read(fs.get(1)));
    }

    public void testGenerateUniqueName() throws Exception
    {
        File d = new File(Tests.getScratchDir(), getRandomDirectoryName("d"));
        assertTrue(d.mkdir());

        assertTrue(Files.write(new File(d, "blah"), "something"));

        File f = Files.generateUniqueName(d, "blah");

        assertEquals("blah.0", f.getName());

        assertTrue(Files.write(f, "something"));

        f = Files.generateUniqueName(d, "blah");

        assertEquals("blah.1", f.getName());

        assertTrue(Files.write(f, "something"));

        f = Files.generateUniqueName(d, "blah");

        assertEquals("blah.2", f.getName());

        assertTrue(Files.write(f, "something"));
    }

    public void testWriteSnapshot_Collection() throws Exception
    {
        File d = new File(Tests.getScratchDir(), getRandomDirectoryName("snapshot"));
        assertTrue(d.mkdir());

        List<String> content = Arrays.asList("a", "b", "c");

        String s = Files.writeSnapshot(d, "collection", 1, content);
        File[] files = d.listFiles();
        assertEquals(1, files.length);

        File f = files[0];
        assertEquals(f.getAbsoluteFile(), new File(s));

        String recovered = Files.read(f);

        BufferedReader br = new BufferedReader(new StringReader(recovered));

        assertEquals("a", br.readLine());
        assertEquals("b", br.readLine());
        assertEquals("c", br.readLine());
        assertNull(br.readLine());

        br.close();
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
