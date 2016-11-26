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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Essentially, a list of lines that are read from a file, possibly modified, and then dumped back to a file.
 *
 * Exposes its internal storage structure to classes from the same package, such as LineBasedContentReader.
 *
 * The default implementation always assumes the Linux convention where a line ends with "\n".
 *
 * @see LineBasedContentReader
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/11/16
 */
public class LineBasedContent {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final int DEFAULT_BUFFER_SIZE = 10240;

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private List<Line> lines;
    private int bufferSize;

    // Constructors ----------------------------------------------------------------------------------------------------

    public LineBasedContent() {

        lines = new ArrayList<>();
        bufferSize = DEFAULT_BUFFER_SIZE;
    }

    public LineBasedContent(String s) throws IOException {

        this();
        read(new ByteArrayInputStream(s.getBytes()));
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Overwrites the current content, if any.
     */
    public void read(InputStream is) throws IOException {

        clear();

        BufferedInputStream bis = new BufferedInputStream(is, bufferSize);

        int bytesRead;
        int lineNumber = 1;
        byte[] buffer = new byte[bufferSize];
        boolean lineFeedExpected = false;
        String currentLine = "";

        while((bytesRead = bis.read(buffer, 0, buffer.length)) != -1) {

            //
            // scan for lines
            //

            for(int i = 0; i < bytesRead; i ++) {

                if (lineFeedExpected) {

                    if (buffer[i] == '\n') {

                        lineFeedExpected = false;

                        //
                        // line identified
                        //
                        lines.add(new Line(lineNumber ++, currentLine, "\r\n"));
                        currentLine = "";
                        continue;
                    }
                    else {

                        //
                        // missing '\n', not supported for the time being
                        //
                        throw new IOException("unsupported carriage return combination");
                    }
                }

                if (buffer[i] == '\r') {

                    //
                    // if not followed by '\n' we throw unsupported format exception
                    //
                    lineFeedExpected = true;
                }
                else if (buffer[i] == '\n') {

                    //
                    // line identified
                    //
                    lines.add(new Line(lineNumber ++, currentLine, "\n"));
                    currentLine = "";

                }
                else {

                    currentLine += (char)buffer[i];
                }
            }
        }

        if (lineFeedExpected) {
            //
            // missing '\n', not supported for the time being
            //
            throw new IOException("unsupported carriage return combination");
        }

        if (currentLine.length() > 0) {
            //
            // no terminator at the end of the last line of the file
            //
            lines.add(new Line(lineNumber, currentLine, null));
        }
    }

    /**
     * Writes to the given output stream, irrespective of the "dirty" flag status.
     *
     * If the instance was dirty and the write was successful, the dirty flag is cleared.
     *
     * The content is forcibly flushed on persistent storage before the method returns.
     */
    public void write(OutputStream os) throws IOException {

        synchronized (this) {

            for(Line l: lines) {

                os.write(l.getBytes());
            }

            os.flush();

            for(Line l: lines) {

                l.setDirty(false);
            }
        }
    }

    public void clear() {

        lines.clear();
    }

    /**
     * @exception IndexOutOfBoundsException if the zero-based line number is out of bounds.
     */
    public Line get(int zeroBasedLineNumber) {

        if (zeroBasedLineNumber < 0 || zeroBasedLineNumber >= lines.size()) {
            throw new IndexOutOfBoundsException("no such line " + zeroBasedLineNumber);
        }

        return lines.get(zeroBasedLineNumber);
    }

    public boolean isDirty() {

        for(Line l: lines) {
            if (l.isDirty()) {
                return true;
            }
        }

        return false;
    }

    public int getLineCount() {

        return lines.size();
    }

    /**
     * Replaces the content on the specified line, between the 'from' and 'to' indexes ('to' is the first character
     * that is not touched during replacement) with the new value, contracting or expanding the line.
     *
     * @param zeroBasedLineNumber - note that the line number returned by the StAX Location instance is 1-based,
     *                            so it has to be converted before passed to this method.
     *
     * @return true if a replacement was made, or false if the old content is identical with the new content.
     */
    public boolean replace(int zeroBasedLineNumber, int from, int to, String newValue) {

        Line line = lines.get(zeroBasedLineNumber);
        return line.replace(from, to, newValue);
    }

    /**
     * @return the text content, as currently cached in memory. It may contain changes that are not saved on disk.
     */
    public String getText() {

        //
        // TODO this is an operation that may be called often, optimize by caching the content and discard if modified
        //

        String s = "";

        for(Line l: lines) {

            s += new String(l.getChars());
        }

        return s;
    }

    @Override
    public String toString() {

        return lines == null ? "null" : lines.size() + " lines";
    }

    // Package protected -----------------------------------------------------------------------------------------------

    void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
