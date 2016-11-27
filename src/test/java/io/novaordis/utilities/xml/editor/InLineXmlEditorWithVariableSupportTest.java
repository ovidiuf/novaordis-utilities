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
import io.novaordis.utilities.variable.VariableProvider;
import io.novaordis.utilities.variable.VariableProviderImpl;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/10/16
 */
public class InLineXmlEditorWithVariableSupportTest extends InLineXmlEditorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(InLineXmlEditorWithVariableSupportTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Tests -----------------------------------------------------------------------------------------------------------

    // variable provider accessor/mutator ------------------------------------------------------------------------------

    @Test
    public void getVariableProvider_setVariableProvider() throws Exception {

        File f = Util.cp("xml/simple.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        assertNull(editor.getVariableProvider());

        VariableProvider p = new VariableProviderImpl();

        editor.setVariableProvider(p);

        assertEquals(p, editor.getVariableProvider());
    }

    // get -------------------------------------------------------------------------------------------------------------

    @Test
    public void get_UnresolvedVariable_NoVariableProvider() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        //
        // we don't install any VariableProvider
        //

        assertNull(editor.getVariableProvider());

        String s = editor.get("/root/blue");
        assertEquals("${something}", s);
    }

    @Test
    public void get_BrokenVariable_NoVariableProvider() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        assertNull(editor.getVariableProvider());

        try {
            editor.get("/root/red");
            fail("should throw exception");
        }
        catch(Exception e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("path /root/red contains an invalid variable reference '${broken.variable'", msg);
        }
    }

    @Test
    public void get_UnresolvedVariable_NoSuchVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        VariableProviderImpl p = new VariableProviderImpl();
        assertNull(p.getValue("something"));

        editor.setVariableProvider(p);

        String s = editor.get("/root/blue");
        assertEquals("${something}", s);
    }

    @Test
    public void get_BrokenVariable_VariableProviderInstalled() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        VariableProviderImpl p = new VariableProviderImpl();

        editor.setVariableProvider(p);

        try {
            editor.get("/root/red");
            fail("should throw exception");
        }
        catch(Exception e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals("path /root/red contains an invalid variable reference '${broken.variable'", msg);
        }
    }

    @Test
    public void get_ResolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        VariableProviderImpl p = new VariableProviderImpl();
        p.setValue("something", "bright");

        editor.setVariableProvider(p);

        String s = editor.get("/root/blue");
        assertEquals("bright", s);
    }

    // getList ---------------------------------------------------------------------------------------------------------

    @Test
    public void getList_UnresolvedVariable_NoVariableProvider() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        //
        // we don't install any VariableProvider
        //

        assertNull(editor.getVariableProvider());

        List<String> s = editor.getList("/root/list/element");

        assertEquals(3, s.size());
        assertEquals("A", s.get(0));
        assertEquals("${element.variable}", s.get(1));
        assertEquals("C", s.get(2));
    }

    @Test
    public void getList_BrokenVariable_NoVariableProvider() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        assertNull(editor.getVariableProvider());

        try {
            editor.getList("/root/list2/element2");
            fail("should throw exception");
        }
        catch(Exception e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals(
                    "path /root/list2/element2 contains an invalid variable reference '${broken.element.variable'",
                    msg);
        }
    }

    @Test
    public void getList_UnresolvedVariable_NoSuchVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        VariableProviderImpl p = new VariableProviderImpl();
        assertNull(p.getValue("element.variable"));

        editor.setVariableProvider(p);

        List<String> s = editor.getList("/root/list/element");

        assertEquals(3, s.size());
        assertEquals("A", s.get(0));
        assertEquals("${element.variable}", s.get(1));
        assertEquals("C", s.get(2));
    }

    @Test
    public void getList_BrokenVariable_VariableProviderInstalled() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        VariableProviderImpl p = new VariableProviderImpl();

        editor.setVariableProvider(p);

        try {
            editor.getList("/root/list2/element2");
            fail("should throw exception");
        }
        catch(Exception e) {
            String msg = e.getMessage();
            log.info(msg);
            assertEquals(
                    "path /root/list2/element2 contains an invalid variable reference '${broken.element.variable'",
                    msg);
        }
    }

    @Test
    public void getList_ResolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        VariableProviderImpl p = new VariableProviderImpl();
        p.setValue("element.variable", "B");

        editor.setVariableProvider(p);

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

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

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
    protected InLineXmlEditorWithVariableSupport getInLineXmlEditorToTest(File xmlFile) throws Exception {

        return new InLineXmlEditorWithVariableSupport(xmlFile);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
