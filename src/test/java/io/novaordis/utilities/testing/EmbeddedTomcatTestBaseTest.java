package io.novaordis.utilities.testing;

import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import java.util.ArrayList;

/**
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2009 Ovidiu Feodorov
 */
public class EmbeddedTomcatTestBaseTest extends TestBase
{
    // Constants -----------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(EmbeddedTomcatTestBaseTest.class);

    // Static --------------------------------------------------------------------------------------

    // Attributes ----------------------------------------------------------------------------------

    // Constructors --------------------------------------------------------------------------------

    // Public --------------------------------------------------------------------------------------

    public void testWeDontLeaveMBeanServersBehind() throws Exception
    {
        ArrayList<MBeanServer> before = MBeanServerFactory.findMBeanServer(null);

        EmbeddedTomcatTestBase t = new EmbeddedTomcatTestBase();

        t.setUp();
        t.tearDown();

        ArrayList<MBeanServer> after = MBeanServerFactory.findMBeanServer(null);

        assertEquals(before.size(), after.size());

        for(MBeanServer s: before)
        {
            if (!after.remove(s))
            {
                fail("MBeanServer " + s + " inadvertently removed");
            }
        }

        if (after.size() > 0)
        {
            fail("leaking MBeanServers: " + after);
        }
    }

    // Package protected ---------------------------------------------------------------------------

    // Protected -----------------------------------------------------------------------------------

    // Private -------------------------------------------------------------------------------------

    // Inner classes -------------------------------------------------------------------------------
}
