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

import java.io.OutputStream;

/**
 * A component managing an output stream that goes into the process' stdin.
 *
 * Experimental implementation, not ready for real use yet.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/28/16
 */
public class StreamProducer {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(StreamProducer.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private final OutputStream outputStream;

    private volatile Thread thread;

    // Constructors ----------------------------------------------------------------------------------------------------

    StreamProducer(OutputStream os) {

        this.outputStream = os;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public synchronized void start() {

        if (isStarted()) {

            log.debug(this + " already started");
            return;
        }

        this.thread = new Thread(StreamProducer.this::produce);

        //
        // make the writing thread daemon, so it won't prevent the JVM from exiting when it is the only one remaining
        //
        thread.setDaemon(true);

        thread.start();

        log.debug(this + " started");
    }

    public synchronized boolean isStarted() {

        return thread != null;
    }

    public synchronized void stop() {

        if (!isStarted()) {

            log.debug(this + " already stopped");
            return;
        }

        this.thread = null;

        log.debug(this + " stopped");
    }

    @Override
    public String toString() {

        return "StreamProducer[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    /**
     * Used for testing. May return null.
     */
    Thread getThread() {

        return thread;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void produce() {

        try {

            int count = 0;

            while (thread != null && count < 10) {

                outputStream.write(1);
                count ++;
            }
        }
        catch (Exception e) {

            log.error("" + e);
        }
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
