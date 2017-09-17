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

import io.novaordis.utilities.expressions.IllegalReferenceException;
import io.novaordis.utilities.expressions.Scope;
import io.novaordis.utilities.expressions.ScopeImpl;
import io.novaordis.utilities.expressions.VariableReference;
import io.novaordis.utilities.expressions.VariableReferenceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * An InLineXMLEditor implementation that is capable of handling variables in the XML content. It does that by querying
 * and possibly modifying the contents of a Scope. If no variable scope is installed, then the editor will fall back to
 * the functionality of a basic editor and won't interact with variables in any way, leaving them unresolved.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/27/16
 */
public class VariableAwareInLineXMLEditor implements InLineXMLEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(VariableAwareInLineXMLEditor.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private BasicInLineXMLEditor delegateEditor;

    private Scope variables;

    // Constructors ----------------------------------------------------------------------------------------------------

    public VariableAwareInLineXMLEditor(File xmlFile) throws IOException {

        this.delegateEditor = new BasicInLineXMLEditor(xmlFile);

        //
        // we start with an empty scope, we need a non-null scope here to detect variable reference syntax errors
        //
        this.variables = new ScopeImpl();

        log.debug(this + " constructed");
    }

    // InLineXMLEditor implementation ----------------------------------------------------------------------------------

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
    public List<XMLElement> getElements(String path) {

        //
        // there are no variables in the element names, delegate
        //
        return delegateEditor.getElements(path);
    }

    /**
     * @exception IllegalStateException if encountering problems with the variable references.
     */
    @Override
    public String get(String path) {

        String s = delegateEditor.get(path);

        if (s == null) {

            return null;
        }

        try {

            //
            // 'variables' is never null
            //

            return variables.evaluate(s);
        }
        catch(IllegalReferenceException e) {

            String msg = e.getMessage();
            String variableName = e.getVariableName();
            msg = "path " + path + " contains an invalid variable reference '" + variableName + "': " + msg;
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public List<String> getList(String path) {

        List<String> elements = delegateEditor.getList(path);

        if (elements.isEmpty()) {

            return elements;
        }

        for(int i = 0; i < elements.size(); i ++) {

            String element = elements.get(i);

            String element2;

            try {

                //
                // 'variables' is never null
                //

                element2 = variables.evaluate(element);
            }
            catch(IllegalReferenceException e) {

                String msg = e.getMessage();
                String variableName = e.getVariableName();
                msg = "path " + path + " contains an invalid variable reference '" + variableName + "': " + msg;
                throw new IllegalStateException(msg);
            }

            if (!element2.equals(element)) {

                elements.set(i, element2);
            }
        }

        return elements;
    }

    @Override
    public boolean set(String path, String newValue) {

        String current = delegateEditor.get(path);

        if (current != null) {

            List<VariableReference> references = new VariableReferenceResolver().getVariableReferences(current);

            //
            // if the path value contains variable references ...
            //

            if (!references.isEmpty()) {

                throw new RuntimeException(
                        "NOT YET IMPLEMENTED: we don't support yet writing content that references variables");
            }
        }

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
    public Scope getScope() {

        return variables;
    }

    public void setScope(Scope s) {

        //
        // we cannot install a null scope
        //

        if (s == null) {

            throw new IllegalArgumentException("null scope");
        }

        this.variables = s;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
