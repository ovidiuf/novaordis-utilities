package io.novaordis.utilities.file;

import io.novaordis.utilities.crawler.Collector;
import io.novaordis.utilities.crawler.Frame;

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
public class FileObliterator implements Collector
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private File root;
    private boolean rootItself;

    // Constructors --------------------------------------------------------------------------------

    /**
     * By default, it deletes the root.
     */
    public FileObliterator()
    {
        this(null, true);
    }

    public FileObliterator(File root, boolean rootItself)
    {
        this.root = root;
        this.rootItself = rootItself;
    }

    // Collector implementation --------------------------------------------------------------------

    public void collect(Frame frame) throws Exception
    {
        File f = ((FileFrame)frame).getDelegate();

        if (f.equals(root) && f.isFile())
        {
            throw new Exception(f + " is a file, cannot rmdir");
        }
        else if (!f.equals(root) || rootItself)
        {
            boolean success = f.delete();

            if (!success)
            {
                throw new Exception(
                    "Cannot delete " + (f.isDirectory() ? "directory" : "file") + " " + f);
            }
        }
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------

}
