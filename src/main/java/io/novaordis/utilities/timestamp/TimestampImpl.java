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

/**
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/11/16
 */
public class TimestampImpl implements Timestamp {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final DateFormat DAY = new SimpleDateFormat("dd");
    public static final DateFormat MONTH = new SimpleDateFormat("MM");
    public static final DateFormat YEAR = new SimpleDateFormat("yy");

    // Static ----------------------------------------------------------------------------------------------------------

    // Attributes ------------------------------------------------------------------------------------------------------

    private long timestampGMT;
    private Integer timezoneOffsetMs;

    private int day;
    private int month;
    private int year;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * To get 0 GMT with a +3600 timezone offset, use:
     *
     * DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss Z");
     * Timestamp t = getTimestampToTest("01/01/1970 01:00:00 +0100", df);
     *
     * @throws ParseException if the string cannot be parsed into a date using the given format.
     * @throws IllegalArgumentException on invalid arguments.
     */
    public TimestampImpl(String timestampAsString, DateFormat format) throws ParseException {

        if (format == null) {
            throw new IllegalArgumentException("null format");
        }

        long tsGMT = format.parse(timestampAsString).getTime();

        setTimestampGMT(tsGMT);

        Integer toMs = Timestamps.timezoneOffsetMsFromString(timestampAsString);
        setTimezoneOffsetMs(toMs);

        day = Integer.parseInt(DAY.format(tsGMT));
        month = Integer.parseInt(MONTH.format(tsGMT));
        year = Integer.parseInt(YEAR.format(tsGMT));
    }

    // Timestamp implementation ----------------------------------------------------------------------------------------

    @Override
    public long getTimestampGMT() {

        return timestampGMT;
    }

    @Override
    public Integer getTimezoneOffsetMs() {

        return timezoneOffsetMs;
    }

    @Override
    public int getDay() {

        return day;
    }

    @Override
    public int getMonth() {

        return month;
    }

    @Override
    public int getYear() {

        return year;
    }


    // Public ----------------------------------------------------------------------------------------------------------

    public void setTimestampGMT(long l) {

        if (l < 0 ) {
            throw new IllegalArgumentException("illegal timestamp negative value");
        }

        this.timestampGMT = l;
    }

    public void setTimezoneOffsetMs(Integer timezoneOffsetMs) {

        if (timezoneOffsetMs != null && !Timestamps.isValidTimeZoneOffsetMs(timezoneOffsetMs)) {
            throw new IllegalArgumentException("invalid timezone offset value");
        }

        this.timezoneOffsetMs = timezoneOffsetMs;
    }

    @Override
    public String toString() {

        return "" +
                timestampGMT +
                (timezoneOffsetMs == null ? "" : ":" + Timestamps.timezoneOffsetMsToString(timezoneOffsetMs));

    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
