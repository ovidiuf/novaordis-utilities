package io.novaordis.utilities.crawler;

import java.util.List;
import java.util.ArrayList;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
class Node implements Frame
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private List<Frame> children;
    private int depth;
    private String id;

    // Constructors --------------------------------------------------------------------------------

    Node()
    {
        this(1);
    }

    Node(int depth)
    {
        this("DEFAULT_ID", depth);
    }

    Node(String id, int depth)
    {
        this.id = id;
        this.depth = depth;
        children = new ArrayList<Frame>();
    }

    // Frame implementation ------------------------------------------------------------------------

    public List<Frame> children()
    {
        return children;
    }

    public Collector use(Collector in) throws Exception
    {
        in.collect(this);
        return in;
    }

    // Public --------------------------------------------------------------------------------------

    void add(Node child)
    {
        children.add(child);
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public String getID()
    {
        return id;
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------

}
