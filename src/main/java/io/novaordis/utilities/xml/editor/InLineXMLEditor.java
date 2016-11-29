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

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * InLineXMLEditor is an API that can be used to modify XML files on disk directly from Java programs.
 *
 * Depending on the implementation, it may or may not handle variables.
 *
 * Currently NOT thread safe, must be accessed and used from a single thread at a time.
 *
 * For more details see https://kb.novaordis.com/index.php/In-Line_XML_Editor
 *
 * @see BasicInLineXMLEditor
 * @see VariableAwareInLineXMLEditor
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/27/16
 */
public interface InLineXMLEditor {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    File getFile();

    int getLineCount();

    boolean isDirty();

    /**
     * @return the text representation of the underlying file, as currently cached in memory. If the instance is
     * dirty, the content does contain changes that are not saved on disk.
     *
     * @see InLineXMLEditor#isDirty()
     */
    String getContent();

    /**
     * @return the String associated with the first element that matched the path. Note that if more than one element
     * matches the path, only the first value will be returned. Will return null if no element matches the path.
     *
     * Implementations may choose to throw unchecked exceptions if invalidity conditions specific to the implementation
     * are detected. Consult implementation documentation for details.
     *
     * @see InLineXMLEditor#getList(String)
     */
    String get(String path);

    /**
     * @return the Strings associated with the all elements that matched the path, in the order in which the elements
     * were matched in the underlying document. Will return an empty list if no element matches the path.
     *
     * Implementations may choose to throw unchecked exceptions if invalidity conditions specific to the implementation
     * are detected. Consult implementation documentation for details.
     *
     * @see InLineXMLEditor#get(String)
     */
    List<String> getList(String path);

    /**
     * @return the XMLElement instances of the first-level child elements for the given path, or an empty list if the
     * path does not exist or it does not have any element children.
     */
    List<XMLElement> getElements(String path);

    /**
     * Updates the value of the element/attribute indicated by the path with the given string.
     *
     * If the path does not exist, it will be created.
     *
     * @return true if an actual update occurred, false if the existing value is identical with the new value.
     */
    boolean set(String path, String newValue);

    /**
     * Writes the in-memory updates (if any) into the file.
     *
     * @return true if state was changed on disk as result of the save operation, false otherwise.
     *
     * @see InLineXMLEditor#undo()
     */
    boolean save() throws IOException;

    /**
     * Reverts the effects on disk of the <b>last</b> save() operation (if any), by restoring the underlying file
     * and the corresponding memory state to the version available before the save() operation.
     *
     * If there was no previous save() operation, undo() is a noop and returns false.
     *
     * If there was a previous save() operation, but the operation did not change state (un-consequential save()),
     * undo() will be a noop and will return false.
     *
     * If there were previous save() operations and the operations changed state on disk, undo() will revert the
     * underlying file and the corresponding memory state to the version present on disk before the <b>last</b> save()
     * operation, and will return true. All subsequent undo() calls after that (unless a new consequential save()
     * operation is performed) will be noops and will return false.
     *
     * @return true if disk state was changed as result of the last undo() operation, false otherwise.
     *
     * @see InLineXMLEditor#save()
     *
     * @throws IOException
     */
    boolean undo() throws IOException;

}
