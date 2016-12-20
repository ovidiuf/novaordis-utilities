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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/31/16
 */
abstract class OSBase implements OS {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(OSBase.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Package Protected Static ----------------------------------------------------------------------------------------

    /**
     * Splits into tokens, taking into account double quotes.
     */
    static String[] split(String s) {

        List<String> tokens = new ArrayList<>();

        String token = "";
        boolean quoted = false;

        for(int i = 0; i < s.length(); i ++) {

            char c = s.charAt(i);

            if (quoted) {

                if (c == '"') {

                    //
                    // end quotes
                    //

                    tokens.add(token);
                    token = "";
                    quoted = false;
                    continue;
                }
            }
            else if (c == ' ' || c == '\t') {

                if (token.length() > 0) {
                    tokens.add(token);
                    token = "";
                }
                continue;
            }
            else if (c == '"') {

                quoted = true;
                continue;
            }

            token += c;
        }

        //
        // process last token
        //

        if (token.length() > 0) {
            tokens.add(token);
        }

        String[] result = new String[tokens.size()];
        return tokens.toArray(result);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // OS implementation -----------------------------------------------------------------------------------------------

    @Override
    public NativeExecutionResult execute(String command) throws NativeExecutionException {
        return execute(null, command);
    }

    @Override
    public NativeExecutionResult execute(File directory, String command) throws NativeExecutionException {

        if (directory == null) {

            directory = new File(".");
        }

        OS.logExecution(log, directory, command);

        //
        // Linux and MacOS implementations should be identical; for Windows, will override if necessary
        //

        String[] commands = split(command);

        try {

            ProcessBuilder pb = new ProcessBuilder();

            pb.directory(directory);

            Process p = pb.command(commands).start();

            //
            // we'll block this thread waiting for the child process to finish, but before we block, we start
            // threads to consume the process' output and error streams; if we don't do that, there is potential for
            // deadlock, where the child process exhaust stream space and blocks, and we block before even attempting
            // to consume the content. We configure the consumers with the smallest possible buffers (1), so we can
            // display immediately what the process is producing at stdout/stderr without waiting for the buffer to
            // fill. This is useful when trying to understand why a process gets stuck, especially when it blocks
            // reading from stdin.
            //

            int stdoutBufferSize = 1;
            int stderrBufferSize = 1;

            StreamConsumer stdoutStreamConsumer = new StreamConsumer("stdout", p.getInputStream(), stdoutBufferSize);
            StreamConsumer stderrStreamConsumer = new StreamConsumer("stderr", p.getErrorStream(), stderrBufferSize);
            StreamProducer stdinStreamProducer = new StreamProducer(p.getOutputStream());

            stdoutStreamConsumer.start();
            stderrStreamConsumer.start();
            stdinStreamProducer.start();

            //
            // now we can block, the streams will be consumed and the child process won't run out of space
            //

            int exitCode = p.waitFor();

            //
            // wait until the consumer threads unwind by themselves, or we timeout; we do this to avoid dropping
            // in-flight stream content
            //

            if (!stdoutStreamConsumer.waitForShutdown(2000L)) {
                log.warn(stdoutStreamConsumer + " timed out waiting for the end of stream");
            }

            if (!stderrStreamConsumer.waitForShutdown(2000L)) {
                log.warn(stderrStreamConsumer + " timed out waiting for the end of stream");
            }

            String processStdoutContent = stdoutStreamConsumer.read();
            String processStderrContent = stderrStreamConsumer.read();

            return new NativeExecutionResult(exitCode, processStdoutContent, processStderrContent);
        }
        catch(IOException e) {

            //
            // non-existent commands produce this:
            // java.io.Exception: Cannot run program "no-such-command" (in directory "."): error=2, No such file or directory
            //

            return new NativeExecutionResult(127, null, e.getMessage());
        }
        catch(Exception e) {

            throw new NativeExecutionException("failed to execute command \"" + command + "\"", e);
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        String s = getClass().getSimpleName();
        return s.substring(0, s.length() - 2);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
