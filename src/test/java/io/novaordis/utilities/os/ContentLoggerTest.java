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

import org.junit.Test;
import org.slf4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 12/19/16
 */
public class ContentLoggerTest {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // identity --------------------------------------------------------------------------------------------------------

    @Test
    public void identity() throws Exception {

        ContentLogger l = new ContentLogger("test", 2);

        Logger log = l.getLog();
        assertNotNull(log);
        assertFalse(log instanceof MockLogger);
        assertEquals(2, l.getBufferSize());

        MockLogger ml = new MockLogger();
        l.setLog(ml);
        assertEquals(ml, l.getLog());

        assertEquals("test", l.getName());
    }

    // logContentAccumulatedSoFar() ------------------------------------------------------------------------------------

    @Test
    public void logContentAccumulatedSoFar_NothingHasAccumulated() throws Exception {

        ContentLogger l = new ContentLogger("test", 2);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.logContentAccumulatedSoFar();

        assertNull(ml.getDebugContent());

        l.logContentAccumulatedSoFar();

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logContentAccumulatedSoFar_NoNewLine() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log('a');

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log('b');

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("b", s2);

        assertNull(ml.getDebugContent());

        l.log('c');
        l.log('d');

        l.logContentAccumulatedSoFar();

        String s3 = ml.getDebugContent();
        assertEquals("cd", s3);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logContentAccumulatedSoFar_OneNewLine() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log('a');
        l.log('\n');

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log('b');

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("b", s2);

        assertNull(ml.getDebugContent());

        l.log('c');
        l.log('d');

        l.logContentAccumulatedSoFar();

        String s3 = ml.getDebugContent();
        assertEquals("cd", s3);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logContentAccumulatedSoFar_OneNewLineAndExtraCharacters() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log('a');
        l.log('\n');
        l.log('b');
        l.log('c');
        l.log('d');

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log('e');

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("bcde", s2);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logContentAccumulatedSoFar_TwoNewLines() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log('a');
        l.log('\n');
        l.log('b');
        l.log('\n');

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a\nb", s);

        assertNull(ml.getDebugContent());

        l.log('e');

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("e", s2);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logContentAccumulatedSoFar_TwoNewLines_ExtraChars() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log('a');
        l.log('b');
        l.log('c');
        l.log('\n');
        l.log('e');
        l.log('f');
        l.log('g');
        l.log('\n');
        l.log('h');
        l.log('i');

        assertEquals(10, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("abc\nefg", s);

        assertNull(ml.getDebugContent());

        l.log('j');

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("hij", s2);

        assertEquals(10, l.getBufferSize());

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logContentAccumulatedSoFar_BufferExtension() throws Exception {

        ContentLogger l = new ContentLogger("test", 2);
        MockLogger ml = new MockLogger();
        l.setLog(ml);
        assertEquals(2, l.getBufferSize());

        l.log('a');
        assertEquals(2, l.getBufferSize());
        l.log('b');
        assertEquals(2, l.getBufferSize());
        l.log('c');
        assertEquals(4, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("abc", s);

        assertNull(ml.getDebugContent());

        l.log('d');
        assertEquals(4, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("d", s2);
    }

    // log(byte[]) -----------------------------------------------------------------------------------------------------

    @Test
    public void logByteArray_NoNewLine() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();

        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'b'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("b", s2);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'c', 'd'}, 0, 2);

        l.logContentAccumulatedSoFar();

        String s3 = ml.getDebugContent();
        assertEquals("cd", s3);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_OneNewLine() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a', '\n'}, 0, 2);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'b'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("b", s2);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'c', 'd'}, 0, 2);

        l.logContentAccumulatedSoFar();

        String s3 = ml.getDebugContent();
        assertEquals("cd", s3);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_OneNewLine_CharByChar() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a'}, 0, 1);
        l.log(new byte[]{'\n'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'b'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("b", s2);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'c'}, 0, 1);
        l.log(new byte[]{'d'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s3 = ml.getDebugContent();
        assertEquals("cd", s3);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_OneNewLineAndExtraCharacters() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a', '\n', 'b', 'c', 'd'}, 0, 5);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'e'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("bcde", s2);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_OneNewLineAndExtraCharacters_CharByChar() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a'}, 0, 1);
        l.log(new byte[]{'\n'}, 0, 1);
        l.log(new byte[]{'b'}, 0, 1);
        l.log(new byte[]{'c'}, 0, 1);
        l.log(new byte[]{'d'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'e'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("bcde", s2);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_TwoNewLines() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a', '\n', 'b', '\n'}, 0, 4);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a\nb", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'e'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("e", s2);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_TwoNewLines_CharByChar() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a'}, 0, 1);
        l.log(new byte[]{'\n'}, 0, 1);
        l.log(new byte[]{'b'}, 0, 1);
        l.log(new byte[]{'\n'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("a\nb", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'e'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("e", s2);

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_TwoNewLines_ExtraChars() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a', 'b', 'c', '\n', 'e', 'f', 'g', '\n', 'h', 'i'}, 0, 10);

        assertEquals(10, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("abc\nefg", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'j'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("hij", s2);

        assertEquals(10, l.getBufferSize());

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_TwoNewLines_ExtraChars_CharByChar() throws Exception {

        ContentLogger l = new ContentLogger("test", 10);
        MockLogger ml = new MockLogger();
        l.setLog(ml);

        l.log(new byte[]{'a'}, 0, 1);
        l.log(new byte[]{'b'}, 0, 1);
        l.log(new byte[]{'c'}, 0, 1);
        l.log(new byte[]{'\n'}, 0, 1);
        l.log(new byte[]{'e'}, 0, 1);
        l.log(new byte[]{'f'}, 0, 1);
        l.log(new byte[]{'g'}, 0, 1);
        l.log(new byte[]{'\n'}, 0, 1);
        l.log(new byte[]{'h'}, 0, 1);
        l.log(new byte[]{'i'}, 0, 1);

        assertEquals(10, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("abc\nefg", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'j'}, 0, 1);

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("hij", s2);

        assertEquals(10, l.getBufferSize());

        assertNull(ml.getDebugContent());
    }

    @Test
    public void logByteArray_BufferExtension() throws Exception {

        ContentLogger l = new ContentLogger("test", 2);
        MockLogger ml = new MockLogger();
        l.setLog(ml);
        assertEquals(2, l.getBufferSize());

        l.log(new byte[]{'a'}, 0, 1);
        assertEquals(2, l.getBufferSize());
        l.log(new byte[]{'b'}, 0, 1);
        assertEquals(2, l.getBufferSize());
        l.log(new byte[]{'c'}, 0, 1);
        assertEquals(4, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s = ml.getDebugContent();
        assertEquals("abc", s);

        assertNull(ml.getDebugContent());

        l.log(new byte[]{'d'}, 0, 1);
        assertEquals(4, l.getBufferSize());

        l.logContentAccumulatedSoFar();

        String s2 = ml.getDebugContent();
        assertEquals("d", s2);
    }

    // production ------------------------------------------------------------------------------------------------------

    @Test
    public void timerThreadLeak() throws Exception {

        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Integer initialThreadCount = (Integer)mBeanServer.
                getAttribute(new ObjectName("java.lang:type=Threading"), "ThreadCount");

        ContentLogger cl = new ContentLogger("logger 1");

        Integer tc = (Integer)mBeanServer.getAttribute(new ObjectName("java.lang:type=Threading"), "ThreadCount");

        //
        // the logger started a new timer thread, so we should see a difference of 1 here
        //

        assertEquals(1, tc - initialThreadCount);

        ContentLogger cl2 = new ContentLogger("logger 2");

        Integer tc2 = (Integer)mBeanServer.getAttribute(new ObjectName("java.lang:type=Threading"), "ThreadCount");

        assertEquals(1, tc2 - tc);

        cl.cancel();
        cl2.cancel();

        //noinspection UnusedAssignment
        cl = null;


        //noinspection UnusedAssignment
        cl2 = null;

        System.gc();

        Integer finalThreadCount = (Integer)mBeanServer.
                getAttribute(new ObjectName("java.lang:type=Threading"), "ThreadCount");


        int delta = finalThreadCount - initialThreadCount;
        assertEquals(0, delta);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
