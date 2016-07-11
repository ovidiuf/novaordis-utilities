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

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Static utilities to hadle timestamps.
 *
 * @see Timestamp
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/10/16
 */
public class Timestamps {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final int LOWEST_VALID_TIMEZONE_OFFSET_HOURS = -12;
    public static final int HIGHEST_VALID_TIMEZONE_OFFSET_HOURS = 14;

    public static final int MILLISECONDS_IN_AN_HOUR = 1000 * 3600;

    // Static ----------------------------------------------------------------------------------------------------------

    public static boolean isValidTimeZoneOffset(int offset) {

        return LOWEST_VALID_TIMEZONE_OFFSET_HOURS <= offset && offset <= HIGHEST_VALID_TIMEZONE_OFFSET_HOURS;
    }

    /**
     * Designed to be lenient, and return a null if no timezone offset is found, unless we're clearly dealing with
     * an improperly formatted timezone offset, in which case it throws Exception.
     *
     * @return the explicitly specified timezone offset (in hours) or null if not specified.
     *
     * @throws IllegalArgumentException in case of improperly formatted timestamp string
     */
    public static Integer getTimeZoneOffsetHours(String timestamp) {

        int negativeIndex = timestamp.indexOf(" -");
        int positiveIndex = timestamp.indexOf(" +");

        if (negativeIndex != -1 && positiveIndex != -1) {
            throw new IllegalArgumentException(
                    "both timezone prefixes ' +' and ' -' detected in timestamp \"" + timestamp + "\"");
        }

        if (negativeIndex == -1 && positiveIndex == -1) {
            return null;
        }

        boolean positive = true;
        int digitsStart;

        if (negativeIndex != -1) {

            positive = false;
            digitsStart = negativeIndex + 2;
        }
        else {

            digitsStart = positiveIndex + 2;
        }

        //
        // we expect four digits
        //

        int crt = digitsStart;
        int offset = 0;
        int multiplier = 10;

        while(crt - digitsStart < 4) {

            if (crt >= timestamp.length()) {

                throw new IllegalArgumentException(
                        "incomplete timezone fragment \"" + timestamp.substring(digitsStart - 1) + "\"");
            }

            char c = timestamp.charAt(crt);

            if (c < '0' || c > '9') {

                throw new IllegalArgumentException(
                        "invalid timezone fragment \"" + timestamp.substring(digitsStart - 1) + "\"");
            }

            int digitIntValue = (int)c - 48;

            if (multiplier == 0 && digitIntValue != 0) {

                throw new IllegalArgumentException("fractional timezones not supported (yet)");
            }

            offset += multiplier * digitIntValue;

            multiplier /= 10;
            crt ++;
        }

        offset = positive ? offset : -offset;

        if (!isValidTimeZoneOffset(offset)) {

            throw new IllegalArgumentException("invalid timezone offset value " + offset);
        }

        return offset;
    }

    public static String timezoneOffsetHoursToString(int offsetHours) {

        if (!isValidTimeZoneOffset(offsetHours)) {
            throw new IllegalArgumentException("invalid timezone offset value " + offsetHours);
        }

        if (offsetHours < -9) {
            return "" + offsetHours + "00";
        }
        else if (offsetHours < 0) {
            return "-0" + (-offsetHours) + "00";
        }
        else if (offsetHours < 10) {
            return "+0" + offsetHours + "00";
        }
        else {
            return "+" + offsetHours + "00";
        }
    }

    public static String timezoneOffsetMsToString(int offsetMs) {

        return timezoneOffsetHoursToString(offsetMs/MILLISECONDS_IN_AN_HOUR);
    }

    /**
     * Returns true if the specified date format includes a timezone specification.
     */
    public static boolean doesIncludeTimezoneSpecification(DateFormat dateFormat) {

        if (dateFormat == null) {
            return false;
        }

        String reference = dateFormat.format(new Date());
        return reference.contains(" +") || reference.contains(" -");
    }

    /**
     * Formats a timestamp. It must get the timezone offset that was used in the original string representation, or null
     * if no timezone offset was used, and uses this information to format the timestamp using the target format,
     * making sure the hour part is identical, even if the timezone information is missing from the target format.
     *
     * This behavior is useful when parsing and translating logs, we want the source and target log hour part of the
     * timestamp to be identical, irrespective of the timezone in which the translation is done.
     *
     * @param timestamp may be null. If not null, a timestamp, in milliseconds, relative to GMT.
     *
     * @param sourceTimeZoneOffsetHours - the time zone offset, in hours, if explicitly specified by the timestamp
     *                                  source representation, or null if the source representation does not
     *                                  explicitly specify a time zone. Valid values are integers between -12 and 14
     *                                  (these values have been obtained by querying all time zones known by Java,
     *                                  with TimeZone.getAvailableIDs()).
     *                                  The method will throw an IllegalArgumentException if it gets an invalid value.
     *
     * @see Timestamps#getTimeZoneOffsetHours(String)
     *
     * @param targetFormat the target format. Cannot be null.
     *
     * @param noTimestampString the string representation to use when the timestamp is null.
     *
     * @throws IllegalArgumentException if the sourceTimeZoneOffsetHours is not a valid value (null or -12 ... 14).
     */
    public static String format(Long timestamp, Integer sourceTimeZoneOffsetHours,
                         DateFormat targetFormat, String noTimestampString) {

        if (targetFormat == null) {
            throw new IllegalArgumentException("null target format");
        }

        if (timestamp == null) {
            return noTimestampString;
        }

        if (sourceTimeZoneOffsetHours != null && !isValidTimeZoneOffset(sourceTimeZoneOffsetHours)) {
            throw new IllegalArgumentException("invalid source time zone offset value " + sourceTimeZoneOffsetHours);
        }

        if (sourceTimeZoneOffsetHours == null || doesIncludeTimezoneSpecification(targetFormat)) {

            //
            // the source time stamp did not contain an explicitly specified timezone offset, so both source and
            // target calculations are done in the local timezone, the format will be the same, simply apply the target
            // pattern
            //

            return targetFormat.format(timestamp);
        }

        //
        // at this point we know the source specifies a timezone offset; if the target format does not specify a timezone
        // offset, we want to compensate for the source offset so the timestamp looks the same.
        //

        TimeZone defaultTimeZone = TimeZone.getDefault();

        int defaultTimezoneOffsetHours =
                (defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings()) / MILLISECONDS_IN_AN_HOUR;

        long offsetTimestamp =
                timestamp - (defaultTimezoneOffsetHours - sourceTimeZoneOffsetHours) * MILLISECONDS_IN_AN_HOUR;

        return targetFormat.format(offsetTimestamp);
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    private Timestamps() {
    }

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
