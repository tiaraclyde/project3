package core;

import main.org.cs213.clinic.util.CustomComparator;
import main.org.cs213.clinic.util.List;
import main.org.cs213.clinic.util.Sort;

import static main.org.cs213.clinic.cli.commands.Command.EMPTY_OUTSTR;

/**
 * This class represents a database that holds all the clinic's data. This
 * class provides methods to retrieve medical records, appointments and
 * timeslots, as well as to find the provider for a specific appointment
 * based on patient details.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Database {
    /**
     * The list of supported timeslots.
     */
    private final List<Timeslot> timeslots;

    /**
     * The patients associated with this database.
     */
    private final List<Patient> medicalRecord;

    /**
     * The list of providers associated with this database.
     */
    private final List<Provider> providers;

    /**
     * Rotation index for technicians within this database.
     */
    private int rotationIndex;

    /**
     * The list of appointments associated with this database.
     */
    private final List<Appointment> appointments;

    /**
     * Constructs database instance for a clinic.
     */
    public Database() {
        this.timeslots = new List<>();
        this.medicalRecord = new List<>();
        this.appointments = new List<>();
        this.providers = new List<>();
        this.rotationIndex = 0;
    }

    /**
     * Adds timeslot instance into the database.
     *
     * @param timeslot timeslot to add
     */
    public void addTimeslot(Timeslot timeslot) {
        timeslots.add(timeslot);
    }

    /**
     * Gets the timeslot by id from the database.
     *
     * @param id the timeslot id
     * @return the timeslot object
     */
    public Timeslot getTimeslot(int id) {
        int iter = Timeslot.START_ID;
        for (Timeslot timeslot : timeslots) {
            if (iter++ == id) {
                return timeslot;
            }
        }
        return null;
    }

    /**
     * Gets the timeslot by a string representation of its id from the
     * database.
     *
     * @param id id in string form
     * @return the timeslot object
     */
    public Timeslot getTimeslot(String id) {
        try {
            return getTimeslot(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Checks the existence of a timeslot in the database.
     *
     * @param id the timeslot id
     * @return if the timeslot exists
     */
    public boolean timeslotExists(int id) {
        return getTimeslot(id) != null;
    }

    /**
     * Creates a string that presents and separates timeslots with new lines.
     *
     * @return all timeslots in database as string
     */
    public String getTimeslotsAsString() {
        StringBuilder builder = new StringBuilder();
        for (Timeslot timeslot : timeslots) {
            builder.append(timeslot);
            builder.append("\n");
        }
        return builder.toString();
    }

    /**
     * Gets patient from database through attributes.
     *
     * @param fnameToken first name of patient
     * @param lnameToken last name of patient
     * @param dobToken   date of birth of patient
     * @return the patient found or null if not found
     */
    public Patient getPatient(String fnameToken, String lnameToken, String dobToken) {
        for (Patient patient : medicalRecord) {
            if (patient.equals(fnameToken, lnameToken, dobToken)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Adds patient instance into the database.
     *
     * @param patient patient to add
     */
    public void addPatient(Patient patient) {
        medicalRecord.add(patient);
    }

    /**
     * Gets the patient from the database. If the patient does not exist a new
     * patient is added to the database.
     *
     * @param fname first name of the patient
     * @param lname last name of the patient
     * @param dob   date of birth of the patient
     * @return the patient found or created
     */
    public Patient getOrCreatePatient(String fname, String lname, String dob) {
        Patient patient = getPatient(fname, lname, dob);
        if (patient == null) {
            patient = new Patient(fname, lname, dob);
            addPatient(patient);
        }
        return patient;
    }

    /**
     * Gets patients from the database as a list.
     * @return patients the list of patients
     */
    public List<Patient> getPatients() {
        List<Patient> patients = new List<>();
        for (Patient patient : medicalRecord) {
            patients.add(patient);
        }
        return patients;
    }

    /**
     * Sorts patients list inside the database through custom comparator.
     *
     * @param comparator sorting methodology
     */
    public void sortPatients(CustomComparator<Patient> comparator) {
        Sort.bubbleSort(medicalRecord, comparator);
    }

    /**
     * Adds provider instance into the database.
     *
     * @param provider provider to add
     */
    public void addProvider(Provider provider) {
        providers.add(provider);
    }

    /**
     * Gets all providers in the database.
     *
     * @return a list copy of the providers
     */
    public List<Provider> getProviders() {
        List<Provider> providers = new List<>();
        for (Provider provider : this.providers) {
            providers.add(provider);
        }
        return providers;
    }

    /**
     * Creates a string that presents and separates providers with new lines.
     * Sorted by last name
     *
     * @return all providers in database as a string
     */
    public String getProvidersAsString() {
        Provider[] array = new Provider[providers.size()];
        for (int i = 0; i < providers.size(); i++) {
            array[i] = providers.get(i);
        }
        Sort.bubbleSort(array, (a, b) -> a.getProfile().getLname()
            .compareTo(b.getProfile().getLname()));
        StringBuilder builder = new StringBuilder();
        for (Provider provider : array) {
            builder.append(provider);
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * Creates a string that presents the technicians in the rotation.
     *
     * @return all technicians in rotation
     */
    public String getRotationAsString() {
        if (providers.isEmpty()) {
            return EMPTY_OUTSTR;
        }

        final String delimiter = " --> ";
        StringBuilder builder = new StringBuilder();
        alignRotation();
        for (Technician technician : getTechnicians()) {
            if (!builder.isEmpty()) {
                builder.append(delimiter);
            }
            builder.append(technician.brief());
        }
        return builder.append("\n").toString();
    }

    /**
     * Gets Doctor instance from the database through their NPI.
     *
     * @param npi npi to look for in the database
     * @return the Doctor found
     */
    public Doctor getDoctor(String npi) {
        for (Provider provider : providers) {
            if (!(provider instanceof Doctor doctor)) continue;
            if (doctor.getNpi().equals(npi)) {
                return (Doctor) provider;
            }
        }
        return null;
    }

    /**
     * Checks if a Doctor exists in the database through their NPI.
     *
     * @param npi npi to check existence through
     * @return true if the doctor exists
     */
    public boolean doctorExists(String npi) {
        return getDoctor(npi) != null;
    }

    /**
     * Checks if the service exists in the database through the service name.
     *
     * @param service service to search for
     * @return true if the service exists
     */
    public boolean serviceExists(String service) {
        for (Radiology radiology : Radiology.values()) {
            if (radiology.name().equalsIgnoreCase(service)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the rotationIndex to the correct position.
     *
     * @return if the rotation alignment as successful
     */
    private boolean alignRotation() {
        final int end = rotationIndex;
        int index = rotationIndex;
        do {
            Provider provider = providers.get(index);
            if (provider instanceof Technician) {
                rotationIndex = index;
                return true;
            }
            index = (index - 1 + providers.size()) % providers.size();
        } while (index != end);

        return false;
    }

    /**
     * Gets the next technician in the order.
     *
     * @return the next technician in rotation
     */
    public Technician nextTechnician() {
        if (!alignRotation()) { return null; }
        final int end = rotationIndex;
        int index = rotationIndex;
        do {
            index = (index - 1 + providers.size()) % providers.size();
            Provider provider = providers.get(index);
            if (!(provider instanceof Technician)) { continue; }
            rotationIndex = index;
            return (Technician) provider;
        } while (index != end);

        return (Technician) providers.get(rotationIndex);
    }

    /**
     * Gets the current technician at the internal rotation index.
     *
     * @return the technician at the rotation index
     */
    public Technician getTechnician() {
        if (!alignRotation()) { return null; }
        return (Technician) providers.get(rotationIndex);
    }

    /**
     * Gets all technicians relative to the rotation index.
     *
     * @return list of technicians relative to ration index
     */
    public List<Technician> getTechnicians() {
        Technician end = getTechnician();
        Technician technician = end;
        List<Technician> technicians = new List<>();
        do {
            technicians.add(technician);
            technician = nextTechnician();
        } while (technician != end);
        return technicians;
    }

    /**
     * Checks if imaging room is in use at a specific location and timeslot.
     *
     * @param location location of room
     * @param timeslot the time of the appoinment
     * @param room room to check use of
     * @return if room is in use using boolean
     */
    public boolean roomInUse(Location location, Timeslot timeslot, Radiology room) {
        for (Appointment appointment : appointments) {
            if (!(appointment instanceof Imaging imaging)) { continue; }
            Technician technician = (Technician) appointment.getProvider();
            if (technician.getLocation().equals(location) &&
                    imaging.getTimeslot().equals(timeslot) &&
                    imaging.getRoom() == room) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an appointment to the database.
     *
     * @param appointment the appointment instance to add
     */
    public void addAppointment(Appointment appointment) {
        if (appointment == null) throw new IllegalArgumentException();
        appointments.add(appointment);
    }

    /**
     * Removes appointment from database.
     *
     * @param appointment appointment to remove
     */
    public void removeAppointment(Appointment appointment) {
        if (appointment == null) return;
        appointments.remove(appointment);
    }

    /**
     * Will delete appointment from the database based on the date, timeslot
     * and patient.
     *
     * @param date the date to match to
     * @param timeslot the timeslot to match to
     * @param person the person to match to
     */
    public void removeAppointment(Date date, Timeslot timeslot, Person person) {
        Appointment appointment = getAppointment(date, timeslot, person);
        if (appointment == null) return;
        appointments.remove(appointment);
    }

    /**
     * Gets appointment given the date, timeslot and patient attributes.
     *
     * @param date date to check for
     * @param timeslot timeslot to check for
     * @param person patient to check for
     * @return appointment found
     */
    public Appointment getAppointment(Date date, Timeslot timeslot,
                                      Person person) {
        for (Appointment appointment : appointments) {
            Profile profile = appointment.getPatient().getProfile();
            Profile check = person.getProfile();
            if (appointment.getDate().equals(date) &&
                    appointment.getTimeslot().equals(timeslot) &&
                    check.getFname().equalsIgnoreCase(profile.getFname()) &&
                    check.getLname().equalsIgnoreCase(profile.getLname()) &&
                    check.getDob().equals(profile.getDob())) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * Returns a list of appointments that correspond with the following
     * profile attribute tokens.
     *
     * @param fnameToken first name
     * @param lnameToken last name
     * @param dobToken   date of birth
     * @return list of appointments containing profile
     */
    public List<Appointment> getAppointments(
            String fnameToken, String lnameToken, String dobToken) {
        List<Appointment> found = new List<>();
        for (Appointment appointment : appointments) {
            if (appointment.getPatient()
                    .equals(fnameToken, lnameToken, dobToken)) {
                found.add(appointment);
            }
        }
        return found;
    }

    /**
     * Checks if appointment exists in the database given the attributes.
     *
     * @param date the date to match to
     * @param timeslot the timeslot to match to
     * @param person the person to match to
     * @return boolean if appointment exists
     */
    public boolean appointmentExists(Date date, Timeslot timeslot, Person person) {
        return getAppointment(date, timeslot, person) != null;
    }

    /**
     * Gets a list of appointments containing the provider mentioned.
     *
     * @param provider the provider to match appointments to
     * @return the list of appointments containing provider
     */
    public List<Appointment> getAppointments(Provider provider) {
        List<Appointment> found = new List<>();
        for (Appointment appointment : appointments) {
            if (appointment.getProvider().equals(provider)) {
                found.add(appointment);
            }
        }
        return found;
    }

    /**
     * Gets the full list of appointments.
     *
     * @return the full list of appointments
     */
    public List<Appointment> getAppointments() {
        List<Appointment> appointments = new List<>();
        for (Appointment appointment : this.appointments) {
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Sorts appointments through given custom comparator.
     *
     * @param comparator sorting methodology
     */
    public void sortAppointments(CustomComparator<Appointment> comparator) {
        Sort.bubbleSort(appointments, comparator);
    }

    /**
     * Clears active appointments from the database.
     */
    public void clearActiveAppointments() {
        final int end = appointments.size() - 1;
        for (int index = end; index >= 0; index--) {
            appointments.remove(appointments.get(index));
        }
    }
}

