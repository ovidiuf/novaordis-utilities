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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
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
 * @since 11/10/16
 */
public class BasicInLineXmlEditorTest extends InLineXmlEditorTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(BasicInLineXmlEditorTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    private File scratchDirectory;
    private File baseDirectory;

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

    // preconditions fail ----------------------------------------------------------------------------------------------

    @Test
    public void fileDoesNotExist() throws Exception {

        File doesNotExist = new File("/I/am/sure/this/file/does/not/exist.xml");

        try {
            new BasicInLineXmlEditor(doesNotExist);
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

        File sampleFile =
                new File(System.getProperty("basedir"), "src/test/resources/data/xml/file-that-cannot-be-written.xml");

        assertTrue(sampleFile.isFile());
        assertTrue(sampleFile.canRead());
        assertFalse(sampleFile.canWrite());

        try {
            new BasicInLineXmlEditor(sampleFile);
            fail("should throw exception");
        }
        catch(IOException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertTrue(msg.matches("^file .*src/test/resources/data/xml/file-that-cannot-be-written\\.xml cannot be written"));
        }
    }

    // constructor -----------------------------------------------------------------------------------------------------

    @Test
    public void constructor_accessors() throws Exception {

        String content =
                "<test>\n" +
                        "   <something/>\n" +
                        "</test>";

        File f = new File(scratchDirectory, "test.xml");
        assertTrue(Files.write(f, content));

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(f);

        assertEquals(3, editor.getLineCount());
        assertFalse(editor.isDirty());
        assertEquals(new File(scratchDirectory, "test.xml"), editor.getFile());

        assertEquals("<test>\n   <something/>\n</test>", editor.getContent());
    }

    // normalize -------------------------------------------------------------------------------------------------------

    @Test
    public void normalize() throws Exception {

        String s = BasicInLineXmlEditor.normalize("/a/b/c");
        assertEquals("/a/b/c", s);
    }

    @Test
    public void normalize2() throws Exception {

        String s = BasicInLineXmlEditor.normalize("/a/b/c/");
        assertEquals("/a/b/c", s);
    }

    @Test
    public void split3() throws Exception {

        String s = BasicInLineXmlEditor.normalize("a/b/c/");
        assertEquals("/a/b/c", s);
    }

    // walk ------------------------------------------------------------------------------------------------------------

    @Test
    public void walk() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.walk("/level1/level2/level3");

        assertEquals(1, matches.size());

        XmlContext ctx = matches.get(0);

        XMLEvent c = ctx.getCurrent();

        assertTrue(c.isCharacters());
        String s = c.asCharacters().getData().trim();

        assertEquals("something", s);

        XMLEvent p = ctx.getPrevious();

        assertTrue(p.isStartElement());
        StartElement se = p.asStartElement();
        assertEquals("level3", se.getName().getLocalPart());

        assertEquals("/level1/level2/level3", ctx.getXmlContentPath());
    }

    @Test
    public void walk_SkipADeepIncursion() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.walk("/level1/level2-b/level3-b");

        assertEquals(1, matches.size());

        XmlContext ctx = matches.get(0);

        XMLEvent c = ctx.getCurrent();

        assertTrue(c.isCharacters());

        String s = c.asCharacters().getData().trim();

        assertEquals("somethingelse", s);

        XMLEvent p = ctx.getPrevious();

        assertTrue(p.isStartElement());
        StartElement se = p.asStartElement();
        assertEquals("level3-b", se.getName().getLocalPart());

        assertEquals("/level1/level2-b/level3-b", ctx.getXmlContentPath());
    }

    @Test
    public void walk_NoPath_Partial() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.walk("/level1/level2/no-such-element");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void walk_NoPath_AtAll() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.walk("/no-such-element1/no-such-element2");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void walk_NoPath_PastTheEdgeOfThePath() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.walk("/level1/level2/level3/level4/level5");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void walk_MultipleElementsMatchingPath() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.walk("/level1/list1/list1-element");

        assertEquals(3, matches.size());

        XmlContext match;
        XMLEvent c, p;

        match = matches.get(0);
        c = match.getCurrent();
        assertEquals("a", c.asCharacters().getData().trim());
        p = match.getPrevious();
        assertEquals("list1-element", p.asStartElement().getName().getLocalPart());

        match = matches.get(1);
        c = match.getCurrent();
        assertEquals("b", c.asCharacters().getData().trim());
        p = match.getPrevious();
        assertEquals("list1-element", p.asStartElement().getName().getLocalPart());

        match = matches.get(2);
        c = match.getCurrent();
        assertEquals("c", c.asCharacters().getData().trim());
        p = match.getPrevious();
        assertEquals("list1-element", p.asStartElement().getName().getLocalPart());
    }

    // get() -----------------------------------------------------------------------------------------------------------

    @Test
    public void get() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);
        String s = ed.get("/level1/level2/level3");
        assertEquals("something", s);
    }

    @Test
    public void get_SkipADeepIncursion() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);
        String s = ed.get("/level1/level2-b/level3-b");
        assertEquals("somethingelse", s);
    }

    @Test
    public void get_NoPath_Partial() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);
        String s = ed.get("/level1/level2/no-such-element");
        assertNull(s);
    }

    @Test
    public void get_MultipleElementsMatchingPath() throws Exception {

        File pom = copyToScratch("xml/walk.xml", "walk.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);
        String s = ed.get("/level1/list1/list1-element");
        assertEquals("a", s);
    }

    // getList() -------------------------------------------------------------------------------------------------------

    @Test
    public void getList() throws Exception {

        File pom = copyToScratch("xml/pom-multi-module.xml", "pom.xml");

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<String> modules = ed.getList("/project/modules/module");

        assertEquals(3, modules.size());
        assertEquals("module1", modules.get(0));
        assertEquals("module2", modules.get(1));
        assertEquals("release", modules.get(2));
    }

    // set() -----------------------------------------------------------------------------------------------------------

    @Test
    public void set_PathDoesNotExistInFile_Root() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a><b>x</b></a>");

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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
//        String content = editor.getContent();
//        assertEquals("<a><b>x</b></a>\n<c>y</c>\n", content);
    }

    @Test
    public void set_PathDoesNotExistInFile_Intermediate() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<a><b>x</b></a>");

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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
//        String content = editor.getContent();
//        assertEquals("<a><b>x</b>\n    <c>y</c>\n</a>\n", content);
    }

    @Test
    public void set_EssentialValuesDoNotDiffer() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");

        String content = "<root><a>  ?    </a></root>\n";

        Files.write(xmlFile, content);

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

        assertFalse(editor.set("/root/a", "?"));
    }

    @Test
    public void set_PreservesSurroundingSpace_OneLine_PaddingDiffers() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");

        String content = "<root><a>  ?    </a></root>\n";

        Files.write(xmlFile, content);

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);
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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);
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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

        // noop
        assertFalse(editor.undo());

        // another noop
        assertFalse(editor.undo());
    }

    @Test
    public void undo_SaveNotDirty() throws Exception {

        File xmlFile = new File(scratchDirectory, "test.xml");
        Files.write(xmlFile, "<root/>");

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(xmlFile);

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

        File origFile = new File(System.getProperty("basedir"), "src/test/resources/data/xml/simple.xml");

        assertTrue(origFile.isFile());

        //
        // copy it in the test area
        //

        File sampleFile = new File(scratchDirectory, "simple-copy.xml");

        assertTrue(Files.cp(origFile, sampleFile));

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(sampleFile);

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

        BasicInLineXmlEditor editor2 = new BasicInLineXmlEditor(sampleFile);

        String s = editor2.get("/example/color");

        assertEquals("blue", s);
    }

    @Test
    public void endToEnd() throws Exception {

        File origFile = new File(System.getProperty("basedir"), "src/test/resources/data/xml/pom-sample.xml");

        assertTrue(origFile.isFile());

        //
        // copy it in the test area
        //

        File sampleFile = new File(scratchDirectory, "pom-sample.xml");

        assertTrue(Files.cp(origFile, sampleFile));

        BasicInLineXmlEditor editor = new BasicInLineXmlEditor(sampleFile);

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

        BasicInLineXmlEditor editor2 = new BasicInLineXmlEditor(sampleFile);

        String s = editor2.get("/project/version");

        assertEquals("1.0.0-SNAPSHOT-2", s);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected BasicInLineXmlEditor getInLineXmlEditorToTest(File xmlFile) throws Exception {

        return new BasicInLineXmlEditor(xmlFile);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    /**
     * Copies to scratch the specified resource file. The path of the resource file must be relative to
     * ./src/test/resources/data.
     *
     * The method fails if the file is not found or the copy fails.
     *
     * @return the copied file in the scratch area.
     */
    private File copyToScratch(String pathRelativeToDataDir, String pathInScratch) {

        File pom = new File(scratchDirectory, pathInScratch);
        assertTrue(Files.cp(new File(baseDirectory, "src/test/resources/data/" + pathRelativeToDataDir), pom));
        return pom;
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
