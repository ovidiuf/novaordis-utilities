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

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The input stream implementation "releases" chunks for consumptions upon the invocation of releaseChunk()
 *
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/28/16
 */
public class MockInputStream extends InputStream {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final char EOT = (char)4;

    private static final Logger log = Logger.getLogger(MockInputStream.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private BlockingQueue<Character> queue;

    private volatile boolean endOfStream;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockInputStream() {

        this.queue = new LinkedBlockingQueue<>();
        this.endOfStream = false;
    }

    // InputStream implementation --------------------------------------------------------------------------------------

    @Override
    public int read() throws IOException {

        if (endOfStream) {

            return -1;
        }

        try {

            Character c = queue.take();

            if ((int)c == EOT) {

                log.info(this + " reached the end of stream");

                endOfStream = true;
                return -1;
            }

            return (int)c;
        }
        catch (InterruptedException e) {

            throw new IOException(e);
        }
    }

    /**
     * Places an "EOT" character in the queue.
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {

        endTheStream();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Release a chunk of bytes for consumption
     */
    public void releaseChunk(String s) {

        for(int i = 0; i < s.length(); i ++) {

            queue.add(s.charAt(i));
        }
    }

    /**
     * Places an "EOT" character in the queue.
     */
    public void endTheStream() {

        queue.add(EOT);
    }

    @Override
    public String toString() {

        return "MockInputStream[" + Integer.toHexString(System.identityHashCode(this)) + "]";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
