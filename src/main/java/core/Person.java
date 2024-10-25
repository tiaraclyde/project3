package core;

/**
 * The Person class is a superclass for the Patient and Provider subclasses. It
 * holds a profile property that has a protected specifier for classes that
 * extend it.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Person implements Comparable<Person> {
    /**
     * Common characteristics such as first name, last name, and dob.
     */
    protected Profile profile;

    /**
     * Initializes the Person object with the given profile.
     *
     * @param profile profile that contains attributes
     */
    public Person(Profile profile) {
        this.profile = profile;
    }

    /**
     * Get profile object with person identifiers.
     *
     * @return the profile of the person
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Set the profile object for person object.
     *
     * @param profile profile to set to
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Compares the person object with the other specified person object for
     * order.
     *
     * @param o the object to be compared.
     * @return a value that represents the comparison evaluated based on the
     *          profile obj
     */
    @Override
    public int compareTo(Person o) {
        return profile.compareTo(o.profile);
    }

    /**
     * Gets a string representation of person based on profile.
     *
     * @return string representation of profile
     */
    @Override
    public String toString() {
        return profile.toString();
    }

    /**
     * Checks if the passed attributes of a profile matches the person's
     * profile.
     *
     * @param fname first name of profile
     * @param lname last name of profile
     * @param dob dob of profile
     * @return if the profiles match and in turn the person
     */
    public boolean equals(String fname, String lname, String dob) {
        return profile.equals(new Profile(fname, lname, dob));
    }
}
