package core;

/**
 * The Timeslot enum contains the timeslots for each appointment
 * by their hour and minutes. The timeslots are used to schedule
 * appointments for patients and providers.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Timeslot implements Comparable<Timeslot> {
    /**
     * Maximum hour in a day.
     */
    public static final int MAX_HOUR = 23;

    /**
     * Minimum hour in a day.
     */
    public static final int MIN_HOUR = 0;

    /**
     * Maximum minutes in an hour.
     */
    public static final int MAX_MINUTES = 59;

    /**
     * Minimum minutes in an hour.
     */
    public static final int MIN_MINUTES = 0;

    /**
     * Number of hours in a day.
     */
    public static final int NUM_OF_HOURS = 24;

    /**
     * Numbers of minutes in an hour.
     */
    public static final int NUM_OF_MINUTES = 60;

    /**
     * Timeslot id offset.
     */
    public static final int START_ID = 1;

    /**
     * Timeslot id used to represent none.
     */
    public static final int NONE_ID = -1;

    /** The hour of the timeslot. */
    private final int hour;
    /** The minutes of the timeslot. */
    private final int minutes;

    /**
     * Constructs a timeslot with a specific hour and minutes. This follows
     * the 24-hour format for initialization.
     * 
     * @param hour    the hour of the timeslot
     * @param minutes the minutes of the timeslot
     */
    public Timeslot(int hour, int minutes) {
        if (hour < MIN_HOUR || hour > MAX_HOUR) {
            throw new IllegalArgumentException("Invalid hour: " + hour);
        }
        if (minutes < MIN_HOUR || minutes > MAX_HOUR) {
            throw new IllegalArgumentException("Invalid minutes: " + minutes);
        }
        this.hour = hour;
        this.minutes = minutes;
    }

    /**
     * Constructs a timeslot with an applied offset of hours and minutes on a
     * starting time.
     *
     * @param hour start hour
     * @param minutes start minutes
     * @param offsetHrs offset for hours
     * @param offsetMins offset for minutes
     */
    public Timeslot(int hour, int minutes, int offsetHrs, int offsetMins) {
        hour += offsetHrs;
        minutes += offsetMins;

        // Normalize minutes and hours
        if (minutes > MAX_MINUTES) {
            hour += minutes / NUM_OF_MINUTES; // Add extra hours
            minutes = minutes % NUM_OF_MINUTES; // Keep remaining minutes
        } else if (minutes < MIN_MINUTES) {
            hour -= 1; // Subtract one hour
            // Adjust to positive minutes
            minutes = (minutes + NUM_OF_MINUTES) % NUM_OF_MINUTES;
        }

        // Normalize hour
        if (hour > MAX_HOUR) {
            hour = hour % NUM_OF_HOURS; // Wrap around if necessary
        } else if (hour < MIN_HOUR) {
            // Handle negative hours
            hour = (hour + NUM_OF_HOURS) % NUM_OF_HOURS;
        }

        this.hour = hour;
        this.minutes = minutes;
    }

    /**
     * Returns the hour of the timeslot.
     * 
     * @return the hour of the timeslot
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the minutes of the timeslot.
     * 
     * @return the minutes of the timeslot
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Check if two timeslot objects are equal based on the hour and minute.
     *
     * @param obj the object to compare
     * @return true if the timeslot objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Timeslot) {
            Timeslot timeslot = (Timeslot) obj;
            return (this.hour == timeslot.hour) &&
                (this.minutes == timeslot.minutes);
        }
        return false;
    }

    /**
     * Get the string representation of the timeslot in the format "HH:MM".
     *
     * @return the timeslot string
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minutes);
    }

    /**
     * Compare two timeslots objects based on the hour and minute.
     *
     * @param timeslot the Timeslot object to compare
     * @return 1 if this Date object is greater than other, -1 if smaller, 0 if
     *         equal
     */
    @Override
    public int compareTo(Timeslot timeslot) {
        if (this.hour > timeslot.hour) {
            return 1;
        } else if (this.hour < timeslot.hour) {
            return -1;
        } else if (this.minutes > timeslot.minutes) {
            return 1;
        } else if (this.minutes < timeslot.minutes) {
            return -1;
        } else {
            return 0;
        }
    }
}
