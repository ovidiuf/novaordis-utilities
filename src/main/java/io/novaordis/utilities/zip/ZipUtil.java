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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 11/28/16
 */
public class ZipUtil {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Handle the given file as a zip file and list the content of the zip file the same way unzip -l would.
     *
     * @param file must point to an underlying ZIP file, otherwise the method will throw an IllegalArgumentException.
     */
    public static String list(File file) throws IllegalArgumentException {

        final StringBuilder s = new StringBuilder();

        walk(file, (ZipEntry e) -> s.append(e.getName()).append("\n"));

        return s.toString();
    }

    /**
     * Handle the given file as a zip file that contains only one top level directory and returns the name of the
     * single top level directory. If the zip file contains no top level directory, or it contains more than one top
     * level directory, the method will return null.
     *
     * If the underlying file is not a zip file, the method will throw IllegalArgumentException,
     *
     * @param file must point to an underlying ZIP file, otherwise the method will throw an IllegalArgumentException.
     */
    public static String getTopLevelDirectoryName(File file) {

        final String[] topLevelDir = new String[1];

        try {

            walk(file, (ZipEntry e) -> {

                String entryName = e.getName();

                if (!entryName.contains("/")) {

                    // file in root of the zip archive
                    return;
                }

                String candidate = entryName.replaceAll("/.*", "");

                if (topLevelDir[0] != null && !topLevelDir[0].equals(candidate)) {

                    //
                    // more than one top level directory found
                    //
                    throw new IllegalStateException("multiple top directories");
                }

                topLevelDir[0] = candidate;
            });
        }
        catch(IllegalStateException e) {

            //
            // multiple top directories
            //
            return null;
        }

        return topLevelDir[0];
    }

    // Package protected static ----------------------------------------------------------------------------------------

    /**
     * Handle the given file as a zip file and throw an IllegalArgumentException if it isn't.
     *
     * Applies a generic closure to each ZipEntry.
     */

    static void walk(File file, ZipEntryClosure c) throws IllegalArgumentException {

        if (file == null) {

            throw new IllegalArgumentException("null file");
        }

        if (!file.isFile() || !file.canRead()) {

            throw new IllegalArgumentException(file + " does not exist or cannot be read");

        }

        ZipFile zipFile;

        try {

            zipFile = new ZipFile(file);
        }
        catch(Exception e) {

            log.debug("failed to handle zip file " + file, e);
            throw new IllegalArgumentException("invalid or corrupted zip file " + file.getAbsolutePath());
        }

        Enumeration<? extends ZipEntry> en = zipFile.entries();

        while(en.hasMoreElements()) {

            ZipEntry e = en.nextElement();
            c.apply(e);
        }
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
