package io.novaordis.utilities.ac;

import io.novaordis.utilities.ac.mock.MockHandler;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class CollectorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(CollectorTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void dispose() throws Exception {

        Collector c = getCollectorToTest("blah");

        assertEquals("blah", c.getName());
        assertTrue(c.getThreadName().contains("blah"));

        MockHandler mh = new MockHandler();

        assertTrue(c.registerHandler(mh));

        c.dispose();

        assertTrue(mh.wasCloseCalled());

        assertFalse(c.handOver(new Object()));

        assertFalse(c.registerHandler(new MockHandler()));

        assertTrue(c.unregisterHandler(mh));
        assertFalse(c.unregisterHandler(mh));
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    protected abstract Collector getCollectorToTest(String name);

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
