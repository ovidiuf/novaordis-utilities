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

import org.apache.log4j.Level;

/**
 * Comparable implementation: it is based on the idea that a verbose logging level is "greater" than a less verbose
 * logging level. For example TRACE.compareTo(INFO) > 0, which is logically equivalent with TRACE > INFO.
 *
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

            if (l.name().equals(s)) {

                return l;
            }
        }

        return null;
    }

    /**
     * @return null if no Log4jLevel match.
     */
    public static Log4jLevel find(String s, int from) {

        s = s.substring(from);

        for(Log4jLevel l: values()) {

            if (s.startsWith(l.name())) {

                return l;
            }
        }

        return null;
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public String toLiteral() {

        return toString();
    }

    public Level getLog4jNativeLevel() {

        if (this.equals(OFF)) {

            return Level.OFF;
        }
        else if (this.equals(FATAL)) {

            return Level.FATAL;
        }
        else if (this.equals(ERROR)) {

            return Level.ERROR;
        }
        else if (this.equals(WARN)) {

            return Level.WARN;
        }
        else if (this.equals(INFO)) {

            return Level.INFO;
        }
        else if (this.equals(DEBUG)) {

            return Level.DEBUG;
        }
        else if (this.equals(TRACE)) {

            return Level.TRACE;
        }
        else if (this.equals(ALL)) {

            return Level.ALL;
        }
        else {

            throw new IllegalStateException("none of the known log4j levels");
        }
    }

}
