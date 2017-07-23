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

package io.novaordis.utilities.logging.log4j;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 4/28/17
 */
public enum Log4jLevel {

    // Constants -------------------------------------------------------------------------------------------------------

    OFF,
    FATAL,
    ERROR,
    WARN,
    INFO,
    DEBUG,
    TRACE,
    ALL;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * @return null on invalid value, that cannot be converted
     */
    public static Log4jLevel fromLiteral(String s) {

        for(Log4jLevel l: values()) {

            if (l.toLiteral().equals(s)) {

                return l;
            }
        }

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String toLiteral() {

        return toString();
    }

}
