package util;

import main.org.cs213.clinic.core.Timeslot;

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
