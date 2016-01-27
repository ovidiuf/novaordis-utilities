package io.novaordis.utilities.testing;

import junit.framework.TestResult;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * A text TestRunner than runs only test methods specified on command line with "-t".
 *
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class SelectiveTestRunner extends TestRunner
{
    // Constants -----------------------------------------------------------------------------------

    // Static --------------------------------------------------------------------------------------

    /**
     * Specify -t testName1,testName2,... somewhere in the argument list.
     */
    public static void main(String[] args)
    {
        SelectiveTestRunner runner = new SelectiveTestRunner();

        try
        {
            args = runner.preProcessCommandLine(args);

            TestResult r = runner.start(args);

            if (!r.wasSuccessful())
            {
                System.exit(TestRunner.FAILURE_EXIT);
            }

            System.exit(TestRunner.SUCCESS_EXIT);
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
            System.exit(TestRunner.EXCEPTION_EXIT);
        }
    }

    // Attributes ----------------------------------------------------------------------------------

    private List<String> methods = new ArrayList<String>();

    // Constructors --------------------------------------------------------------------------------

    // TestRunner overrides ------------------------------------------------------------------------

    public Test getTest(String suiteClassName)
    {
       Test t = super.getTest(suiteClassName);

       if (methods.isEmpty())
       {
          return t;
       }
       else
       {
          return new SelectiveTestSuite((TestSuite)t, methods);
       }
    }

    // Public --------------------------------------------------------------------------------------

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    /**
     * Scan command line for an argument of type "-t testMethod1,testMethod2,..." and processes it.
     */
    private String[] preProcessCommandLine(String[] args)
    {
        List<String> l = new ArrayList<String>();
        for (int i = 0; i < args.length; i++)
        {
            if ("-t".equals(args[i]))
            {
                i++;
                for(StringTokenizer st = new StringTokenizer(args[i], ","); st.hasMoreTokens(); )
                {
                    methods.add(st.nextToken());
                }
            }
            else
            {
                l.add(args[i]);
            }
        }

        String[] a = new String[l.size()];
        return (String[])l.toArray(a);
    }

    // Inner classes -------------------------------------------------------------------------------
}
