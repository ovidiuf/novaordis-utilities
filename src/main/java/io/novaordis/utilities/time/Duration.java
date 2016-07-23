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

package io.novaordis.utilities.time;

/**
 * Models a time interval duration (e.g. 10 minutes), with a millisecond resolution.
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/22/16
 */
public class Duration {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    public static long stringToMs(String s) throws DurationFormatException {

        long multiplicationFactor;
        String fragment = s.substring(0, s.length() - 1);

        if (s.endsWith("s")) {

            multiplicationFactor = 1000L;
        }
        else if (s.endsWith("m")) {

            multiplicationFactor = 1000L * 60;
        }
        else if (s.endsWith("h")) {

            multiplicationFactor = 1000L * 60 * 60;
        }
        else {
            throw new DurationFormatException("invalid duration string \"" + s + "\"");
        }

        long d;

        try {

            d = Long.parseLong(fragment);
        }
        catch(NumberFormatException e) {

            throw new DurationFormatException(
                    "invalid number portion of the duration string: \"" + fragment + "\"" , e);
        }

        return d * multiplicationFactor;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private long durationMs;

    // Constructors ----------------------------------------------------------------------------------------------------

    public Duration(long durationMs) {

        this.durationMs = durationMs;
    }

    public Duration(String s) throws DurationFormatException {

        this.durationMs = stringToMs(s);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    public long getMilliseconds() {

        return durationMs;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
