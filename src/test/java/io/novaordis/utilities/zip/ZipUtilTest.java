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

package io.novaordis.utilities.zip;

import io.novaordis.utilities.Files;
import io.novaordis.utilities.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/28/16
 */
public class ZipUtilTest {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ZipUtilTest.class);

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    private File scratchDirectory;

    @Before
    public void before() throws Exception {

        String projectBaseDirName = System.getProperty("basedir");
        scratchDirectory = new File(projectBaseDirName, "target/test-scratch");
        assertTrue(scratchDirectory.isDirectory());
    }

    @After
    public void after() throws Exception {

        //
        // scratch directory cleanup
        //

        assertTrue(io.novaordis.utilities.Files.rmdir(scratchDirectory, false));
    }

    // Test ------------------------------------------------------------------------------------------------------------

    // list ------------------------------------------------------------------------------------------------------------

    @Test
    public void list_Null() throws Exception {

        try {

            ZipUtil.list(null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }
    }

    @Test
    public void list_NotAFile() throws Exception {

        try {

            ZipUtil.list(new File("/I/am/sure/no/such/file/exists"));
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }
    }

    @Test
    public void list_NotAZipFile() throws Exception {

        File fakeZip = new File(scratchDirectory, "fake.zip");
        assertTrue(Files.write(fakeZip, "NOT A ZIP FILE"));

        try {

            ZipUtil.list(fakeZip);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals(msg, "invalid or corrupted zip file " + fakeZip.getAbsolutePath());
        }
    }

    @Test
    public void list() throws Exception {

        File zipFile = Util.cp("zip/zip-example.zip", scratchDirectory);

        String list = ZipUtil.list(zipFile);

        log.info(list);

        String expected =
                "top-level-dir/\n" +
                "top-level-dir/a/\n" +
                "top-level-dir/a/a-file.txt\n" +
                "top-level-dir/a/x/\n" +
                "top-level-dir/a/x/x-file.txt\n" +
                "top-level-dir/a/y/\n" +
                "top-level-dir/a/z/\n" +
                "top-level-dir/b/\n" +
                "top-level-dir/c/\n" +
                "top-level-dir/top-level-file.txt\n";

        assertEquals(expected, list);
    }

    // getTopLevelDirectoryName ----------------------------------------------------------------------------------------

    @Test
    public void getTopLevelDirectoryName_Null() throws Exception {

        try {

            ZipUtil.getTopLevelDirectoryName(null);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }
    }

    @Test
    public void getTopLevelDirectoryName_NotAFile() throws Exception {

        try {

            ZipUtil.getTopLevelDirectoryName(new File("/I/am/sure/no/such/file/exists"));
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            log.info(e.getMessage());
        }
    }

    @Test
    public void getTopLevelDirectoryName_NotAZipFile() throws Exception {

        File fakeZip = new File(scratchDirectory, "fake.zip");
        assertTrue(Files.write(fakeZip, "NOT A ZIP FILE"));

        try {

            ZipUtil.getTopLevelDirectoryName(fakeZip);
            fail("should throw exception");
        }
        catch(IllegalArgumentException e) {

            String msg = e.getMessage();
            log.info(msg);
            assertEquals(msg, "invalid or corrupted zip file " + fakeZip.getAbsolutePath());
        }
    }

    @Test
    public void getTopLevelDirectoryName_RelativeDirectories_MultipleTopLevelDirectories() throws Exception {

        File zipFile = Util.cp("zip/two-top-level-directories.zip", scratchDirectory);

        String name = ZipUtil.getTopLevelDirectoryName(zipFile);

        assertNull(name);
    }

    @Test
    public void getTopLevelDirectoryName_NoTopLevelDirectories() throws Exception {

        File zipFile = Util.cp("zip/no-top-level-directory.zip", scratchDirectory);

        String name = ZipUtil.getTopLevelDirectoryName(zipFile);

        assertNull(name);
    }

    @Test
    public void getTopLevelDirectoryName_RelativeDirectories() throws Exception {

        File zipFile = Util.cp("zip/zip-example.zip", scratchDirectory);

        String name = ZipUtil.getTopLevelDirectoryName(zipFile);

        assertEquals("top-level-dir", name);
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
