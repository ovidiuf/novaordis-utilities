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

import io.novaordis.utilities.Files;
import io.novaordis.utilities.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/27/16
 */
public abstract class InLineXmlEditorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(InLineXmlEditorTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    protected File scratchDirectory;
    protected File baseDirectory;

    @Before
    public void before() throws Exception {

        String projectBaseDirName = System.getProperty("basedir");
        scratchDirectory = new File(projectBaseDirName, "target/test-scratch");
        assertTrue(scratchDirectory.isDirectory());

        baseDirectory = new File(System.getProperty("basedir"));
        assertTrue(baseDirectory.isDirectory());
    }

    @After
    public void after() throws Exception {

        //
        // scratch directory cleanup
        //

        assertTrue(io.novaordis.utilities.Files.rmdir(scratchDirectory, false));
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // preconditions ---------------------------------------------------------------------------------------------------

    @Test
    public void fileDoesNotExist() throws Exception {

        File fileThatDoesNotExist = new File("/I/am/sure/this/file/does/not/exist.xml");

        try {

            getInLineXmlEditorToTest(fileThatDoesNotExist);
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("file /I/am/sure/this/file/does/not/exist.xml does not exist", msg);
        }
    }

    @Test
    public void fileCannotBeWritten() throws Exception {

        File fileCannotBeWritten =
                new File(baseDirectory, "src/test/resources/data/xml/file-that-cannot-be-written.xml");

        assertTrue(fileCannotBeWritten.isFile());
        assertTrue(fileCannotBeWritten.canRead());
        assertFalse(fileCannotBeWritten.canWrite());

        try {

            getInLineXmlEditorToTest(fileCannotBeWritten);
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.matches("^file .*src/test/resources/data/xml/file-that-cannot-be-written\\.xml cannot be written"));
        }
    }

    // construction and basic accessors --------------------------------------------------------------------------------

    @Test
    public void constructorAndBasicAccessors() throws Exception {

        String content =
                "<test>\n" +
                        "   <something/>\n" +
                        "</test>";

        File f = new File(scratchDirectory, "test.xml");
        assertTrue(Files.write(f, content));

        InLineXmlEditor editor = getInLineXmlEditorToTest(f);

        assertEquals(3, editor.getLineCount());
        assertEquals(new File(scratchDirectory, "test.xml"), editor.getFile());
        assertFalse(editor.isDirty());
        assertEquals("<test>\n   <something/>\n</test>", editor.getContent());
    }

    // get -------------------------------------------------------------------------------------------------------------

    @Test
    public void get() throws Exception {

        File pomFile = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        String s = ed.get("/level1/level2/level3");
        assertEquals("something", s);
    }

    @Test
    public void get_SkipADeepIncursion() throws Exception {

        File pomFile = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        String s = ed.get("/level1/level2-b/level3-b");
        assertEquals("somethingelse", s);
    }

    @Test
    public void get_NoPath_Partial() throws Exception {

        File pomFile = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        String s = ed.get("/level1/level2/no-such-element");
        assertNull(s);
    }

    @Test
    public void get_MultipleElementsMatchingPath() throws Exception {

        File pomFile = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        String s = ed.get("/level1/list1/list1-element");
        assertEquals("a", s);
    }

    // getList ---------------------------------------------------------------------------------------------------------

    @Test
    public void getList() throws Exception {

        File pomFile = Util.cp("xml/pom-multi-module.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        List<String> modules = ed.getList("/project/modules/module");

        assertEquals(3, modules.size());
        assertEquals("module1", modules.get(0));
        assertEquals("module2", modules.get(1));
        assertEquals("release", modules.get(2));
    }

    // getElements -------------------------------------------------------------------------------------------------

    @Test
    public void getElements_NoSuchPath() throws Exception {

        File pomFile = Util.cp("xml/element-name-list.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        List<XMLElement> l = ed.getElements("/no/such/path");
        assertTrue(l.isEmpty());
    }

    @Test
    public void getElements_ElementHasNoChildren() throws Exception {

        File pomFile = Util.cp("xml/element-name-list.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        List<XMLElement> l = ed.getElements("/root/listB");
        assertTrue(l.isEmpty());
    }

    @Test
    public void getElements_ElementHasNoChildren2() throws Exception {

        File pomFile = Util.cp("xml/element-name-list.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        List<XMLElement> l = ed.getElements("/root/listC");
        assertTrue(l.isEmpty());
    }

    @Test
    public void getElements() throws Exception {

        File pomFile = Util.cp("xml/element-name-list.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        List<XMLElement> l = ed.getElements("/root/listA");

        assertEquals(3, l.size());
        assertEquals("a", l.get(0).getName());
        assertEquals("A", l.get(0).getValue());
        assertEquals("b", l.get(1).getName());
        assertEquals("B", l.get(1).getValue());
        assertEquals("c", l.get(2).getName());
        assertEquals("C", l.get(2).getValue());
    }

    @Test
    public void getElements_MultiLineBlankPaddedValue() throws Exception {

        File pomFile = Util.cp("xml/element-name-list-2.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        List<XMLElement> l = ed.getElements("/root/listA");

        assertEquals(1, l.size());
        assertEquals("a", l.get(0).getName());
        assertEquals("A", l.get(0).getValue());
    }

    @Test
    public void getElements_ValueNotCharacters() throws Exception {

        File pomFile = Util.cp("xml/element-name-list-3.xml", scratchDirectory);

        InLineXmlEditor ed = getInLineXmlEditorToTest(pomFile);

        try {
            ed.getElements("/root/listA");
            fail("should have thrown exception");
        }
        catch(RuntimeException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals("NOT YET IMPLEMENTED (2): cannot process embedded elements yet (/root/listA/a)", msg);
        }
    }


    // set() -----------------------------------------------------------------------------------------------------------

    @Test
    public void set_PathDoesNotExistInFile_Root() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a><b>x</b></a>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        try {
            assertTrue(editor.set("/c", "y"));
            fail("should throw NYE");
        }
        catch(RuntimeException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("NOT YET IMPLEMENTED: we did not find element and we don't know how to add"));
        }

        // when implementing, test this:
//        assertTrue(editor.isDirty());
//        String content = editor.read();
//        assertEquals("<a><b>x</b></a>\n<c>y</c>\n", content);
    }

    @Test
    public void set_PathDoesNotExistInFile_Intermediate() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a><b>x</b></a>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        try {
            assertTrue(editor.set("/a/c", "y"));
            fail("should throw NYE");
        }
        catch(RuntimeException e) {
            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.startsWith("NOT YET IMPLEMENTED: we did not find element"));
        }

        // when implementing, test this:
//        assertTrue(editor.isDirty());
//        String content = editor.read();
//        assertEquals("<a><b>x</b>\n    <c>y</c>\n</a>\n", content);
    }

    @Test
    public void set_EssentialValuesDoNotDiffer() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");

        String content = "<root><a>  ?    </a></root>\n";

        Files.write(xmlFile, content);

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertFalse(editor.set("/root/a", "?"));
    }

    @Test
    public void set_EssentialValuesDoNotDiffer_MultiLine() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");

        String content =
                "" +
                        "<root>\n" +
                        "      <a>\n" +
                        "               ?   \n" +
                        "      </a>\n" +
                        "</root>";

        Files.write(xmlFile, content);

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertFalse(editor.set("/root/a", "?"));
    }

    @Test
    public void set_PreservesSurroundingSpace_OneLine_PaddingDiffers() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");

        String content = "<root><a>  ?    </a></root>\n";

        Files.write(xmlFile, content);

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertTrue(editor.set("/root/a", ".."));

        String modifiedContent = editor.getContent();

        String expected = "<root><a>  ..    </a></root>\n";

        assertEquals(expected, modifiedContent);
    }

    @Test
    public void set_PreservesSurroundingSpace_MultiLine() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");

        String content =
                "" +
                        "<root>\n" +
                        "      <a>\n" +
                        "               ?   \n" +
                        "      </a>\n" +
                        "</root>";

        Files.write(xmlFile, content);

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertTrue(editor.set("/root/a", "!"));

        String modifiedContent = editor.getContent();

        String expected =
                "" +
                        "<root>\n" +
                        "      <a>\n" +
                        "               !   \n" +
                        "      </a>\n" +
                        "</root>";

        assertEquals(expected, modifiedContent);
    }

    // save ------------------------------------------------------------------------------------------------------------

    @Test
    public void save_NotDirty() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<root/>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertEquals("<root/>", Files.read(xmlFile));

        assertFalse(editor.isDirty());

        //
        // change the file on disk, then save. Saving should not have any effect
        //

        Files.write(xmlFile, "<blah/>");

        assertEquals("<blah/>", Files.read(xmlFile));

        //
        // save, nothing should happen
        //

        assertFalse(editor.save());

        assertEquals("<blah/>", Files.read(xmlFile));
    }

    @Test
    public void save_Dirty() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<root><a>?</a></root>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertFalse(editor.isDirty());

        //
        // change the file on disk, then save. It should overwrite.
        //

        Files.write(xmlFile, "<blah/>");
        assertEquals("<blah/>", Files.read(xmlFile));

        assertTrue(editor.set("/root/a", "!"));

        assertTrue(editor.save());

        assertEquals("<root><a>!</a></root>", Files.read(xmlFile));
    }

    @Test
    public void save_Dirty_MultiLine() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile,
                "<root>\n" +
                        "      <a>\n" +
                        "               ?   \n" +
                        "      </a>\n" +
                        "</root>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertFalse(editor.isDirty());

        //
        // change the file on disk, then save. It should overwrite.
        //

        Files.write(xmlFile, "<blah/>");
        assertEquals("<blah/>", Files.read(xmlFile));

        assertTrue(editor.set("/root/a", "!"));

        assertTrue(editor.save());

        String expected =
                "<root>\n" +
                        "      <a>\n" +
                        "               !   \n" +
                        "      </a>\n" +
                        "</root>";

        assertEquals(expected, Files.read(xmlFile));
    }

    // undo ------------------------------------------------------------------------------------------------------------

    @Test
    public void undo_NoSave() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<root/>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        // noop
        assertFalse(editor.undo());

        // another noop
        assertFalse(editor.undo());
    }

    @Test
    public void undo_SaveNotDirty() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<root/>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertFalse(editor.save());

        // noop
        assertFalse(editor.undo());

        // another noop
        assertFalse(editor.undo());
    }

    @Test
    public void undo_SaveDirty() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a>x</a>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertTrue(editor.set("/a", "y"));

        assertTrue(editor.save());

        assertEquals("<a>y</a>", Files.read(xmlFile));

        assertTrue(editor.undo());

        assertEquals("<a>x</a>", Files.read(xmlFile));
        assertEquals("x", editor.get("/a"));

        // noop
        assertFalse(editor.undo());

        assertEquals("<a>x</a>", Files.read(xmlFile));

        // another noop
        assertFalse(editor.undo());

        assertEquals("<a>x</a>", Files.read(xmlFile));
    }

    @Test
    public void undo_SaveDirty_TwoSets() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a>x</a>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertTrue(editor.set("/a", "y"));

        assertTrue(editor.set("/a", "z"));

        assertTrue(editor.save());

        assertEquals("<a>z</a>", Files.read(xmlFile));

        assertTrue(editor.undo());

        assertEquals("<a>x</a>", Files.read(xmlFile));
        assertEquals("x", editor.get("/a"));

        // noop
        assertFalse(editor.undo());

        assertEquals("<a>x</a>", Files.read(xmlFile));

        // another noop
        assertFalse(editor.undo());

        assertEquals("<a>x</a>", Files.read(xmlFile));
    }

    @Test
    public void undo_TwoSuccessiveDirtySaves_UndoRestoresJustTheLastOne() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a>x</a>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertTrue(editor.set("/a", "y"));

        assertTrue(editor.save());

        assertEquals("<a>y</a>", Files.read(xmlFile));

        assertTrue(editor.set("/a", "z"));

        assertTrue(editor.save());

        assertEquals("<a>z</a>", Files.read(xmlFile));

        assertTrue(editor.undo());

        assertEquals("<a>y</a>", Files.read(xmlFile));

        // noop
        assertFalse(editor.undo());

        assertEquals("<a>y</a>", Files.read(xmlFile));

        // another noop
        assertFalse(editor.undo());

        assertEquals("<a>y</a>", Files.read(xmlFile));
    }

    @Test
    public void undo_ThreeSuccessiveDirtySaves_UndoRestoresJustTheLastOne() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a>0</a>");

        InLineXmlEditor editor = getInLineXmlEditorToTest(xmlFile);

        assertTrue(editor.set("/a", "1"));
        assertTrue(editor.save());
        assertEquals("<a>1</a>", Files.read(xmlFile));

        assertTrue(editor.set("/a", "2"));
        assertTrue(editor.save());
        assertEquals("<a>2</a>", Files.read(xmlFile));

        assertTrue(editor.set("/a", "3"));
        assertTrue(editor.save());
        assertEquals("<a>3</a>", Files.read(xmlFile));

        assertTrue(editor.undo());

        assertEquals("<a>2</a>", Files.read(xmlFile));

        // noop
        assertFalse(editor.undo());

        assertEquals("<a>2</a>", Files.read(xmlFile));

        // another noop
        assertFalse(editor.undo());

        assertEquals("<a>2</a>", Files.read(xmlFile));
    }

    // end to end ------------------------------------------------------------------------------------------------------

    @Test
    public void simpleXmlFileEndToEnd() throws Exception {

        File sampleFile = Util.cp(
                baseDirectory, "src/test/resources/data/xml/simple.xml", scratchDirectory, "simple-copy.xml");

        InLineXmlEditor editor = getInLineXmlEditorToTest(sampleFile);

        assertFalse(editor.isDirty());
        assertEquals(3, editor.getLineCount());

        //
        // attempt to overwrite a value with the same
        //

        assertFalse(editor.set("/example/color", "red"));
        assertFalse(editor.isDirty());

        //
        // attempt to overwrite a value with a different one
        //

        assertTrue(editor.set("/example/color", "blue"));
        assertTrue(editor.isDirty());

        editor.save();

        assertFalse(editor.isDirty());

        //
        // make sure the change went to disk
        //

        InLineXmlEditor editor2 = getInLineXmlEditorToTest(sampleFile);

        String s = editor2.get("/example/color");

        assertEquals("blue", s);
    }

    @Test
    public void endToEnd() throws Exception {

        File sampleFile = Util.cp(
                baseDirectory, "src/test/resources/data/xml/pom-sample.xml", scratchDirectory, "pom-sample.xml");

        InLineXmlEditor editor = getInLineXmlEditorToTest(sampleFile);

        assertFalse(editor.isDirty());
        assertEquals(28, editor.getLineCount());

        //
        // attempt to overwrite a value with the same
        //

        assertFalse(editor.set("/project/version", "1.0.0-SNAPSHOT-1"));
        assertFalse(editor.isDirty());

        //
        // attempt to overwrite a value with a different one
        //

        assertTrue(editor.set("/project/version", "1.0.0-SNAPSHOT-2"));
        assertTrue(editor.isDirty());

        editor.save();

        assertFalse(editor.isDirty());

        //
        // make sure the change went to disk
        //

        InLineXmlEditor editor2 = getInLineXmlEditorToTest(sampleFile);

        String s = editor2.get("/project/version");

        assertEquals("1.0.0-SNAPSHOT-2", s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    protected abstract InLineXmlEditor getInLineXmlEditorToTest(File xmlFile) throws Exception;

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
