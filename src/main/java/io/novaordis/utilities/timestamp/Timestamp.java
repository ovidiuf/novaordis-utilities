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

/**
 * An interface that binds together a timestamp expressed in milliseconds from the GMT epoch, and optionally a
 * timezone offset, as specified in the original string representation of the timestamp.
 *
 * @see Timestamp#getTimestampGMT()
 * @see Timestamp#getTimezoneOffsetMs()
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

    /**
     * @return the timezone offset, in milliseconds, as specified by the source of the event (logs, for example). If
     * the original event timestamp was "12/31/16 10:00:00 -0800" in the log, then the timezone offset is
     * -8 * 3600 * 1000 ms. Null if no timezone offset specified by the source of the event. We need this information
     * to produce timestamps similar to the original ones, when the processing is done in an arbitrary timezone.
     *
     * Valid values are integers between -12 * 3600 * 1000 and 14 * 3600 * 1000 (these values have been obtained by
     * querying all time zones known by Java, with TimeZone.getAvailableIDs()).
     */
    Integer getTimezoneOffsetMs();

    /**
     * @return the "day in month" information from the original string representation of the timestamp. If the
     * timestamp was represented in a log as "12/31/16 10:00:00 -0800", then getDay() returns 31.
     */
    int getDay();

    /**
     * @return the "month in year" information from the original string representation of the timestamp. If the
     * timestamp was represented in a log as "12/31/16 10:00:00 -0800", then getMonth() returns 12.
     */
    int getMonth();

    /**
     * @return the year information from the original string representation of the timestamp. If the timestamp was
     * represented in a log as "12/31/16 10:00:00 -0800", then getYear() returns 16.
     */
    int getYear();

}

