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

    final private OutputStream outputStream;

    // Constructors ----------------------------------------------------------------------------------------------------

    StreamProducer(OutputStream os) {

        this.outputStream = os;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public synchronized void start() {

        Thread thread = new Thread(StreamProducer.this::produce);
        thread.start();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void produce() {

        try {

            int count = 0;

            while (count < 10) {

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
