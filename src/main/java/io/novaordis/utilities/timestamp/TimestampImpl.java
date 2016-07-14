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

//    /**
//     * Use this constructor for synthetic events.
//     */
//    public TimestampImpl(long timestampGMT, TimeZone timeZone) {
//
//        this.timestampGMT = timestampGMT;
//        this.timeZone = timeZone;
//    }
//
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


//    @Override
//    public long getOffsetFor(TimeZone timeZone) {
//
//        throw new RuntimeException("adjustForTimeZone() NOT YET IMPLEMENTED");
//
//    /**
//     * Calculates a new GMT-relative timestamp offset for timezone. The value returned allows for correct comparison
//     * of the given log timestamp with a value parsed in the given timezone.
//     *
//     * Example:
//     *
//     * int currentTimezoneOffset = TimeZone.getDefault().getDSTSavings() + TimeZone.getDefault().getRawOffset();
//     *
//     * long timeToCompare = (new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse().getTime();
//     *
//     * long theOtherTimeToCompare = Timestamps.adjustForTimezone(timestampToCompare, currentTimezoneOffset);
//     *
//     * ... compare timeToCompare and timeToCompare
//     *
//     * @param timezoneOffsetMs the timezone offset the event we want to compare with was parsed in.
//     */
//    public static long adjustForTimezone(Timestamp t, int timezoneOffsetMs) {
//
//        Integer timestampTimezoneOffsetMs = t.getTimezoneOffsetMs();
//        long gmt = t.getTimestampGMT();
//
//        if (timestampTimezoneOffsetMs == null) {
//
//            //
//            // the incoming timestamp was not parsed in the contest of a specific timestamp, which means it
//            // used the default one, so adjustment is a noop
//            //
//            return gmt;
//        }
//
//        return gmt + timestampTimezoneOffsetMs - timezoneOffsetMs;
//    }
//
//    }
//
//    @Override
//    public String format(DateFormat format, TimeZone timeZone) {
//
//        return format.format(getTime());
//    }

//    /**
//     * @param s a valid SimpleDateFormat format element.
//     */
//    @Override
//    public String getTimestampElement(String s) {
//
//        DateFormat format = new SimpleDateFormat(s);
//        format.setTimeZone(timeZone);
//        return format.format(time);
//    }

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
