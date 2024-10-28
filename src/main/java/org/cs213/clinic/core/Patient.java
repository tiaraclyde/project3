package org.cs213.clinic.core;

/**
 * The Patient class is a representation of a patient which includes a profile
 * and a linked list of visits (completed appointments). This class is mainly
 * utilized during statement generations to confirm a patient has visited a
 * provider. The class also includes a method to calculate the total charge of
 * all visits.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Patient extends Person implements Comparable<Person> {
    /**
     * A linked list of visits (completed appointments).
     */
    private Visit visits;

    /**
     * Constructs a patient with a specific profile and a linked list of visits
     * (completed appointments).
     * 
     * @param profile the profile object associated with the patient.
     */
    public Patient(Profile profile) {
        this(profile, null);
    }

    /**
     * Constructs a patient with a specific profile and a linked list of visits
     * (completed appointments).
     * 
     * @param profile the profile object associated with the patient
     * @param visits  the linked list of visits (completed appointments)
     */
    public Patient(Profile profile, Visit visits) {
        super(profile);
        this.visits = visits;
    }

    /**
     * Constructs a patient with a specific first name, last name, and date of
     * birth.
     * 
     * @param firstName the first name of the patient
     * @param lastName  the last name of the patient
     * @param dob       the date of birth of the patient
     */
    public Patient(String firstName, String lastName, String dob) {
        this(new Profile(firstName, lastName, dob));
    }

    /**
     * Returns the profile object associated with the patient.
     * 
     * @return a patient object with the specified profile and linked list
     * of visits
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the profile object associated with the patient.
     * 
     * @param profile the profile object associated with the patient
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Returns the linked list of visits (completed appointments).
     * 
     * @return the linked list of visits (completed appointments)
     */
    public Visit getVisits() {
        return visits;
    }

    /**
     * Sets the linked list of visits (completed appointments).
     * 
     * @param visits the linked list of visits (completed appointments)
     */
    public void setVisits(Visit visits) {
        this.visits = visits;
    }

    /**
     * Adds a visit to the linked list of visits (completed appointments).
     * 
     * @param appointment the appointment object associated with the visit
     */
    public void addVisit(Appointment appointment) {
        Visit newVisit = new Visit(appointment, null);
        if (visits == null) {
            visits = newVisit;
            return;
        }

        Visit current = visits;
        while (current.getNext() != null) {
            current = current.getNext();
        }
        current.setNext(newVisit);
    }

    /**
     * Calculates the total charge of all visits (completed appointments).
     * 
     * @return the total charge of all visits (completed appointments)
     */
    public int charge() {
        Visit current = visits;
        int total = 0;
        while (current != null) {
            total += current.getAppointment().getProvider().rate();
            current = current.getNext();
        }
        return total;
    }

    /**
     * Returns a string representation of the patient object.
     * 
     * @return a string representation of the patient object
     */
    @Override
    public String toString() {
        return profile.toString();
    }
}
