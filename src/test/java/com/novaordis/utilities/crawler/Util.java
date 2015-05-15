package com.novaordis.utilities.crawler;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
class Util 
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    /**
     * @return the same node it receives, but grown.
     */
    public static Node grow(Node root, int width, int depth)
    {
        if (depth > 1)
        {
            for(int i = 0; i < width; i++)
            {
                root.add(grow(new Node(root.getDepth() + 1), width, depth - 1));
            }
        }

        return root;
    }

    public static Node build3By3()
    {
        Node n0 = new Node("0", 1);

        Node n1 = new Node("1", 2);
        Node n2 = new Node("2", 2);
        Node n3 = new Node("3", 2);
        n0.add(n1);
        n0.add(n2);
        n0.add(n3);

        Node n4 = new Node("4", 3);
        Node n5 = new Node("5", 3);
        Node n6 = new Node("6", 3);
        n1.add(n4);
        n1.add(n5);
        n1.add(n6);

        Node n7 = new Node("7", 3);
        Node n8 = new Node("8", 3);
        Node n9 = new Node("9", 3);
        n2.add(n7);
        n2.add(n8);
        n2.add(n9);

        Node n10 = new Node("10", 3);
        Node n11 = new Node("11", 3);
        Node n12 = new Node("12", 3);
        n3.add(n10);
        n3.add(n11);
        n3.add(n12);

        return n0;
    }

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
