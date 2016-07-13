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

    /**
     * Formats a timestamp. It must get the timezone offset that was used in the original string representation, or null
     * if no timezone offset was used, and uses this information to format the timestamp using the target format,
     * making sure the hour part is identical, even if the timezone information is missing from the target format.
     *
     * This behavior is useful when parsing and translating logs, we want the source and target log hour part of the
     * timestamp to be identical, irrespective of the timezone in which the translation is done.
     *
     * @param timestamp may be null. If not null, it contains a GMT timestamp and possibly the time zone offset,
     *                  in millisecond, if explicitly specified by the timestamp source representation, or null if the
     *                  source representation does not explicitly specify a time zone.
     *
     * @see Timestamp
     *
     * @param targetFormat the target format. Cannot be null.
     *
     * @param noTimestampString the string representation to use when the timestamp is null.
     *
     * @throws IllegalArgumentException if the sourceTimeZoneOffsetHours is not a valid value (null or -12 ... 14).
     */
    public static String format(Timestamp timestamp, DateFormat targetFormat, String noTimestampString) {

        throw new RuntimeException("Timestamps.format() NOT YET IMPLEMENTED");

//        if (targetFormat == null) {
//            throw new IllegalArgumentException("null target format");
//        }
//
//        if (timestamp == null) {
//            return noTimestampString;
//        }
//
//        Integer sourceTimeZoneOffset = timestamp.getTimezoneOffsetMs();
//
//        if (sourceTimeZoneOffset == null || doesIncludeTimezoneSpecification(targetFormat)) {
//
//            //
//            // the source time stamp did not contain an explicitly specified timezone offset, so both source and
//            // target calculations are done in the local timezone, the format will be the same, simply apply the target
//            // pattern
//            //
//
//            return targetFormat.format(timestamp.getTimestampGMT());
//        }
//
//        //
//        // at this point we know the source specifies a timezone offset; if the target format does not specify a timezone
//        // offset, we want to compensate for the source offset so the timestamp looks the same.
//        //
//
//        TimeZone defaultTimeZone = TimeZone.getDefault();
//
//        int defaultTimezoneOffset = defaultTimeZone.getRawOffset() + defaultTimeZone.getDSTSavings();
//
//        long offsetTimestamp = timestamp.getTimestampGMT() - defaultTimezoneOffset + sourceTimeZoneOffset;
//
//        return targetFormat.format(offsetTimestamp);
    }

    /**
     * Accounts for the timezone offset and the daylight saving time.
     */
    public static int getDefaultTimezoneHours() {

        return getDefaultTimezoneMs() / MILLISECONDS_IN_AN_HOUR;
    }

    /**
     * Accounts for the timezone offset and the daylight saving time.
     */
    public static int getDefaultTimezoneMs() {

        TimeZone defaultTimezone = TimeZone.getDefault();
        return defaultTimezone.getRawOffset() + defaultTimezone.getDSTSavings();
    }

    public static String timezoneOffsetHoursToString(int offsetHours) {

        if (!isValidTimeZoneOffsetHours(offsetHours)) {
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

    public static boolean isValidTimeZoneOffsetHours(int hours) {

        return LOWEST_VALID_TIMEZONE_OFFSET_HOURS <= hours && hours <= HIGHEST_VALID_TIMEZONE_OFFSET_HOURS;
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
