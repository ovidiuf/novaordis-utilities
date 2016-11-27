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
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertNull;

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

    // get -------------------------------------------------------------------------------------------------------------

    @Test
    public void get_ResolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);



        log.debug(".");
    }

    @Test
    public void get_UnresolvedVariable() throws Exception {

        File f = Util.cp("xml/variables/xml-with-custom-variables.xml", scratchDirectory);

        InLineXmlEditorWithVariableSupport editor = getInLineXmlEditorToTest(f);

        //
        // we don't install any VariableProvider
        //

        assertNull(editor.getVariableProvider());
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
