package core;

import java.util.Calendar;

/**
 * The Date class is a representation of a medical appointment dates
 * which includes a specific year, month, and day. The Comparable
 * interfaces allows dates to be compared based on year, month and day.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */

public class Date implements Comparable<Date> {
    /**
     * Constant representing the number of years in a quadrennial period.
     */
    public static final int QUADRENNIAL = 4;

    /**
     * Constant representing the number of years in a centennial period.
     */
    public static final int CENTENNIAL = 100;

    /**
     * Constant representing the number of years in a quadricentennial period.
     */
    public static final int QUATERCENTENNIAL = 400;

    /**
     * Minimum valid year.
     */
    private static final int MIN_YEAR = 1900;

    /**
     * Maximum valid year.
     */
    private static final int MAX_YEAR = 9999;

    /**
     * Minimum valid month.
     */
    private static final int MIN_MONTH = 1;

    /**
     * Maximum valid month.
     */
    private static final int MAX_MONTH = 12;

    /**
     * Minimum valid day.
     */
    private static final int MIN_DAY = 1;

    /**
     * Maximum valid day.
     */
    private static final int MAX_DAY = 31;

    /**
     * Constant representing the month of February.
     */
    public static final int FEBRUARY = 2;

    /**
     * Constant representing the number of days in a leap year February.
     */
    public static final int LEAP_YEAR_DAYS = 29;

    /**
     * The maximum valid Date object, set to December 31, 9999.
     */
    public static final Date MAX = new Date(MAX_YEAR, MAX_MONTH, MAX_DAY);

    /**
     * The minimum valid Date object, set to January 1, 1900.
     */
    public static final Date MIN = new Date(MIN_YEAR, MIN_MONTH, MIN_DAY);

    /**
     * Array representing the number of days in each month.
     */
    private static final int[] DAYS_IN_MONTH = { 0, 31, 28, 31, 30, 31, 30,
        31, 31, 30, 31, 30, 31 };

    /**
     * The year of the date.
     */
    private int year;

    /**
     * The month of the date.
     */
    private int month;

    /**
     * The day of the date.
     */
    private int day;

    /**
     * Construct a Date object with the current date. The time is set to
     * 00:00:00.
     *
     * @param date the current date
     */
    public Date(String date) {
        this(parseDate(date));
    }

    /**
     * Construct a Date object with the given year, month, and day. The date
     * must be valid.
     * 
     * @param year  the year
     * @param month the month
     * @param day   the day
     */
    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Construct a Date object with the given Date object. The new Date object
     * is a copy of the given Date object. This does not modify the original
     * Date object.
     * 
     * @param date the Date object to copy
     */
    public Date(Date date) {
        this(date.year, date.month, date.day);
    }

    /**
     * Get the year of the date.
     * 
     * @return the year in the date
     */
    public int getYear() {
        return year;
    }

    /**
     * Get the month of the date.
     * 
     * @return the month of the date
     */
    public int getMonth() {
        return month;
    }

    /**
     * Get the day of the date. The day is between 1 and the number of days in
     * the month. February has 29 days if the year is a leap year.
     * 
     * @return the day of the date
     */
    public int getDay() {
        return day;
    }

    /**
     * Add months to the date. The date must be valid. The new date is the same
     * day of the month as the original date. If the new month has fewer days than
     * the original month, the day is adjusted to the last day of the month. The
     * year is adjusted if the new month is greater than 12. The new date is
     * returned. This does not modify the original date.
     * 
     * @param months the number of months to add
     * @return the new date
     */
    public Date addMonths(int months) {
        int newYear = this.year;
        int newMonth = this.month + months;

        while (newMonth > 12) {
            newMonth -= 12;
            newYear++;
        }

        int newDay = Math.min(this.day, daysInMonth(newMonth, newYear));
        return new Date(newYear, newMonth, newDay);
    }

    /**
     * Check if the date is valid. The year must be greater than 0. The month
     * must be between 1 and 12. The day must be between 1 and the number of
     * days in the month. February has 29 days if the year is a leap year.
     * 
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        if (year < MIN_YEAR || month < MIN_MONTH || month > MAX_MONTH || day < MIN_DAY) {
            return false;
        }
        return day <= daysInMonth(month, year);
    }

    /**
     * Get the number of days in a month of a year. February has 29 days if
     * the year is a leap year. The month must be valid. The year must be
     * greater than 0.
     * 
     * @param month the month
     * @param year  the year
     * @return the number of days in the month
     */
    private int daysInMonth(int month, int year) {
        if (month == FEBRUARY && isLeapYear(year)) {
            return LEAP_YEAR_DAYS;
        }
        return DAYS_IN_MONTH[month];
    }

    /**
     * Check if the year is a leap year. A leap year is divisible by 4, but
     * not by 100 unless it is divisible by 400. The year must be greater than
     * 0.
     * 
     * @param year the year
     * @return true if the year is a leap year, false otherwise
     */
    private boolean isLeapYear(int year) {
        return year % QUADRENNIAL == 0 &&
                (year % CENTENNIAL != 0 || year % QUATERCENTENNIAL == 0);
    }

    /**
     * Check if the date is a weekend (Saturday or Sunday). The date must be
     * valid. This uses the Calendar class to determine the day of the week.
     * 
     * @return true if the date is a weekend, false otherwise
     * @see java.util.Calendar
     */
    public boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day); // Calendar months are 0-based
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    /**
     * Check if two Date objects are equal based on the year, month, and day.
     * 
     * @param obj the object to compare
     * @return true if the Date objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            return (this.year == date.year) && (this.month == date.month) && (this.day == date.day);
        }
        return false;
    }

    /**
     * Get the string representation of the date in the format "MM/DD/YYYY".
     * 
     * @return the date string
     */
    @Override
    public String toString() {
        return String.format("%d/%d/%d", month, day, year);
    }

    /**
     * Compare two Date objects based on the year, month, and day.
     * 
     * @param date the Date object to compare
     * @return 1 if this Date object is greater than other, -1 if smaller, 0 if
     *         equal
     */
    @Override
    public int compareTo(Date date) {
        if (this.year > date.year) {
            return 1;
        } else if (this.year < date.year) {
            return -1;
        }
        if (this.month > date.month) {
            return 1;
        } else if (this.month < date.month) {
            return -1;
        }
        if (this.day > date.day) {
            return 1;
        } else if (this.day < date.day) {
            return -1;
        }
        return 0;
    }

    /**
     * Get the current date. The time is set to 00:00:00. This uses the
     * Calendar class to get the current date.
     * 
     * @return the current date
     * @see java.util.Calendar
     */
    public static Date today() {
        Calendar now = Calendar.getInstance();
        return new Date(now.get(Calendar.YEAR),
                // Calendar months are 0-based
                now.get(Calendar.MONTH) + 1,
                now.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Parse a date string in the format "MM/DD/YYYY" into a Date object.
     * 
     * @param date the date string to parse
     * @return the Date object
     */
    public static Date parseDate(String date) {
        String[] dateParts = date.split("/");
        final int monthIndex = 0, dayIndex = 1, yearIndex = 2;
        int month = Integer.parseInt(dateParts[monthIndex]);
        int day = Integer.parseInt(dateParts[dayIndex]);
        int year = Integer.parseInt(dateParts[yearIndex]);
        return new Date(year, month, day);
    }

    /**
     * Main method for testing the Date class. The main method tests the
     * isValid() method with various test cases.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Test case 1: Invalid month
        Date datetest1 = new Date(2024, 13, 5);
        testisValid(datetest1, false);

        // Test case 2: Invalid date, 2025 is not a leap year
        Date datetest2 = new Date(2025, 2, 29);
        testisValid(datetest2, false);

        // Test case 3: Invalid year (less than 1900)
        Date datetest3 = new Date(1890, 1, 25);
        testisValid(datetest3, false);

        // Test case 4: Invalid month
        Date datetest4 = new Date(2024, 0, 5);
        testisValid(datetest4, false);

        // Test case 5: Valid date (2024 is a leap year)
        Date datetest5 = new Date(2024, 2, 29);
        testisValid(datetest5, true);

        // Test case 6: Valid date
        Date datetest6 = new Date(2024, 12, 1);
        testisValid(datetest6, true);

    }

    /**
     * Tests the validity of a Date object by comparing the result of
     * the isValid() method to the expected result.
     *
     * @param date           the Date object to test for validity
     * @param expectedResult the expected boolean result of the validity test
     */
    private static void testisValid(Date date, boolean expectedResult) {
        // Call the isValid() method on the date object
        boolean result = date.isValid();
        System.out.println("Test Case: " + date);
        System.out.println("Expected Result: " + expectedResult);
        System.out.println("Actual Result: " + result);
        System.out.println("Test " + (result == expectedResult ?
            "PASSED" : "FAILED"));
        System.out.println();
    }
}
