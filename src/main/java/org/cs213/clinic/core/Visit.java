package org.cs213.clinic.core;

/**
 * The Visit class is a representation of a visit which includes an appointment
 * and a reference to the next visit object in the list. This class is mainly
 * utilized during statement generations to confirm a patient has visited a
 * provider.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Visit {
    /**
     * The appointment object that is associated with the visit
     */
    private Appointment appointment;

    /**
     * A reference to the next appointment object in the list
     */
    private Visit next;

    /**
     * Constructs a visit with a specific appointment and a reference to the next
     * visit object in the list.
     * 
     * @param appointment the appointment object associated with the visit
     * @param next        the next visit object in the list
     */
    public Visit(Appointment appointment, Visit next) {
        this.appointment = appointment;
        this.next = next;
    }

    /**
     * Returns the next visit object in the list.
     * 
     * @return the next visit object in the list
     */
    public Visit getNext() {
        return next;
    }

    /**
     * Sets the next visit object in the list.
     * 
     * @param next the next visit object in the list
     */
    public void setNext(Visit next) {
        this.next = next;
    }

    /**
     * Returns the appointment object associated with the visit.
     * 
     * @return the appointment object associated with the visit
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Sets the appointment object associated with the visit.
     * 
     * @param appointment the appointment object associated with the visit
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Returns a string representation of the visit object.
     * 
     * @return a string representation of the visit object
     */
    public String toString() {
        return appointment.toString();
    }
}