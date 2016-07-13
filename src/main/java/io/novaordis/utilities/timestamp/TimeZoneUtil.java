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

package io.novaordis.utilities.timestamp;

import java.util.TimeZone;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/13/16
 */
public class TimeZoneUtil {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Scan the string for the first RFC822 representation of a timezone offset and return the corresponding TimeZone
     * instance if it finds it. Returns null if it cannot detect a timezone offset in RFC822 format, unless we're
     * clearly looking at an improperly formatted timezone offset, in which case it throws Exception.
     *
     * @return the TimeZone instance corresponding to the RFC822  timezone offset identified in the string, or null if
     * no timezone offset is identified.
     *
     * @throws IllegalArgumentException in case of improperly formatted timestamp string
     *
     * @see TimeZoneUtil#toRFC822String(TimeZone)
     */
    public static TimeZone fromRFC822String(String s) {

        int negativeIndex = s.indexOf("-");
        int positiveIndex = s.indexOf("+");

        if (negativeIndex == -1 && positiveIndex == -1) {
            return null;
        }

        //
        // pick the first occurrence
        //
        int separatorIndex =
                negativeIndex < 0 ? positiveIndex :
                        (positiveIndex < 0 ? negativeIndex : Math.max(positiveIndex, negativeIndex));

        //
        // we expect four digits
        //

        int crt = separatorIndex + 1;
        String timeZoneOffset = "";

        while(crt - separatorIndex - 1 < 4) {

            if (crt >= s.length()) {

                throw new IllegalArgumentException(
                        "incomplete RFC822 timezone offset fragment \"" + s.substring(separatorIndex) + "\"");
            }

            char c = s.charAt(crt);

            if (c < '0' || c > '9') {

                throw new IllegalArgumentException("invalid RFC822 timezone \"" + s.substring(separatorIndex) + "\"");
            }

            timeZoneOffset += c;
            crt ++;
        }

        timeZoneOffset = "GMT" + s.charAt(separatorIndex) + timeZoneOffset;

        return TimeZone.getTimeZone(timeZoneOffset);

    }

    /**
     * The inverse operation to fromRFC822String()
     *
     * @see TimeZoneUtil#fromRFC822String(String)
     */
    public static String toRFC822String(TimeZone timeZone) {

        //
        // figure out the offset relative to GMT
        //

        TimeZone reference = TimeZone.getTimeZone("GMT");

        long now = System.currentTimeMillis();

        int offsetMs = reference.getOffset(now) - timeZone.getOffset(now);

        int offsetHours = offsetMs / (3600 * 1000);

        if (offsetHours <= -10) {
            return "" + offsetHours + "00";
        }
        else if (offsetHours < 0) {
            return "-0" + (-offsetHours) + "00";
        }
        else if (offsetHours < 9) {
            return "+0" + offsetHours + "00";
        }
        else {
            return "+" + offsetHours + "00";
        }
    }

    /**
     * @param hours the number of hours can be positive or negative.
     * @return a new TimeZone that is shifted relative to the reference timezone with the specified number of hours.
     *
     * @throws IllegalArgumentException if we cannot identify a TimeZone shifted with the specfied number of hours.
     */
    public static TimeZone shift(TimeZone reference, int hours) {

        if (hours == 0) {
            return reference;
        }

        int offset = reference.getRawOffset();
        offset += hours * 3600 * 1000;
        String[] candidates = TimeZone.getAvailableIDs(offset);

        if (candidates.length == 0) {
            throw new IllegalArgumentException(
                    "no timezone " + hours + " hours apart from " + reference.getDisplayName());
        }

        //
        // pick the first that includes GMT in its name, if not, pick the first one
        //

        String candidate = null;

        for(String s: candidates) {
            if (s.toUpperCase().contains("GMT")) {
                candidate = s;
                break;
            }
        }

        if (candidate == null) {
            candidate = candidates[0];
        }

        return TimeZone.getTimeZone(candidate);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    private TimeZoneUtil() {
    }

    // Inner classes ---------------------------------------------------------------------------------------------------

}
