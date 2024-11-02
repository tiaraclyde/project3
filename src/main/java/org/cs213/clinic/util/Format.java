package org.cs213.clinic.util;

import org.cs213.clinic.core.Timeslot;

/**
 * The Format class defines common specific formats for the
 * information stored in many of the scheduler's enums.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Format {
    /**
     * Initializes the Format object. This constructor is empty because
     * the class is only used to store static methods.
     */
    public Format() {}

    /**
     * This method capitalizes the first letter of a string and lowercases
     * the rest of the string. This is useful for formatting names.
     *
     * @param str the string to capitalize
     * @return the capitalized string
     */
    public static String capitalize(String str) {
        final int rest = 1;
        return str.substring(0, rest).toUpperCase() + str.substring(rest).toLowerCase();
    }

    /**
     * This method takes a date string in the format of "MM/DD/YYYY" and
     * converts it to the format "DD/MM/YYYY". This is useful for converting
     * dates from UK format to US format. The delimiter can be any character
     * that separates the date parts.
     *
     * @param date the date to convert
     * @param delimiter the delimiter separating the date parts
     * @param newDelimiter the delimiter to replace the old delimiter
     * @return the date in US format
     */
    public static String ukToUsDate(String date, String delimiter, String newDelimiter) {
        String[] dateParts = date.split(delimiter);
        return String.format("%s%s%s%s%s",
                dateParts[1], newDelimiter, dateParts[2], newDelimiter, dateParts[0]);
    }
    
    /**
     * This method takes a 24-hour timeslot and converts it into a 12-hour
     * string representation. Essentially, when hours are greater than 12
     * it subtracts 12 hours and changes the append "PM" period. Otherwise, a
     * default "AM" period is attached. Another edge case is 0 where the clock
     * hits 12 AM.
     * 
     * @param timeslot the timeslot to reinterpret
     * @return a 12-hour string representation of the timeslot
     */
    public static String get12Hour(Timeslot timeslot) {
        int hour = timeslot.getHour();
        int minutes = timeslot.getMinutes();
        String period = "AM";

        if (hour >= 12) {
            period = "PM";
            hour -= 12;
        }

        if (hour == 0) {
            hour = 12;
        }

        return String.format("%d:%02d %s",
                hour, minutes, period);
    }

    /**
     * This method takes a timeslot and creates a 24-hour representation of its
     * hours and minutes. Since timeslot are already stored as 24 hour times
     * this method uses a format string and extracts the hours and minutes
     * respectively.
     * 
     * @param timeslot the timeslot to create a representation for
     * @return the 24-hour string formatted version of the timeslot
     */
    public static String get24Hour(Timeslot timeslot) {
        return String.format("%d:%02d",
                timeslot.getHour(), timeslot.getMinutes());
    }
}
