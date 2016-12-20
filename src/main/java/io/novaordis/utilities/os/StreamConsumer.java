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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * An instance employing its own thread, whose sole aim is to consume content produced by a child process (at its
 * stdout or stderr), preventing it from blocking. It can be only used once.
 *
 * The current implementation does not have protection for memory overflow, but it can be implemented if the need
 * arise.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/28/16
 */
public class StreamConsumer {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(StreamConsumer.class);

    public static final int DEFAULT_BUFFER_SIZE = 1;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private int bufferSize;
    private byte[] buffer;
    private String name;
    final private InputStream inputStream;
    private volatile boolean stopRequested;

    private Thread thread;

    final private ByteArrayOutputStream storage;

    private final CountDownLatch consumerThreadStopped;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Starts with the default buffer size, which is 1.
     *
     * Buffer Size Note: A large buffer will will allow efficient transfers of large quantities of output. However, the
     * output will not be immediately available for consumption, unless the buffer fills up. If you need immediate
     * feedback on what the underlying process is producing at stdout/stdout, use small buffer size - even 1.
     *
     * @param name will become the name of the consuming thread.
     */
    public StreamConsumer(String name, InputStream is) {

        this(name, is, DEFAULT_BUFFER_SIZE);
    }

    /**
     * @param bufferSize sets the internal buffer. A large buffer will will allow efficient transfers of large
     *                   quantities of output. However, the output will not be immediately available for consumption,
     *                   unless the buffer fills up. If you need immediate feedback on what the underlying process is
     *                   producing at stdout/stdout, use small buffer size - even 1.

     * @param name will become the name of the consuming thread.
     */
    StreamConsumer(String name, InputStream is, int bufferSize) {

        if (name == null) {

            throw new IllegalArgumentException("null name");
        }

        this.name = name;

        if (is == null) {
            throw new IllegalArgumentException("null input stream");
        }

        this.inputStream = is;
        this.bufferSize = bufferSize;
        this.buffer = new byte[bufferSize];
        this.consumerThreadStopped = new CountDownLatch(1);
        this.storage = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Starts the internal thread and begins to consume input stream's content. If the thread was already started
     * start() is a noop
     */
    public synchronized void start() {

        if (consumerThreadStopped.getCount() <= 0) {

            //
            // the consumer thread stopped due to various causes (end of stream, shutdown, etc.)
            //

            throw new IllegalStateException("the stream consumer was stopped");
        }

        if (thread != null) {

            log.debug(this + " already started");
            return;
        }

        this.thread = new Thread(StreamConsumer.this::consume, getName());

        log.debug(this + " starting ...");

        thread.start();
    }

    /**
     * Aborts consuming in progress, if any. Note that the consumer thread is blocked on reading, the stop request
     * has no effect until the read completes.
     */
    public void stop() {

        stopRequested = true;
    }

    public boolean isConsuming() {

        return thread != null && consumerThreadStopped.getCount() == 1;
    }

    public String getName() {

        return name;
    }

    public int getBufferSize() {

        return bufferSize;
    }

    /**
     * @return the stream content accumulated since the last read, or empty string if nothing arrived while the stream
     * is not closed yet. A read empties the buffer. After the buffer was emptied, the method will return null if the
     * consumer does not consume anymore, either because it was forcibly closed or because it reached the end of stream.
     */
    public String read() {

        synchronized (storage) {

            byte[] content = storage.toByteArray();
            storage.reset();

            if (content.length == 0 && consumerThreadStopped.getCount() <= 0) {

                //
                // we depleted the storage and we're not going to get anymore, because we reached the end of stream
                //
                return null;
            }

            return new String(content);
        }
    }

    /**
     * @return true if the consumer shut down in time (due to end of stream or forcible stop), false if timeout occured
     */
    public boolean waitForShutdown(long timeoutMs) {

        while (true) {

            try {

                log.debug("blocking to wait for the end of stream");

                return consumerThreadStopped.await(timeoutMs, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {

                log.debug("interrupted while waiting for the end of stream, re-enlisting ...");

            }
        }
    }

    @Override
    public String toString() {

        return getName() + " Stream Consumer";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void consume() {

        //
        // read in a loop from the input stream until we reach the end of stream, we encounter an IOException or we are
        // stopped
        //

        try {

            while (true) {

                int read = inputStream.read(buffer);

                if (read == -1) {

                    //
                    // end of stream
                    //

                    log.debug(this + " reached the end of stream");

                    //
                    // the latch will be counted down in the finally clause
                    //

                    return;
                }

                synchronized (storage) {

                    storage.write(buffer, 0, read);
                }

                if (stopRequested) {

                    //
                    // abort
                    //

                    log.debug("stop requested, " + thread + " exiting ...");

                    //
                    // the latch will be counted down in the finally clause
                    //

                    return;
                }

            }
        }
        catch (Exception e) {

            //
            // the consumer thread exited with exception, warn and let it go
            //

            log.warn(this + " failed and stopped consuming");
            log.debug("consumer failure", e);

        }
        finally {

            consumerThreadStopped.countDown();

        }
    }


    // Inner classes ---------------------------------------------------------------------------------------------------

}
