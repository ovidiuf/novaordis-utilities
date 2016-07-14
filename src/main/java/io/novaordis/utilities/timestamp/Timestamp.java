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
 * An interface that binds together a UTC timestamp (a universal time timestamp, expressed in milliseconds from
 * 01/01/1972 00:00:00 GMT and not accounting for daylight saving), and optionally a time offset, as specified in the
 * original timestamp string representation (or, if the explicit time offset is absent, the effective time offset of
 * the default time zone at the time of the parsing.
 *
 * @see Timestamp#getTime()
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/11/16
 */
public interface Timestamp {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Review usage, figure out whether I can refactor.
     *
     * Formats a timestamp using the target format, making sure the hour part is identical, even if the timezone
     * information is missing from the target format.
     *
     * This behavior is useful when parsing and translating logs, we want the source and target log hour part of the
     * timestamp to be identical, irrespective of the timezone in which the translation is done.
     *
     * @param t may be null. If not null, it contains a UTC timestamp and a time offset.
     *
     * @see Timestamp
     *
     * @param targetFormat the target format. Cannot be null.
     * @param nullTimestampString the string representation to use when the timestamp is null.
     *
     */
    @Deprecated
    public static String format(Timestamp t, DateFormat targetFormat, String nullTimestampString) {

        if (targetFormat == null) {
            throw new IllegalArgumentException("null target format");
        }

        if (t == null) {
            return nullTimestampString;
        }

        long offsetTimestamp =
                t.getTime() -
                        TimeZone.getDefault().getOffset(System.currentTimeMillis()) + t.getTimeOffset().getOffset();

        return targetFormat.format(offsetTimestamp);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return a UTC timestamp (a Universal Time timestamp, expressed in milliseconds from 01/01/1972 00:00:00 GMT and
     * not accounting for daylight saving). Similar in semantics to Date.getTime()
     */
    long getTime();

    /**
     * @return the time offset explicitly specified by the date string, or, if no explicit time offset was present in
     * the timestamp string, the effective time offset of the default time zone at the time of the parsing.
     *
     * It never returns null.
     *
     * This information is needed to correctly process timestamps when we mix timestamps parsed from string
     * representations that use explicit time offset and those that do not (and thus imply the time offset of the
     * time zone the parsing occurred in).
     */
    TimeOffset getTimeOffset();

    /**
     * @param formatElement a valid SimpleDateFormat format element ("M") or a combination of elements ("MM/dd/yy").
     *
     * @return the specified fragment corresponding to the timestamp in its original string representation - the
     * way it was read from the logs. For example, if the timestamp was represented in a log as
     * "12/31/16 10:00:00 -0800", then getTimestampElement("d") returns "31", getTimestampElement("M") returns "12",
     * etc.
     */
    String elementToString(String formatElement);

    /**
     * Review usage, figure out whether I can refactor.
     */
    @Deprecated
    long adjustForTimeOffset(int timeOffset);

}

