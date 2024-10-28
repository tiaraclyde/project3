package org.cs213.clinic.core;

/**
 * The Technician class defines technicians responsible for handling equipment
 * and their charge rate.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Technician extends Provider {
    /**
     * Setup for short description of technician.
     */
    public static final String BRIEF_FORMAT = "%s %s (%s)";

    /**
     * Keeps track of technician's charging rate per visit.
     */
    private int ratePerVisit;

    /**
     * Constructs a technician using the string representations of a profile,
     * @param fname first name for technician
     * @param lname last name for technician
     * @param dob date of birth for technician
     * @param location location of the technician
     * @param rate technician's rate
     */
    public Technician(String fname, String lname, String dob, String location,
                      String rate) {
        super(new Profile(fname, lname, dob));
        if (!Date.parseDate(dob).isValid()) throw new IllegalArgumentException();
        if (!Location.exists(location)) throw new IllegalArgumentException();
        try {
            this.ratePerVisit = Integer.parseInt(rate);
            if (ratePerVisit < 0) throw new IllegalArgumentException();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
        setLocation(Location.valueOf(location.toUpperCase()));
    }

    /**
     * Gets the rate per visit for technician.
     *
     * @return the rate per visit
     */
    @Override
    public int rate() {
        return ratePerVisit;
    }

    /**
     * Gets the string representation of a technician's information.
     *
     * @return technician's information
     */
    @Override
    public String toString() {
        final String doctorInfoFormat = "[%s, %s][rate: $%.2f]";
        return String.format(doctorInfoFormat, profile,
                getLocation(), (double) ratePerVisit);
    }

    /**
     * Gets a short string representation of the technician's information.
     *
     * @return technician's information small ver.
     */
    public String brief() {
        return String.format(BRIEF_FORMAT,
                profile.getFname(), profile.getLname(), getLocation().name());
    }
}
