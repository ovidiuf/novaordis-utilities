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

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * An adapter that allows StAX parsing of the cached lines.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/11/16
 */
public class CachedContentReader extends Reader {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<Line> externalContent;

    // 0-based
    int currentLine;

    // the index of the first character that was NOT read yet
    int currentPositionInLine;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Usually the actual content of the editor is passed, no copy is made.
     */
    public CachedContentReader(List<Line> lines) {

        this.externalContent = lines;
        this.currentLine = 0;
        this.currentPositionInLine = 0;
    }

    // Reader implementation -------------------------------------------------------------------------------------------

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        if (currentLine == -1 || currentLine >= externalContent.size()) {

            // we reached the end of stream
            return -1;
        }

        //
        // iterate over lines until we get sufficient characters or we reach the end of our stream
        //

        int readCharacters = 0;

        while(true) {

            //
            // if we ran out of lines, return what we have so far
            //
            if (currentLine == -1 || currentLine >= externalContent.size()) {
                return readCharacters;
            }

            Line crtl = externalContent.get(currentLine);
            char[] chars = crtl.getChars();

            int availableOnThisLine = chars.length - currentPositionInLine;

            if (availableOnThisLine >= len - readCharacters) {

                //
                // we have sufficient characters on this line
                //

                int readFromLine = len - readCharacters;
                System.arraycopy(chars, currentPositionInLine, cbuf, off + readCharacters, readFromLine);
                readCharacters = len;
                currentPositionInLine += readFromLine;

                //
                // if we reached end of the line, advance the cursor to the next line
                //
                if (currentPositionInLine >= chars.length) {
                    currentPositionInLine = 0;
                    currentLine ++;

                    identifyEndOfStream();
                }

                return readCharacters;
            }
            else {

                // copy what we have so far on the current line and go to the next line
                int readFromLine = chars.length - currentPositionInLine;
                System.arraycopy(chars, currentPositionInLine, cbuf, off + readCharacters, readFromLine);
                readCharacters += readFromLine;
                currentLine ++;
                currentPositionInLine = 0;
                identifyEndOfStream();
            }
        }

        //
        //
        //

        // TODO len bigger than what fits in cbuf
    }

    @Override
    public void close() throws IOException {
        throw new RuntimeException("close() NOT YET IMPLEMENTED");
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     *  The line number (0-based) of the line that contains the first character that was not read yet.
     *
     *  Returns -1 if we reached the end of stream.
     */
    public int getCurrentLine() {

        return currentLine;
    }

    /**
     *  The index of the first character, on the current line, that was NOT read yet.
     *
     *  Returns -1 if we reached the end of stream.
     */
    public int getCurrentPositionInLine() {

        return currentPositionInLine;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private void identifyEndOfStream() {

        if (currentLine < externalContent.size()) {
            return;
        }

        currentLine = -1;
        currentPositionInLine = -1;
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
