package com.novaordis.utilities.testing;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.Assert;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Collections;

/**
 * A TestSuite that filters tests.
 *
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class SelectiveTestSuite extends TestSuite
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    private List<TestCase> tests;

    // Constructors --------------------------------------------------------------------------------

    public SelectiveTestSuite(TestSuite original, List passedMethods)
    {
       tests = new ArrayList<TestCase>();
       List<String> methods = new ArrayList<String>(passedMethods);
       methods.add("warning");

       for(Enumeration e = original.tests(); e.hasMoreElements();)
       {
          TestCase tc = (TestCase)e.nextElement();
          if (methods.contains(tc.getName()))
          {
             tests.add(tc);
          }
       }

       if (tests.isEmpty())
       {
          tests.add(new TestCase("warning")
          {
             protected void runTest()
             {
                Assert.fail("The SelectiveTestSuite did not select any test.");
             }
          });
       }
    }

    // TestSuite overrides -------------------------------------------------------------------------

    public Test testAt(int index)
    {
       return (Test)tests.get(index);
    }

    public int testCount()
    {
       return tests.size();
    }

    public Enumeration tests()
    {
       return Collections.enumeration(tests);
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
