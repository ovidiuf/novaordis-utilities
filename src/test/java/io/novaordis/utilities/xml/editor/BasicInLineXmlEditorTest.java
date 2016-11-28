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

import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    // collectMatches --------------------------------------------------------------------------------------------------

    @Test
    public void collectMatches() throws Exception {

        File pom = Util.cp("xml/walk.xml", scratchDirectory);

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.collectMatches("/level1/level2/level3");

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

        log.debug(".");
    }

    @Test
    public void collectMatches_SkipADeepIncursion() throws Exception {

        File pom = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.collectMatches("/level1/level2-b/level3-b");

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
    public void collectMatches_NoPath_Partial() throws Exception {

        File pom = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.collectMatches("/level1/level2/no-such-element");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void collectMatches_NoPath_AtAll() throws Exception {

        File pom = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.collectMatches("/no-such-element1/no-such-element2");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void collectMatches_NoPath_PastTheEdgeOfThePath() throws Exception {

        File pom = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.collectMatches("/level1/level2/level3/level4/level5");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void collectMatches_MultipleElementsMatchingPath() throws Exception {

        File pom = Util.cp(baseDirectory, "src/test/resources/data/xml/walk.xml", scratchDirectory);

        BasicInLineXmlEditor ed = new BasicInLineXmlEditor(pom);

        List<XmlContext> matches = ed.collectMatches("/level1/list1/list1-element");

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

    // Package protected -----------------------------------------------------------------------------------------------

    @Override
    protected BasicInLineXmlEditor getInLineXmlEditorToTest(File xmlFile) throws Exception {

        return new BasicInLineXmlEditor(xmlFile);
    }

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
