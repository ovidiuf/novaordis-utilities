package io.novaordis.utilities.crawler;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class CrawlerTest extends Assert
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    @Test
    public void testUnidimensionalFrameCrawl() throws Exception
    {
        Crawler crawler = new SingleThreadedCrawler();

        for(int depth = 1; depth < 11; depth ++)
        {
            Node root = Util.grow(new Node(), 1, depth);

            Counter counter = new Counter();

            assertEquals(0, counter.getCounter());

            crawler.crawl(root, counter);

            assertEquals(depth, counter.getCounter());
        }
    }

    @Test
    public void testBidimensionalCrawl() throws Exception
    {
        Crawler crawler = new SingleThreadedCrawler();

        Node root = Util.grow(new Node(), 2, 3);

        Counter counter = new Counter();

        assertEquals(0, counter.getCounter());

        crawler.crawl(root, counter);

        assertEquals(7, counter.getCounter());
    }

    @Test
    public void testDepthGaugeCrawl() throws Exception
    {
        Crawler crawler = new SingleThreadedCrawler();

        Node root = Util.grow(new Node(), 3, 5);

        DepthGauge g = new DepthGauge();

        assertEquals(0, g.getReading());

        crawler.crawl(root, g);

        assertEquals(5, g.getReading());
    }

    @Test
    public void testPREORDER() throws Exception
    {
        // default should be PREORDER

        SingleThreadedCrawler c = new SingleThreadedCrawler();

        assertEquals(Crawler.PREORDER, c.getVisitingOrder());

        Node root = Util.build3By3();

        final StringBuffer sb = new StringBuffer();

        Collector collector = new Collector()
        {
            public void collect(Frame frame) throws Exception
            {
                Node n = (Node)frame;
                sb.append(n.getID()).append(',');
            }
        };

        c.crawl(root, collector);

        assertEquals("0,1,4,5,6,2,7,8,9,3,10,11,12,", sb.toString());
    }


    @Test
    public void testPOSTORDER() throws Exception
    {
        SingleThreadedCrawler c = new SingleThreadedCrawler(Crawler.POSTORDER);

        assertEquals(Crawler.POSTORDER, c.getVisitingOrder());

        Node root = Util.build3By3();

        final StringBuffer sb = new StringBuffer();

        Collector collector = new Collector()
        {
            public void collect(Frame frame) throws Exception
            {
                Node n = (Node)frame;
                sb.append(n.getID()).append(',');
            }
        };

        c.crawl(root, collector);

        assertEquals("4,5,6,1,7,8,9,2,10,11,12,3,0,", sb.toString());
    }


    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------

    private class Counter implements Collector
    {
        int counter = 0;

        public synchronized void collect(Frame frame) throws Exception
        {
            counter ++;
        }

        public int getCounter()
        {
            return counter;
        }
    }

    private class DepthGauge implements Collector
    {
        int depth = 0;

        public synchronized void collect(Frame frame) throws Exception
        {
            int d = ((Node)frame).getDepth();

            if (d > depth)
            {
                depth = d;
            }
        }

        public int getReading()
        {
            return depth;
        }
    }
}
