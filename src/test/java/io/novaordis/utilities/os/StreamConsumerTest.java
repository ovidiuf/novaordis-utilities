/*
 * Copyright (c) 2016 Nova Ordis LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.novaordis.utilities.os;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/28/16
 */
public class StreamConsumerTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(StreamConsumerTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void nullName() throws Exception {

        MockInputStream mis = new MockInputStream();

        try {
            new StreamConsumer(null, mis);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    @Test
    public void nullInputStream() throws Exception {

        try {
            new StreamConsumer("test", null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {
            log.info(e.getMessage());
        }
    }

    // identity --------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        MockInputStream mis = new MockInputStream();
        StreamConsumer c = new StreamConsumer("test", mis);

        assertEquals(StreamConsumer.DEFAULT_BUFFER_SIZE, c.getBufferSize());
        assertFalse(c.isLogContent());
    }

    // lifecycle -------------------------------------------------------------------------------------------------------

    @Test
    public void lifecycle_EndOfStream_BufferSize4() throws Exception {

        MockInputStream mis = new MockInputStream();

        StreamConsumer c = new StreamConsumer("test", mis, 4);

        assertEquals(4, c.getBufferSize());

        assertFalse(c.isConsuming());

        //
        // the consumer was not started, nothing to read
        //

        assertTrue(c.read().isEmpty());

        c.start();

        assertTrue(c.isConsuming());

        //
        // at this point the consumer thread is starting to consume
        //

        // this should be a noop, shouldn't block or throw exception
        c.start();

        //
        // nothing was released so far
        //

        String s;

        s = c.read();
        assertEquals("", s);

        //
        // release 4 bytes, which is the buffer size
        //

        mis.releaseChunk("blah");

        //
        // loop until we get the content
        //
        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        assertEquals("blah", s);

        s = c.read();
        assertEquals("", s);

        //
        // release 5 bytes, which is the buffer size
        //

        mis.releaseChunk("ABCDE");

        //
        // because the buffer is 4 wide, we should receive four of them
        //

        //
        // loop until we get the content
        //
        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        assertEquals("ABCD", s);

        //
        // release 5 more, 3 will fill the buffer and 2 will remain in storage
        //

        mis.releaseChunk("FGHXY");

        //
        // loop until we get the content
        //
        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        assertEquals("EFGH", s);

        //
        // trigger the end the stream
        //

        mis.endTheStream();

        //
        // the end of stream will propagate, will release the storage and the will generate null
        //

        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        assertEquals("XY", s);

        //
        // next, and subsequent reads must be null
        //

        //
        assertNull(c.read());
        assertNull(c.read());

        assertTrue(c.waitForShutdown(10L));

        assertFalse(c.isConsuming());
    }

    @Test
    public void lifecycle_EndOfStream_BufferSize1() throws Exception {

        MockInputStream mis = new MockInputStream();

        StreamConsumer c = new StreamConsumer("test", mis, 1);

        assertEquals(1, c.getBufferSize());

        assertFalse(c.isConsuming());

        //
        // the consumer was not started, nothing to read
        //

        assertTrue(c.read().isEmpty());

        c.start();

        assertTrue(c.isConsuming());

        //
        // at this point the consumer thread is starting to consume
        //

        // this should be a noop, shouldn't block or throw exception
        c.start();


        String content = "";

        //
        // nothing was released so far
        //

        String s;

        s = c.read();
        assertEquals("", s);

        //
        // release 4 bytes
        //

        mis.releaseChunk("A");

        //
        // loop until we get the content
        //
        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        content += s;

        //
        // release 5 more bytes
        //

        mis.releaseChunk("BCD");

        //
        // loop until we get the content
        //
        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        content += s;

        //
        // release 5 more
        //

        mis.releaseChunk("EFGHIJKL");

        //
        // loop until we get the content
        //
        while((s = c.read()).isEmpty()) {

            Thread.sleep(50L);
        }

        content += s;

        //
        // trigger the end the stream
        //

        mis.endTheStream();

        //
        // drain the stream
        //
        while((s = c.read()) != null) {

            content += s;
        }

        //
        // we reached EOS
        //

        assertNull(c.read());
        assertNull(c.read());

        assertTrue(c.waitForShutdown(10L));

        assertFalse(c.isConsuming());

        assertEquals("ABCDEFGHIJKL", content);
    }

    @Test
    public void lifecycle_Stop() throws Exception {

        MockInputStream mis = new MockInputStream();

        StreamConsumer c = new StreamConsumer("test", mis, 4);

        c.start();

        c.stop();

        //
        // release some data to unblock (more than 4 bytes)
        //

        mis.releaseChunk("something");

        assertTrue(c.waitForShutdown(100L));

        assertFalse(c.isConsuming());
    }

    @Test
    public void lifecycle_ClosingTheInputStreamStopsTheConsumer() throws Exception {

        MockInputStream mis = new MockInputStream();

        StreamConsumer c = new StreamConsumer("test", mis, 4);

        c.start();

        mis.releaseChunk("something");

        mis.close();

        String content = "";
        String s;
        while((s = c.read()) != null) {

            assertTrue(c.isConsuming());
            content += s;
        }

        assertFalse(c.isConsuming());
        assertEquals("something", content);
    }

    @Test
    public void lifecycle_AttemptToStartAfterDeath() throws Exception {

        MockInputStream mis = new MockInputStream();

        StreamConsumer c = new StreamConsumer("test", mis);

        c.start();

        mis.endTheStream();

        //
        // wait until the consumer stopped
        //
        while(c.isConsuming()) {

            Thread.sleep(50L);
        }

        try {

            c.start();
            fail("should throw exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("the stream consumer was stopped", msg);
        }
    }

    // debug logging ---------------------------------------------------------------------------------------------------

    @Test
    public void loggingVsNotLogging() throws Exception {

        //
        // exercise a consumer that is NOT logging
        //

        MockInputStream mis = new MockInputStream();
        StreamConsumer c = new StreamConsumer("test", mis, 1);
        c.start();

        assertFalse(c.isLogContent());


        mis.releaseChunk("1\n2\n3\n4\n5\n");

        mis.close();


        //
        // drain it
        //

        String content = "";

        String s;
        while((s = c.read()) != null) {

            content += s;
        }

        assertEquals("1\n2\n3\n4\n5\n", content);

        //
        // exercise a consumer that IS logging
        //

        MockInputStream mis2 = new MockInputStream();
        StreamConsumer c2 = new StreamConsumer("test", mis2, 1, true);
        c2.start();

        assertTrue(c2.isLogContent());


        mis2.releaseChunk("1\n2\n3\n4\n5\n");

        mis2.close();


        //
        // drain it
        //

        String content2 = "";

        String s2;
        while((s2 = c2.read()) != null) {

            content2 += s2;
        }

        assertEquals("1\n2\n3\n4\n5\n", content2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
