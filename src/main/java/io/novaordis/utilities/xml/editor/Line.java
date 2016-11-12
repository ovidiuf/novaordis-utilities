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

package io.novaordis.utilities.xml.editor;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/11/16
 */
public class Line {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private int lineNumber;

    // the actual characters, including the line terminators (\n, \r\n, etc) if present
    private char[] chars;

    // whether we had line terminators or not
    private String newLine;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * @param newLine "\n", "\r\n" or null
     */
    public Line(int lineNumber, String line, String newLine) {

        this.lineNumber = lineNumber;
        this.newLine = newLine;
        this.chars = new char[line.length() + (newLine == null ? 0 : newLine.length())];
        line.getChars(0, line.length(), chars, 0);
        if (newLine != null) {
            newLine.getChars(0, newLine.length(), chars, line.length());
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public boolean hasNewLine() {

        return newLine != null;
    }

    /**
     * @return  may return "\n" "\r\n", or null if the line has no "new line" information,
     */
    public String getNewLine() {

        return newLine;
    }

    /**
     * @return the length of the line, including the terminator, if any.
     */
    public int length() {

        return chars.length;
    }

    /**
     * @return direct access to the underlying char[] storage, for efficiency reasons. If the line ended with a
     * terminator in the original source ('\n' or '\r\n'), the terminator is also part of the returned char[]. If you
     * need only the string, without the terminator, use getValue()
     *
     * @see Line#getValue();
     */
    public char[] getChars() {
        return chars;
    }

    /**
     * @return the line value - the string, without line terminators.
     *
     * @see Line#getChars();
     */
    public String getValue() {

        return new String(chars, 0, chars.length - newLine.length());
    }

    public int getLineNumber() {
        return lineNumber;
    }


    @Override
    public String toString() {

        return lineNumber + ": " + new String(chars, 0, chars.length - newLine.length());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
