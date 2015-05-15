package com.novaordis.utilities.file;

import com.novaordis.utilities.crawler.Collector;
import com.novaordis.utilities.crawler.Frame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.File;

/**
 * A Frame decorator for Files.
 *
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class FileFrame implements Frame
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private File delegate;

    // Constructors --------------------------------------------------------------------------------

    public FileFrame(File f)
    {
        delegate = f;
    }

    // Frame implementation ------------------------------------------------------------------------

    public List<Frame> children()
    {
        File[] children = delegate.listFiles();

        if (children == null)
        {
            // delegate is not a directory
            return Collections.emptyList();
        }
        
        Frame[] frames = new FileFrame[children.length];

        for(int i = 0; i < children.length; i++)
        {
            frames[i] = new FileFrame(children[i]);
        }

        return Arrays.asList(frames);
    }

    public Collector use(Collector in) throws Exception
    {
        in.collect(this);
        return in;
    }

    // Public --------------------------------------------------------------------------------------

    public File getDelegate()
    {
        return delegate;
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------

}
