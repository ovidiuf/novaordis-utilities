/*
 * Copyright (c) 2017 Nova Ordis LLC
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

package io.novaordis.utilities.os;

/**
 * An identifier for various operating systems.
 *
 * Has embedded logic to detect the operating system the current JVM runs on. The information can be obtained as
 * OSType.current
 *
 * @see OSType#current
 * 
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 6/6/17
 */
public enum OSType {

    // Constants -------------------------------------------------------------------------------------------------------

    LINUX,
    MAC,
    WINDOWS,
    TEST,
    ;

    // Static ----------------------------------------------------------------------------------------------------------

    public static final String OS_NAME_SYSTEM_PROPERTY = "os.name";

    public static OSType current;

    protected static String value;

    static {

        reset();
    }

    public static OSType getCurrent() {

        if (current == null) {

            throw new IllegalStateException("unknown '" + OS_NAME_SYSTEM_PROPERTY + "' value: '" + value + "'");
        }

        return current;
    }

    public static void reset() {

        value = System.getProperty(OS_NAME_SYSTEM_PROPERTY);

        if ("Linux".equals(value)) {

            current = LINUX;
        }
        else if ("Mac OS X".equals(value)) {

            current = MAC;
        }
        else if (value.toLowerCase().contains("windows")) {

            current = WINDOWS;
        }
    }

    // Public ----------------------------------------------------------------------------------------------------------

}
