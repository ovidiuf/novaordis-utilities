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

import io.novaordis.utilities.variable.VariableProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * An InLineXmlEditor implementation that is capable of handling variables in the XML content. It does that by querying
 * and possibly modifying the contents of a VariableProvider. If no variable provider is installed, then the editor
 * will fall back to the functionality of a basic editor and won't interact with variables in any way, leaving them
 * unresolved.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/27/16
 */
public class InLineXmlEditorWithVariableSupport implements InLineXmlEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private BasicInLineXmlEditor delegateEditor;
    private VariableProvider variableProvider;

    // Constructors ----------------------------------------------------------------------------------------------------

    public InLineXmlEditorWithVariableSupport(File xmlFile) throws IOException {

        this.delegateEditor = new BasicInLineXmlEditor(xmlFile);
    }

    // InLineXmlEditor implementation ----------------------------------------------------------------------------------

    @Override
    public File getFile() {

        return delegateEditor.getFile();
    }

    @Override
    public int getLineCount() {

        return delegateEditor.getLineCount();
    }

    @Override
    public boolean isDirty() {

        return delegateEditor.isDirty();
    }

    @Override
    public String getContent() {

        return delegateEditor.getContent();
    }

    @Override
    public String get(String path) {

        return delegateEditor.get(path);
    }

    @Override
    public List<String> getList(String path) {

        return delegateEditor.getList(path);
    }

    @Override
    public boolean set(String path, String newValue) {

        return delegateEditor.set(path, newValue);
    }

    @Override
    public boolean save() throws IOException {

        return delegateEditor.save();
    }

    @Override
    public boolean undo() throws IOException {

        return delegateEditor.undo();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return may return null
     */
    public VariableProvider getVariableProvider() {

        return variableProvider;
    }

    public void setVariableProvider(VariableProvider variableProvider) {

        this.variableProvider = variableProvider;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
