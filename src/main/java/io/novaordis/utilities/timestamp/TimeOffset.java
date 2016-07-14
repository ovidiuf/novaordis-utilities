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
 * A time offset is an amount of time, in milliseconds, subtracted from or added to UTC to get the current wall clock
 * time – whether it's standard time or daylight saving time. For a time zone where the daylight saving time is not in
 * effect, the time offset is the time zone offset. For a time zone where the daylight saving time is in effect, the
 * time offset is the time zone offset + the daylight saving time offset (usually one hour).
 *
 * @link https://kb.novaordis.com/index.php/Time#Time_Offset
 *
 * @author Ovidiu Feodorov <ovidiu@novaordis.com>
 * @since 7/13/16
 */
public class TimeOffset {

    // Constants -------------------------------------------------------------------------------------------------------

    public static final int LOWEST_VALID_TIME_OFFSET = -12 * 3600 * 1000;
    public static final int HIGHEST_VALID_TIME_OFFSET = 14 * 3600 * 1000;

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Scan the string for the first RFC822 representation of a time offset (±hhmm) and return the corresponding
     * TimeOffset instance, if it finds one. Return null if it cannot detect a time offset in RFC822 format. If the
     * method finds something that might be a RFC822 time offset but it is improperly formatted, it throws an
     * InvalidTimeOffsetException.
     *
     * @return the TimeOffset instance corresponding to the first RFC822 time offset identified in the string, or null
     * if no time offset is identified.
     *
     * @throws InvalidTimeOffsetException in case of improperly formatted timestamp string
     *
     * @see TimeOffset#toRFC822String()
     */
    public static TimeOffset fromRFC822String(String s) throws InvalidTimeOffsetException {

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
        // in order to qualify, the separator must be either at the beginning of the string, or it must follow
        // after a space
        //

        if (separatorIndex != 0 && s.charAt(separatorIndex - 1) != ' ') {
            //
            // does not qualify
            //
            return null;
        }

        int end = separatorIndex + 5;
        end = end > s.length() ? s.length() : end;
        s = s.substring(separatorIndex, end);

        return new TimeOffset(s);
    }

    /**
     * @return the default time offset, the one corresponding to the local time zone in this moment in time.
     */
    public static TimeOffset getDefault() {

        return new TimeOffset(TimeZone.getDefault().getOffset(System.currentTimeMillis()));
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    private int timeOffsetMs;

    // Constructors ----------------------------------------------------------------------------------------------------

    /**
     * The UTC time offset. We're not exposing this publicly to avoid confusion with the value returned by
     * TimeOffset.getDefault(). If we want an UTC time offset, build it explicitly with new TimeOffset(0)
     */
    @SuppressWarnings("unused")
    private TimeOffset() {

        this(0);
    }

    /**
     * @param offset the time offset in milliseconds.
     *
     * @exception InvalidTimeOffsetException on invalid values
     */
    public TimeOffset(int offset) throws InvalidTimeOffsetException {

        setOffset(offset);
    }

    /**
     * @param s a String in RFC822 format
     *
     * @exception InvalidTimeOffsetException on invalid values
     */
    public TimeOffset(String s) throws InvalidTimeOffsetException {

        //
        // special cases
        //
        if ("0000".equals(s)) {
            setOffset(0);
            return;
        }

        boolean positive = true;

        if (s.startsWith("-")) {
            positive = false;
        }
        else if (!s.startsWith("+")) {

            throw new InvalidTimeOffsetException("time offset representation must start with + or -");
        }

        //
        // we expect four digits
        //

        int crt = 1;
        int multiplier = 10 * 3600 * 1000;
        int offset = 0;

        while(crt < 5) {

            if (crt >= s.length()) {

                throw new InvalidTimeOffsetException("incomplete RFC822 time offset fragment \"" + s + "\"");
            }

            char c = s.charAt(crt);

            if (c < '0' || c > '9') {

                throw new InvalidTimeOffsetException(
                        "invalid RFC822 time offset \"" + s + "\", \"" + c + "\" not a digit");
            }

            offset += multiplier * ((int)c - 48);
            multiplier /= 10;
            if (multiplier == 360000) {
                multiplier = 10 * 60 * 1000;
            }
            crt ++;
        }

        offset = (positive ? 1 : -1) * offset;
        setOffset(offset);
    }

    // Public ----------------------------------------------------------------------------------------------------------

    /**
     * @return the time zone offset in milliseconds.
     */
    public int getOffset() {

        return timeOffsetMs;
    }

    public void setOffset(int offset) throws InvalidTimeOffsetException {

        if (offset < LOWEST_VALID_TIME_OFFSET || offset > HIGHEST_VALID_TIME_OFFSET) {
            throw new InvalidTimeOffsetException(
                    "time offset not within expected limits " +
                            new TimeOffset(LOWEST_VALID_TIME_OFFSET).toRFC822String() + "..." +
                            new TimeOffset(HIGHEST_VALID_TIME_OFFSET).toRFC822String());
        }

        this.timeOffsetMs = offset;
    }

    public String toRFC822String() {

        String s;

        if (timeOffsetMs < 0) {
            s = "-";
        }
        else {
            s = "+";
        }

        int abs = Math.abs(timeOffsetMs);

        int tensOfHours = abs / (10 * 3600 * 1000);

        s += tensOfHours;

        abs -= 10 * tensOfHours * 3600 * 1000;

        int hours = abs / (3600 * 1000);

        s += hours;

        abs -= hours * 3600 * 1000;

        int tensOfMinutes = abs / (10 * 60 * 1000);

        s += tensOfMinutes;

        abs -= tensOfMinutes * 10 * 60 * 1000;

        int minutes = abs / (60 * 1000);

        s += minutes;

        return s;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof TimeOffset)) {
            return false;
        }

        TimeOffset that = (TimeOffset)o;
        return timeOffsetMs == that.timeOffsetMs;
    }

    @Override
    public int hashCode() {

        return 17 + 7 * timeOffsetMs;
    }

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------

}
