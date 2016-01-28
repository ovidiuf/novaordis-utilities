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

package io.novaordis.utilities;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/27/16
 */
public class VersionUtilitiesTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(VersionUtilities.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    @Test
    public void getVersion_noVERSIONFile() throws Exception {

        assertNull(VersionUtilities.getVersion());
    }

    @Test
    public void getReleaseDate_noVERSIONFile() throws Exception {

        assertNull(VersionUtilities.getReleaseDate());
    }

    @Test
    public void getVersionAndReleaseDate() throws Exception {

        //
        // place a DEFAULT_VERSION_FILE_NAME in target/test-classes, which is in classpath
        //

        String basedir = System.getProperty("basedir");
        assertNotNull(basedir);
        File d = new File(basedir, "target/test-classes");
        assertTrue(d.isDirectory());
        File versionFile = new File(d, VersionUtilities.DEFAULT_VERSION_FILE_NAME);
        assertTrue(Files.write(versionFile, "version=blah\nrelease_date=blah2"));

        String version = VersionUtilities.getVersion();
        assertEquals("blah", version);
        log.info(version);

        String releaseDate = VersionUtilities.getReleaseDate();
        assertEquals("blah2", releaseDate);
        log.info(releaseDate);

        assertTrue(versionFile.delete());
        assertFalse(versionFile.exists());
    }

    @Test
    public void getVersionButNotReleaseDate() throws Exception {

        //
        // place a DEFAULT_VERSION_FILE_NAME in target/test-classes, which is in classpath
        //

        String basedir = System.getProperty("basedir");
        assertNotNull(basedir);
        File d = new File(basedir, "target/test-classes");
        assertTrue(d.isDirectory());
        File versionFile = new File(d, VersionUtilities.DEFAULT_VERSION_FILE_NAME);
        assertTrue(Files.write(versionFile, "version=blah"));

        String version = VersionUtilities.getVersion();
        assertEquals("blah", version);
        log.info(version);

        assertNull(VersionUtilities.getReleaseDate());

        assertTrue(versionFile.delete());
        assertFalse(versionFile.exists());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
