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
 * An interface that binds together a timestamp expressed in milliseconds from 01/01/1972 00:00:00 GNT, and optionally
 * a timezone offset, as specified in the original string representation of the timestamp, or the default timezone
 * at the time of the parsing, if no timezone offset is specified in the original string representation.
 *
 * @see Timestamp#getTimestampGMT()
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/11/16
 */
public interface Timestamp {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the timestamp in milliseconds from the GMT epoch, not accounting for timezone and daylight saving
     * offsets. It is illegal to return a negative value.
     */
    long getTimestampGMT();

    long adjustForTimeZone(TimeZone timeZone);

    /**
     * @return the TimeZone instance corresponding to the timezone offset specification from the original timestamp
     * representation, or the default JVM timezone at the time of the parsing, if no timezone specification was found
     * in the timestamp representation.
     *
     * Never returns null.
     */
    TimeZone getTimeZone();

    /**
     * @param formatElement a valid SimpleDateFormat format element.
     *
     * @return the specified fragment corresponding to the timestamp in its original timezone (the one returned
     * by getTimezone()). For example, if the timestamp was represented in a log as "12/31/16 10:00:00 -0800", then
     * getTimestampElement("d") returns "31", getTimestampElement("M") returns "12", etc.
     */
    String getTimestampElement(String formatElement);

}

