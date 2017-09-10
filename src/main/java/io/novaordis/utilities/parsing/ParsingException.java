/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.utilities.parsing;

/**
 * A parsing exception. Provides line number and position in line information related to where the parsing error
 * occurred.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 2/14/17
 */
public class ParsingException extends LineNumberException {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    public ParsingException(String message) {

        this(null, null, message, null);
    }

    public ParsingException(Throwable cause) {

        this(null, null, null, cause);
    }

    public ParsingException(String message, Throwable cause) {

        super(message, cause);
    }

    public ParsingException(Long lineNumber, String message) {

        super(lineNumber, null, message, null);
    }

    public ParsingException(Long lineNumber, Throwable cause) {

        super(lineNumber, null, null, cause);
    }

    public ParsingException(Long lineNumber, String message, Throwable cause) {

        super(lineNumber, null, message, cause);
    }

    public ParsingException(Long lineNumber, Integer positionInLine, String message) {

        super(lineNumber, positionInLine, message, null);
    }

    public ParsingException(Long lineNumber, Integer positionInLine, String message, Throwable cause) {

        super(lineNumber, positionInLine, message, cause);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
