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

package io.novaordis.utilities;

/**
 * An exception that carries a line number, as well as a position in line. Useful for parsing errors.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/21/16
 */
public class LineNumberException extends Exception {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private Long lineNumber;
    private Integer positionInLine;

    // Constructors ----------------------------------------------------------------------------------------------------

    public LineNumberException(String message) {

        this(null, null, message, null);
    }

    public LineNumberException(String message, Throwable cause) {

        this(null, null, message, cause);
    }

    public LineNumberException(Long lineNumber, String message) {

        this(lineNumber, null, message, null);
    }

    public LineNumberException(Long lineNumber, String message, Throwable cause) {

        this(lineNumber, null, message, cause);
    }

    public LineNumberException(Long lineNumber, Integer positionInLine, String message) {

        this(lineNumber, positionInLine, message, null);
    }

    /**
     * @param lineNumber the line number of the line that generated the exception. Null is acceptable if the line number
     *                   is not known.
     * @param positionInLine the position in line where the exception occurred. Null is acceptable if the position is
     *                       not known.
     */
    public LineNumberException(Long lineNumber, Integer positionInLine, String message, Throwable cause) {

        super(message, cause);
        this.lineNumber = lineNumber;
        this.positionInLine = positionInLine;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the line number of the line that generated the exception, or null if the information is not available.
     */
    public Long getLineNumber() {

        return lineNumber;
    }

    /**
     * @return the position in line where the cause of the exception can be found, or null if the position is not
     * available
     */
    public Integer getPositionInLine() {

        return positionInLine;
    }

    /**
     * A message suitable for displaying in human-read logs.
     */
    public String toLogFormat() {

        String s = "";

        if (lineNumber != null) {

            s += "line " + lineNumber;
        }

        if (positionInLine != null) {

            if (!s.isEmpty()) {

                s += ",";

            }

            s += " position " + positionInLine;
        }

        if (!s.isEmpty()) {

            s += ": ";
        }

        s += getMessage();

        return s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
