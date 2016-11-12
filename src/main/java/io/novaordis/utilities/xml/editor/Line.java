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

    // the 1-based line number
    private int lineNumber;

    // the actual characters, including the line terminators (\n, \r\n, etc) if present
    private char[] chars;

    // whether we had line terminators or not
    private String newLine;

    private boolean dirty;

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

        this.dirty = false;
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
     * @return the length of the line, including the terminator(s), if any.
     */
    public int getLength() {

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
     * Returns the bytes corresponding to the the internal storage content, in a copy array (unlike the char[] returned
     * by getChars(), which is the actual underlying storage).
     */
    public byte[] getBytes() {

        byte[] bytes = new byte[chars.length];
        for(int i = 0; i < bytes.length; i ++) {
            bytes[i] = (byte)chars[i];
        }
        return bytes;
    }

    /**
     * @return the line value - the string, without line terminators.
     *
     * @see Line#getChars();
     */
    public String getValue() {

        return new String(chars, 0, chars.length - (newLine == null ? 0 : newLine.length()));
    }

    /**
     *
     * @return the 1-based line number.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    public boolean isDirty() {

        return dirty;
    }

    /**
     * Replaces the content between the 'from' and 'to' indexes ('to' is the first character that is not touched
     * during replacement) with the new value, contracting or expanding the line.
     *
     * If the indexes are identical, the new content is inserted on the position.
     *
     * @return true if a replacement was made, or false if the old content is identical with the new content.
     */
    public boolean replace(int from, int to, String newValue) {

        if (from < 0 || from >= chars.length - terminatorSectionLength()) {
            throw new IndexOutOfBoundsException("'from' index " + from);
        }

        if (to < from || to > chars.length - terminatorSectionLength()) {
            throw new IndexOutOfBoundsException("'to' index " + to);
        }

        //
        // calculate whether we need to expand or contract the storage, "expansion" can be negative
        //

        int expansion = newValue.length() - to + from;

        if (expansion == 0) {

            //
            // no expansion necessary
            //

            if (newValue.equals(new String(chars, from, to - from))) {

                //
                // if the old value and the new value are identical, don't do anything
                //
                return false;
            }
            else {

                //
                // simply replace the value
                //
                newValue.getChars(0, newValue.length(), chars, from);
                dirty = true;
                return true;
            }
        }

        //
        // expand, positively or negatively
        //
        char[] oldChars = chars;
        chars = new char[chars.length + expansion];
        System.arraycopy(oldChars, 0, chars, 0, from);
        newValue.getChars(0, newValue.length(), chars, from);
        System.arraycopy(oldChars, to, chars, from + newValue.length(), oldChars.length - to);
        dirty = true;
        return true;
    }

    @Override
    public String toString() {

        return lineNumber + " (" + (isDirty() ? "dirty" : "not dirty") + "): " + getValue();
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setDirty(boolean b) {
        this.dirty = b;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * @return 0, 1 or 2 depending on whether there is no terminator, the terminator is '\n' or is '\r\n'.
     */
    private int terminatorSectionLength() {

        if (newLine == null) {
            return 0;
        }

        return newLine.length();
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
