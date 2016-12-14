package io.novaordis.utilities.ac;

import io.novaordis.utilities.ac.mock.MockHandler;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HandlersTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(HandlersTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void addRemove() throws Exception {

        Handlers handlers = new Handlers();

        MockHandler h = new MockHandler();

        assertTrue(handlers.add(h));

        h.setCanHandle(true);

        handlers.process(new Collected(-1L, "k823b32f"));

        List<Collected> received = h.getReceived();
        assertEquals(1, received.size());
        assertEquals("k823b32f", received.get(0).getPayload());
        h.clear();

        assertTrue(handlers.remove(h));
        assertFalse(handlers.remove(h));

        handlers.process(new Collected(-1L, "j7yOPe6"));
        assertTrue(received.isEmpty());

        log.debug(".");
    }

    @Test
    public void failureOnCanHandle() throws Exception {

        Handlers handlers = new Handlers();

        Handler h = new Handler() {

            @Override
            public boolean canHandle(Object o) {

                throw new RuntimeException("SYNTHETIC");
            }

            @Override
            public void handle(long timestamp, String threadName, Object o) {
            }

            @Override
            public void close() {
            }
        };

        MockHandler h2 = new MockHandler();
        h2.setCanHandle(true);

        assertTrue(handlers.add(h));
        assertTrue(handlers.add(h2));

        // this must not fail
        handlers.process(new Collected(-1L, "k823b32G"));

        // the other handler must be notified
        assertEquals("k823b32G", h2.getReceived().get(0).getPayload());

        assertTrue(handlers.remove(h));
    }

    @Test
    public void failureOnHandle() throws Exception {

        Handlers handlers = new Handlers();

        Handler h = new Handler() {

            @Override
            public boolean canHandle(Object o) {
                return true;
            }

            @Override
            public void handle(long timestamp, String threadName, Object o) {
                throw new RuntimeException("SYNTHETIC");
            }

            @Override
            public void close() {
            }
        };

        MockHandler h2 = new MockHandler();
        h2.setCanHandle(true);

        assertTrue(handlers.add(h));
        assertTrue(handlers.add(h2));

        // this must not fail
        handlers.process(new Collected(-1L, "k823b32k"));

        // the other handler must be notified
        assertEquals("k823b32k", h2.getReceived().get(0).getPayload());

        assertTrue(handlers.remove(h));
    }

    @Test
    public void successfulClose() throws Exception {

        Handlers handlers = new Handlers();

        MockHandler h = new MockHandler();
        MockHandler h2 = new MockHandler();

        assertTrue(handlers.add(h));
        assertTrue(handlers.add(h2));

        handlers.close();

        assertTrue(h.wasCloseCalled());
        assertTrue(h2.wasCloseCalled());
    }

    @Test
    public void failureOnClose() throws Exception {

        Handlers handlers = new Handlers();

        Handler h = new Handler() {

            @Override
            public boolean canHandle(Object o) {
                return true;
            }

            @Override
            public void handle(long timestamp, String threadName, Object o) {
            }

            @Override
            public void close() {
                throw new RuntimeException("SYNTHETIC");
            }
        };

        MockHandler h2 = new MockHandler();

        assertTrue(handlers.add(h));
        assertTrue(handlers.add(h2));

        // this must not fail
        handlers.close();

        assertTrue(h2.wasCloseCalled());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
