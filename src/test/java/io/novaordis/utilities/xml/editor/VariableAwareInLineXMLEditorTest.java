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

import io.novaordis.utilities.Util;
import io.novaordis.utilities.expressions.Scope;
import io.novaordis.utilities.expressions.ScopeImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class VariableAwareInLineXMLEditorTest extends InLineXMLEditorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(VariableAwareInLineXMLEditorTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // variable provider accessor/mutator ------------------------------------------------------------------------------

    @Test
    public void getScope_setScope() throws Exception {

        File f = Util.cp("xml/simple.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope s = new ScopeImpl();

        editor.setScope(s);

        assertEquals(s, editor.getScope());
    }

    @Test
    public void setScope_Null() throws Exception {

        File f = Util.cp("xml/simple.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        try {

            editor.setScope(null);
            fail("should have thrown exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("null scope"));
        }
    }

    // get -------------------------------------------------------------------------------------------------------------

    @Test
    public void get_UnresolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        String s = editor.get("/root/blue");
        assertEquals("${something}", s);
    }

    @Test
    public void get_BrokenVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        try {

            editor.get("/root/red");
            fail("should throw exception");
        }
        catch(Exception e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("path /root/red contains an invalid variable reference 'broken.variable'"));
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void get_UnresolvedVariable_NoSuchVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope sc = new ScopeImpl();

        assertNull(sc.getVariable("something"));

        editor.setScope(sc);

        String s = editor.get("/root/blue");
        assertEquals("${something}", s);
    }

    @Test
    public void get_BrokenVariable_VariableProviderInstalled() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope sc = new ScopeImpl();

        editor.setScope(sc);

        try {

            editor.get("/root/red");
            fail("should throw exception");
        }
        catch(IllegalStateException e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("path /root/red contains an invalid variable reference"));
            assertTrue(msg.contains("broken.variable"));
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void get_ResolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope sc = new ScopeImpl();

        sc.declare("something", "bright");

        editor.setScope(sc);

        String s = editor.get("/root/blue");
        assertEquals("bright", s);
    }

    // getList ---------------------------------------------------------------------------------------------------------

    @Test
    public void getList_UnresolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        List<String> s = editor.getList("/root/list/element");

        assertEquals(3, s.size());
        assertEquals("A", s.get(0));
        assertEquals("${element.variable}", s.get(1));
        assertEquals("C", s.get(2));
    }

    @Test
    public void getList_BrokenVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        try {

            editor.getList("/root/list2/element2");
            fail("should throw exception");
        }
        catch(Exception e) {

            String msg = e.getMessage();
            assertTrue(msg.contains("path /root/list2/element2 contains an invalid variable reference"));
            assertTrue(msg.contains("broken.element.variable"));
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void getList_UnresolvedVariable_NoSuchVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope sc = new ScopeImpl();

        assertNull(sc.getVariable("element.variable"));

        editor.setScope(sc);

        List<String> s = editor.getList("/root/list/element");

        assertEquals(3, s.size());
        assertEquals("A", s.get(0));
        assertEquals("${element.variable}", s.get(1));
        assertEquals("C", s.get(2));
    }

    @Test
    public void getList_BrokenVariable_VariableProviderInstalled() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope sc = new ScopeImpl();

        editor.setScope(sc);

        try {

            editor.getList("/root/list2/element2");
            fail("should throw exception");
        }
        catch(Exception e) {

            String msg = e.getMessage();
            assertTrue(msg.contains(
                    "path /root/list2/element2 contains an invalid variable reference 'broken.element.variable'"));
            assertTrue(msg.contains("unbalanced '{' in variable reference"));
        }
    }

    @Test
    public void getList_ResolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        Scope sc = new ScopeImpl();

        sc.declare("element.variable", "B");

        editor.setScope(sc);

        List<String> s = editor.getList("/root/list/element");

        assertEquals(3, s.size());
        assertEquals("A", s.get(0));
        assertEquals("B", s.get(1));
        assertEquals("C", s.get(2));
    }

    // set -------------------------------------------------------------------------------------------------------------

    @Test
    public void set_ValueContainsVariable() throws Exception {

        //
        // we don't handle this case yet, but this is a reminder to add tests when we implement support for it
        //

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        VariableAwareInLineXMLEditor editor = getInLineXmlEditorToTest(f);

        try {

            editor.set("/root/blue", "blah");
            fail("should have failed");
        }
        catch(RuntimeException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("NOT YET IMPLEMENTED: we don't support yet writing content that references variables", msg);
        }
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected VariableAwareInLineXMLEditor getInLineXmlEditorToTest(File xmlFile) throws Exception {

        return new VariableAwareInLineXMLEditor(xmlFile);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
