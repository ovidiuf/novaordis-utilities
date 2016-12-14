package io.novaordis.utilities.ac.end2end;

import io.novaordis.utilities.ac.Collector;
import io.novaordis.utilities.ac.CollectorFactory;
import io.novaordis.utilities.ac.Handler;
import io.novaordis.utilities.ac.handler.ToCsv;
import io.novaordis.utilities.ac.mock.MockHandler;
import io.novaordis.utilities.testing.Tests;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class End2EndTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(End2EndTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @After
    public void scratchCleanup() throws Exception {

        Tests.cleanup();
    }

    @Test
    public void end2end() throws Exception {


        final Collector c = CollectorFactory.getInstance("j8Hr64BK");

        assertEquals("j8Hr64BK", c.getName());
        assertTrue(c.getThreadName().contains("j8Hr64BK"));

        TestHandler th = new TestHandler();

        assertTrue(c.registerHandler(th));

        // we use the object handed over to collector for synchronization too
        final BlockingQueue<Object> o = new ArrayBlockingQueue<>(1);

        long t0 = System.currentTimeMillis();

        new Thread(new Runnable() {

            public void run() {

                // we hand over the empty queue
                c.handOver(o);

            }
        }, "j23bEWq31 thread").start();

        // wait until the handler puts something in the queue

        Object[] result = (Object[])o.take();

        long t2 = System.currentTimeMillis();

        assertEquals(3, result.length);

        // verify thread name

        assertEquals("j23bEWq31 thread", result[0]);

        // verify time interval

        long t1 = (Long)result[1];

        assertTrue(t0 <= t1);
        assertTrue(t1 <= t2);

        assertEquals(c.getThreadName(), result[2]);

        assertEquals(1, th.getInvocationCounter());

        c.dispose();

        assertFalse(c.handOver("B"));

        // make sure nothing is pushed to the handler
        assertEquals(1, th.getInvocationCounter());

        assertFalse(c.registerHandler(new MockHandler()));
    }

    @Test
    public void end2endCsv() throws Exception {

        File sd = Tests.getScratchDir();
        File f = new File(sd, "test.csv");
        Collector c = CollectorFactory.getInstance("j8Hr64BL", Thread.NORM_PRIORITY + 1);
        assertTrue(c.registerHandler(new ToCsv(f)));

        c.handOver("A");
        c.handOver("B");
        c.handOver("C");

        // put the main thread to sleep for 500 ms to allow the drain thread to kick in; even so it's not guaranteed
        // to have our events processed. If this test fails a lot, review the approach.
        Thread.sleep(500L);

        c.dispose();

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = br.readLine();
        assertNotNull(line);
        log.info(line);

        line = br.readLine();
        assertNotNull(line);
        log.info(line);

        line = br.readLine();
        assertNotNull(line);
        log.info(line);

        assertNull(br.readLine());

        br.close();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

    private class TestHandler implements Handler {

        int counter;

        TestHandler() {

            this.counter = 0;
        }

        @Override
        public boolean canHandle(Object o)
        {
            return o instanceof ArrayBlockingQueue<?>;
        }

        @Override
        public void handle(long timestamp, String originatorThreadName, Object o) {

            try {

                counter ++;
                BlockingQueue<Object> queue = (BlockingQueue<Object>)o;
                Object[] result = new Object[]
                    {
                        originatorThreadName, // initiating thread
                        timestamp,
                        Thread.currentThread().getName()}; // the pump thread
                queue.put(result);
            }
            catch(Exception e) {

                log.error("handler failed", e);
            }
        }

        @Override
        public void close() {
        }

        public int getInvocationCounter() {

            return counter;
        }
    }

}
