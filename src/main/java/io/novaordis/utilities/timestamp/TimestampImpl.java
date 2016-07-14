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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/11/16
 */
public class TimestampImpl implements Timestamp {

    // Constants -------------------------------------------------------------------------------------------------------

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private long time;
    private TimeOffset timeOffset;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * Use this constructor for synthetic events. It will default to the current time offset
     * TimeZone.getDefault().getOffset(System.currentTimeMillis())
     */
    public TimestampImpl(long timestampUTC) {

        this(timestampUTC, new TimeOffset(TimeZone.getDefault().getOffset(System.currentTimeMillis())));
    }

    /**
     * Use this constructor for synthetic events.
     *
     * @param timeOffset cannot be null
     *
     * @exception IllegalArgumentException on null time offset.
     */
    public TimestampImpl(long timestampUTC, TimeOffset timeOffset) {

        if (timeOffset == null) {
            throw new IllegalArgumentException("null time offset");
        }

        this.time = timestampUTC;
        this.timeOffset = timeOffset;
    }

    /**
     * @throws ParseException if the string cannot be parsed into a date using the given format.
     *
     * @throws InvalidTimeOffsetException on invalid time offset
     * @throws IllegalArgumentException on invalid arguments.
     */
    public TimestampImpl(String timestampAsString, DateFormat format) throws ParseException {

        if (format == null) {
            throw new IllegalArgumentException("null format");
        }

        this.time = format.parse(timestampAsString).getTime();
        this.timeOffset = TimeOffset.fromRFC822String(timestampAsString);

        if (this.timeOffset == null) {

            //
            // get ehe offset for the default timezone
            //
            int offset = TimeZone.getDefault().getOffset(System.currentTimeMillis());
            this.timeOffset = new TimeOffset(offset);
        }
    }

    // Timestamp implementation ----------------------------------------------------------------------------------------

    @Override
    public long getTime() {

        return time;
    }

    @Override
    public TimeOffset getTimeOffset() {

        return timeOffset;
    }

    @Override
    public String elementToString(String s) {

        DateFormat format = new SimpleDateFormat(s);
        TimeZone synthetic = TimeZone.getTimeZone("UTC");

        // we verified that setting an offset on a TimeZone instance does not have lateral effects on other TimeZone
        // of the same time returned by TimeZone.getTimeZone().
        synthetic.setRawOffset(timeOffset.getOffset());
        format.setTimeZone(synthetic);
        return format.format(time);
    }

    @Override
    public String format(DateFormat format) {

        if (format == null) {
            throw new IllegalArgumentException("null target format");
        }

        long adjustedTimestamp =
                time - TimeZone.getDefault().getOffset(System.currentTimeMillis()) + timeOffset.getOffset();

        return format.format(adjustedTimestamp);
    }

    @Override
    public long adjustTime(TimeOffset timeOffset) {

        return time + this.timeOffset.getOffset() - timeOffset.getOffset();
    }

    // Public ----------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return time + ":" + (timeOffset == null ? null : timeOffset.toRFC822String());
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
