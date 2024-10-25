package core;

import main.org.cs213.clinic.util.Format;

/**
 * The Appointment class is a representation of a medical appointment
 * which includes a specific date, timeslot, patient profile, and provider.
 * The Comparable interfaces allows appointments to be compared based on their
 * date and timeslot.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Appointment implements Comparable<Appointment> {
    /**
     * The date of the appointment.
     */
    protected Date date;

    /**
     * The timeslot of the appointment.
     */
    protected Timeslot timeslot;

    /**
     * The patient for the appointment.
     */
    protected Person patient;

    /**
     * The provider for the appointment.
     */
    protected Person provider;

    /**
     * Constructs an Appointment with a specific date, timeslot, patient, and
     * provider.
     *
     * @param date     the date of the appointment
     * @param timeslot the timeslot of the appointment
     * @param patient  the patient of the appointment
     * @param provider the provider of the appointment
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * Returns the date of the appointment.
     *
     * @return the date of the appointment
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the timeslot of the appointment.
     *
     * @return the timeslot of the appointment
     */
    public Timeslot getTimeslot() {
        return timeslot;
    }

    /**
     * Get the patient of whom the appointment is for.
     *
     * @return the patient of the appointment
     */
    public Person getPatient() {
        return patient;
    }

    /**
     * Get the provider for this appointment.
     *
     * @return the provider of the appointment
     */
    public Provider getProvider() {
        return (Provider) provider;
    }

    /**
     * Sets the date for the appointment.
     *
     * @param date the new date of the appointment
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the timeslot of the appointment.
     *
     * @param timeslot the new timeslot of the appointment
     */
    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    /**
     * Sets a new patient for the appointment.
     *
     * @param patient the new patient for the appointment
     */
    public void setPatient(Person patient) {
        this.patient = patient;
    }

    /**
     * Sets the provider of the appointment.
     *
     * @param provider the new provider for the appointment
     */
    public void setProvider(Person provider) {
        this.provider = provider;
    }

    /**
     * Checks if the following date and timeslot conflicts with the existing
     * appointment.
     *
     * @param date the date to check against
     * @param timeslot the timeslot to check against
     * @return if there was a conflict
     */
    public boolean conflicts(Date date, Timeslot timeslot) {
        return this.date.equals(date) &&
                this.timeslot.equals(timeslot);
    }

    /**
     * Override the equals method to determine if appointments objects are
     * equal. We assume if the dates, timeslots, and profiles are equal it is
     * the same appointment.
     *
     * @param obj the object to be compared
     * @return return true if two appointment objects are equal; return false
     *         otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Appointment other)) { return false; }

        return this.date.equals(other.date) &&
                this.timeslot.equals(other.timeslot) &&
                this.patient.equals(other.patient) &&
                this.provider.equals(other.provider);
    }

    /**
     * Return a textual representation of the Appointment object. The string
     * contains the date, timeslot, patient's first name, patient's last name,
     * patient's date of birth, provider's name, provider's location, and
     * provider's specialty.
     *
     * @return a string containing the Appointment date, timeslot, patients'
     * first name, patients' last name, patients' date of birth, provider's
     * name, provider's location, and provider's specialty.
     */
    @Override
    public String toString() {
        String date = this.date.toString();
        String timeslot = Format.get12Hour(this.timeslot);
        String patient = this.patient.toString();
        String provider = this.provider.toString();
        return String.format("%s %s %s %s", date, timeslot, patient, provider);
    }

    /**
     * Compare two Appointment objects based on the "key" value. Assume the
     * date of the appointment is the "key". Note that, typically, the "key"
     * is consistent with the key in the equals().
     *
     * @param appointment the appointment object to be compared.
     * @return return 1 if this appointment object is greater than
     * "appointment", return -1 if smaller; return 0 if they are equal.
     */
    @Override
    public int compareTo(Appointment appointment) {
        return this.date.compareTo(appointment.date);
    }
}
