package io.novaordis.utilities.crawler;

/**
 * A crawler can walk a tree in 4 ways, by choosing a different order of visiting:
 *
 * 1. Crawler.PREORDER: It means visit the ROOT, then the LEFT subtree, then the RIGHT subtree.
 * 2. Crawler.INORDER: It means visit the LEFT subtree, then the ROOT, then the RIGHT subtree.
 * 3. Crawler.POSTORDER: It means visit the LEFT subtree, then the RIGHT subtree, then the ROOT.
 * 4. Crawler.LEVELORDER (Crawler.BREADTH_FIRST): It means visit on the same horizontal level first
 *    all childs, then proceed to the next horizontal level.
 *
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public interface Crawler
{
    public static final byte PREORDER = 0;
    public static final byte INORDER = 1;
    public static final byte POSTORDER = 2;
    public static final byte LEVELORDER = 3;
    public static final byte BREADTH_FIRST = 3;

    /**
     * @return the Collector instance that has been passed in, but after the whole frame was crawled
     *         over, and all that needed collected was collected.
     */
    Collector crawl(Frame frame, Collector in) throws Exception;

    byte getVisitingOrder();
}
