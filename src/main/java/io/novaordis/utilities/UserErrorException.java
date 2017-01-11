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

package io.novaordis.utilities;

/**
 * An exception intended up to bubble up to the highest layer and put a human-readable error message at stderr.
 *
 * Message Composition Rules:
 *
 * 1. If there is no underlying cause, getMessage() result is the message specified in the constructor. If no message
 *    was specified in the constructor, getMessage() returns null.
 *
 * 2. If there is an underlying cause, the message will contain the bottom-most (originating) cause details, translated
 * into a log/console friendly format. If the UserErrorException instance specifies a message, it will prefix the
 * inferred message.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/21/16
 */
public class UserErrorException extends Exception {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private String message;

    // Constructors ----------------------------------------------------------------------------------------------------

    public UserErrorException() {
        super();
    }

    public UserErrorException(String message) {

        this(message, null);
    }

    public UserErrorException(String message, Throwable cause) {

        super(cause);
        this.message = message;
    }

    public UserErrorException(Throwable cause) {
        super(cause);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Descend the cause hierarchy and extract the bottom-most information, or null.
     *
     * @return the bottom-most embedded cause, or null
     */
    public Throwable getOriginalCause() {

        Throwable originalCause = null;

        Throwable crt = getCause();

        while(crt != null) {

            originalCause = crt;
            crt = crt.getCause();
        }

        return originalCause;
    }

    /**
     * @return a human-readable message, appropriate for logs and console. See Message Composition Rules above.
     *
     * May return null.
     */
    @Override
    public String getMessage() {

        String message = null;

        //
        // descend the cause hierarchy and extract the bottom-most information
        //

        Throwable originalCause = getOriginalCause();

        if (originalCause != null) {

            String olderCauseMessage = originalCause.getMessage();

            message = originalCause.getClass().getSimpleName();

            if (olderCauseMessage != null) {

                message += " " + olderCauseMessage;
            }
        }

        if (this.message != null) {

            message = this.message + (message == null ? "" : ": " + message);
        }

        return message;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
