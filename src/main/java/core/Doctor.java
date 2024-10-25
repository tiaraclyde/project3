package core;

/**
 * The Doctor class defines doctors for patients that can be appended to
 * corresponding appointments.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Doctor extends Provider {
    /**
     * Encapsulate the rate per visit based on specialty.
     */
    private Specialty specialty;

    /**
     * National Provider Identification unique to doctor.
     */
    private String npi;

    /**
     * Constructors a doctor using the string representations of a profile,
     * location, specialty, and NPI.
     *
     * @param fname first name for doctor
     * @param lname last name for doctor
     * @param dob date of birth for doctor
     * @param location location of the doctor
     * @param specialty specialty for doctor
     * @param npi national provider identification number for doctor
     */
    public Doctor(String fname, String lname, String dob,
                  String location, String specialty, String npi) {
        super(new Profile(fname, lname, dob));
        if (!Date.parseDate(dob).isValid()) throw new IllegalArgumentException();
        if (!Specialty.exists(specialty)) throw new IllegalArgumentException();
        if (!Location.exists(location)) throw new IllegalArgumentException();
        setLocation(Location.valueOf(location.toUpperCase()));
        this.specialty = Specialty.valueOf(specialty.toUpperCase());
        this.npi = npi;
    }

    /**
     * Gets specialty for doctor.
     *
     * @return the specialty of the doctor
     */
    public Specialty getSpecialty() {
        return specialty;
    }

    /**
     * Set specialty for doctor.
     *
     * @param specialty the new specialty to set to
     */
    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    /**
     * Gets NPI identifier for doctor.
     *
     * @return the NPI of doctor
     */
    public String getNpi() {
        return npi;
    }

    /**
     * Set NPI identifier for doctor.
     *
     * @param npi the new NPI for doctor
     */
    public void setNpi(String npi) {
        this.npi = npi;
    }

    /**
     * Gets the rate for doctor.
     *
     * @return the rate of doctor
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    /**
     * Gets the string representation of a doctor's information.
     *
     * @return doctor's information
     */
    @Override
    public String toString() {
        final String doctorInfoFormat = "[%s, %s][%s, #%s]";
        return String.format(doctorInfoFormat, profile, getLocation(),
                specialty.name(), getNpi());
    }
}
