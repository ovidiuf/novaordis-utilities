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

package io.novaordis.utilities.version;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * See {@linktourl https://kb.novaordis.com/index.php/Nova_Ordis_Utilities_Version_Metadata_Handling}
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 1/27/16
 */
public class VersionUtilities {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = LoggerFactory.getLogger(VersionUtilities.class);

    public static final String DEFAULT_VERSION_FILE_NAME = "VERSION";

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private VersionUtilities() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * Extract version metadata from the default resource file ("VERSION").
     *
     * @see VersionUtilities#getVersion(String)
     */
    public static String getVersion() {

        return getVersion(DEFAULT_VERSION_FILE_NAME);
    }

    /**
     * Extract version metadata from a custom resource file. A file with the specified name must exist somewhere on
     * the classpath.
     *
     * @return null if the metadata file is not found or there was a read failure (also a log warning is generated).
     */
    public static String getVersion(String resourceFileName) {

        return getReleaseMetadata(resourceFileName, "version");
    }

    /**
     * Extract release data from the default resource file ("VERSION").
     *
     * @see VersionUtilities#getReleaseDate(String)
     */
    public static String getReleaseDate() {

        return getReleaseDate(DEFAULT_VERSION_FILE_NAME);
    }

    /**
     * Extract release data from a custom resource file. A file with the specified name must exist somewhere on
     * the classpath.
     *
     * @return null if the metadata file is not found or there was a read failure (also a log warning is generated).
     */
    public static String getReleaseDate(String resourceFileName) {

        return getReleaseMetadata(resourceFileName, "release_date");
    }

    /**
     * Extract release metadata from the default resource file ("VERSION").
     *
     * @see VersionUtilities#getReleaseMetadata(String, String)
     */
    public static String getReleaseMetadata(String propertyName) {

        return getReleaseMetadata(DEFAULT_VERSION_FILE_NAME, propertyName);
    }

    /**
     * Extract release metadata from a custom resource file. A file with the specified name must exist somewhere on
     * the classpath.
     *
     * @return null if the metadata file was not found, the property was not found in the file or there was a read
     * failure. A warning is logged in all of those cases.
     */
    public static String getReleaseMetadata(String releaseMetadataFileName, String propertyName) {

        ClassLoader cl = VersionUtilities.class.getClassLoader();

        InputStream is = cl.getResourceAsStream(releaseMetadataFileName);

        if (is == null) {

            log.warn("release metadata file \"" + releaseMetadataFileName + "\" not found on the classpath");
            return null;
        }

        Properties properties = new Properties();

        try {

            properties.load(is);
        }
        catch(IOException e) {

            log.warn("failed to read the release metadata file \"" + releaseMetadataFileName + "\"", e);
            return null;
        }

        log.debug("properties from '" + releaseMetadataFileName + "' loaded");

        String value = properties.getProperty(propertyName);

        if (value == null) {

            log.warn("no '" + propertyName + "' property found in \"" + releaseMetadataFileName + "\"");
            return null;
        }

        return value;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
