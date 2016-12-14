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

package io.novaordis.utilities.ac.mock;

import io.novaordis.utilities.ac.Collected;
import io.novaordis.utilities.ac.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 12/14/16
 */
public class MockHandler implements Handler {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private volatile boolean closed;
    private List<Collected> received;
    private boolean canHandle;

    // Constructors ----------------------------------------------------------------------------------------------------

    public MockHandler() {

        this.closed = false;
        this.received = new ArrayList<>();
    }

    // Handler interface -----------------------------------------------------------------------------------------------

    @Override
    public boolean canHandle(Object o) {

        return canHandle;
    }

    @Override
    public void handle(long timestamp, String threadName, Object o) {

        received.add(new Collected(timestamp, threadName, o));
    }

    @Override
    public void close() {
        closed = true;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public boolean wasCloseCalled() {

        return closed;
    }

    public List<Collected> getReceived() {

        return received;
    }

    public void clear() {

        received.clear();
    }

    public boolean isEmpty() {

        return received.isEmpty();
    }

    public void setCanHandle(boolean b) {
        this.canHandle = b;
    }

    // Package Protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
