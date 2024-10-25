package core;

/**
 * The Profile class represents a profile with a first name, last name, and
 * date of birth (dob). Profiles can be compared based on last name, first
 * name, and date of birth. This class overrides the equals and toString
 * methods for checking equality and providing a string representation.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */

public class Profile implements Comparable<Profile> {
    /**
     * The first name of the profile.
     */
    private String fname;

    /**
     * The last name of the profile.
     */
    private String lname;

    /**
     * The date of birth of the profile.
     */
    private Date dob;

    /**
     * Constructs a Profile object with the specified first name, last name,
     * and date of birth.
     * 
     * @param fname the first name of the profile
     * @param lname the last name of the profile
     * @param dob   the date of birth of the profile
     */
    public Profile(String fname, String lname, String dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = Date.parseDate(dob);
    }

    /**
     * Gets the first name of profile.
     *
     * @return first name
     */
    public String getFname() {
        return fname;
    }

    /**
     * Gets the last name of profile.
     *
     * @return last name
     */
    public String getLname() {
        return lname;
    }

    /**
     * Returns the date of birth of the profile.
     *
     * @return the date of birth of the profile
     */
    public Date getDob() {
        return dob;
    }

    /**
     * Indicates whether some other object is "equal to" this profile.
     * Two profiles are considered equal if their first name, last name,
     * and date of birth are identical.
     *
     * @param obj the object to compare with
     * @return true if this profile is equal to the specified object if it is
     *         not return false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Profile) {
            Profile profile = (Profile) obj;
            return this.fname.equals(profile.fname) &&
                    this.lname.equals(profile.lname) &&
                    this.dob.toString().equals(profile.dob.toString());
        }
        return false; // take a look back
    }

    /**
     * Returns a string representation of the profile in the format "fname
     * lname dob".
     * 
     * @return a string representation of the profile
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob.toString();
    }

    /**
     * Compares this profile to another profile. Profiles are compared by last
     * name, first name, then by date of birth if the names are identical.
     *
     * @param o the other profile to compare to
     * @return -1 if this profile is less than the specified profile,
     *         0 if this profile equal to the specified profile
     *         1 if this profile is greater than the specified profile
     */
    @Override
    public int compareTo(Profile o) {
        int lnameCompare = this.lname.compareTo(o.lname);
        if (lnameCompare != 0) {
            if (lnameCompare < 0)
                lnameCompare = -1;
            else
                lnameCompare = 1;
            return lnameCompare;
        }

        int fnameCompare = this.fname.compareTo(o.fname);
        if (fnameCompare != 0) {
            if (fnameCompare < 0)
                fnameCompare = -1;
            else
                fnameCompare = 1;
            return fnameCompare;
        }

        return this.dob.compareTo(o.dob);
    }

    /**
     * Test bed main method to thoroughly test the compareTo() method. If
     * the method works correctly, the output should indicate that all test
     * cases passed. If any test case fails, the output will indicate which
     * test case failed and the expected vs. actual result.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Test case 1: Compare profiles with different last names
        Profile p1 = new Profile("John", "Doe", "01/01/1990");
        Profile p2 = new Profile("John", "Smith", "01/02/1991");
        testCompareTo(p1, p2, "Different last names", -1);

        // Test case 2: Compare profiles with same last name, different first names
        Profile p3 = new Profile("Alice", "Johnson", "03/03/1992");
        Profile p4 = new Profile("Bob", "Johnson", "04/04/1993");
        testCompareTo(p3, p4, "Same last name, different first names", -1);

        // Test case 3: Compare profiles with same names, different DOB
        Profile p5 = new Profile("Chris", "Brown", "05/05/1994");
        Profile p6 = new Profile("Chris", "Brown", "06/06/1995");
        testCompareTo(p5, p6, "Same names, different DOB", -1);

        // Test case 4: Compare identical profiles
        Profile p7 = new Profile("David", "Wilson", "07/07/1996");
        Profile p8 = new Profile("David", "Wilson", "07/07/1996");
        testCompareTo(p7, p8, "Identical profiles", 0);

        // Test case 5: Compare profiles with same last and first name, different DOB
        Profile p9 = new Profile("Emily", "Taylor", "08/08/1997");
        Profile p10 = new Profile("Emily", "Taylor", "09/09/1998");
        testCompareTo(p9, p10, "Same names, different DOB", -1);

        // Test case 6: Compare profiles with names as substrings
        Profile p11 = new Profile("John", "Doe", "10/10/1999");
        Profile p12 = new Profile("Johnny", "Doer", "11/11/2000");
        testCompareTo(p11, p12, "Names as substrings", -1);

        // Test case 7: Compare profiles with same last name, different first name
        // (reverse order)
        Profile p13 = new Profile("Bob", "Johnson", "04/04/1993");
        Profile p14 = new Profile("Alice", "Johnson", "03/03/1992");
        testCompareTo(p13, p14, "Same last name, different first names (reverse order)", 1);

        // Test case 8: Compare profiles with same names, different DOB (reverse order)
        Profile p15 = new Profile("Chris", "Brown", "06/06/1995");
        Profile p16 = new Profile("Chris", "Brown", "05/05/1994");
        testCompareTo(p15, p16, "Same names, different DOB (reverse order)", 1);
    }

    /**
     * Helper method to test and print results of compareTo() method.
     * 
     * @param p1             The first profile to compare.
     * @param p2             The second profile to compare.
     * @param testCase       Description of the test case.
     * @param expectedResult The expected result of the comparison
     *                       (-1, 0, or 1).
     */
    private static void testCompareTo(Profile p1, Profile p2,
                                      String testCase, int expectedResult) {
        int result = p1.compareTo(p2);
        System.out.println("Test Case: " + testCase);
        System.out.println("Profile 1: " + p1);
        System.out.println("Profile 2: " + p2);
        System.out.println("Expected Result: " + expectedResult);
        System.out.println("Actual Result: " + result);
        System.out.println("Test " + (result == expectedResult ?
            "PASSED" : "FAILED"));
        System.out.println();
    }
}