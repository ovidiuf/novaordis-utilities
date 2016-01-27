package io.novaordis.utilities.crawler;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class SingleThreadedCrawler implements Crawler
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    /**
     * @exception IllegalArgumentException
     */
    private static byte validateVisitingOrder(byte b)
    {
        if (b != PREORDER &&
            b != INORDER &&
            b != POSTORDER &&
            b != LEVELORDER &&
            b != BREADTH_FIRST)
        {
            throw new IllegalArgumentException("Invalid visiting order");
        }

        return b;
    }

    // Attributes ----------------------------------------------------------------------------------

    // stateless, with the exception of the visiting order

    private byte visitingOrder;

    // Constructors --------------------------------------------------------------------------------


    /**
     * Default visiting order is PREORDER.
     */
    public SingleThreadedCrawler()
    {
        this(PREORDER);
    }

    /**
     * @param visitingOrder - one of Crawler.INORDER, Crawler.POSTORDER, Crawler.LEVELORDER (or
     *        Crawler.BREADTH_FIRST)
     */
    public SingleThreadedCrawler(byte visitingOrder)
    {
        this.visitingOrder = validateVisitingOrder(visitingOrder);
    }

    // Crawler implementation ----------------------------------------------------------------------

    public Collector crawl(Frame frame, Collector in) throws Exception
    {
        if (visitingOrder == PREORDER)
        {
            in = frame.use(in);

            for(Frame child: frame.children())
            {
                in = crawl(child, in);
            }
        }
        else if (visitingOrder == POSTORDER)
        {
            for(Frame child: frame.children())
            {
                in = crawl(child, in);
            }

            in = frame.use(in);
        }
        else
        {
            throw new Exception("NOT YET IMPLEMENTED");
        }

        return in;
    }

    public byte getVisitingOrder()
    {
        return visitingOrder;
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
