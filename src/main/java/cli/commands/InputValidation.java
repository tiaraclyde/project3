package main.org.cs213.clinic.cli.commands;

import main.org.cs213.clinic.core.Appointment;
import main.org.cs213.clinic.core.Database;
import main.org.cs213.clinic.core.Date;
import main.org.cs213.clinic.core.Person;
import main.org.cs213.clinic.core.Profile;
import main.org.cs213.clinic.core.Provider;
import main.org.cs213.clinic.core.Radiology;
import main.org.cs213.clinic.core.Technician;
import main.org.cs213.clinic.core.Timeslot;
import main.org.cs213.clinic.util.Format;
import main.org.cs213.clinic.util.List;

import static main.org.cs213.clinic.cli.commands.Command.EMPTY_OUTSTR;
import static main.org.cs213.clinic.cli.commands.Command.MISSING_TOKENS_OUTSTR;

/**
 * The InputValidation class validates tokens for numerous inputs that come
 * from general interfaces or programmatically. It connects to the clinic
 * centralized database to check if information is correct under any requested
 * check.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class InputValidation {
    /**
     * The prefix for the appointment date in the error message.
     */
    private static final String APPOINTMENT_DATE_PREFIX = "Appointment date: ";

    /**
     * The prefix for the patient date of birth in the error message.
     */
    private static final String PATIENT_DOB_PREFIX = "Patient dob: ";

    /**
     * The error format string for an invalid date format.
     */
    private static final String INVALID_DATE_FORMAT = "%s is not a valid" +
            " calendar date \n";

    /**
     * The error format string for a weekend date.
     */
    private static final String WEEKEND_DATE_FORMAT = "%s is Saturday or" +
            " Sunday.\n";

    /**
     * The error format string for a date today or before today.
     */
    private static final String DATE_TODAY_OR_BEFORE_FORMAT = "%s is today" +
        " or a date before today.\n";

    /**
     * The error format string for a date today or after today.
     */
    private static final String DATE_TODAY_OR_AFTER_FORMAT = "%s is today or" +
        " a date after today.\n";

    /**
     * The error format string for an invalid time slot format.
     */
    private static final String INVALID_TIMESLOT_FORMAT = "%s is not a valid" +
            " time slot.\n";

    /**
     * The error format string for an invalid provider format.
     */
    private static final String INVALID_PROVIDER_FORMAT = "%s - provider" +
            " doesn't exist.\n";

    /**
     * The error format string for an invalid service.
     */
    private static final String INVALID_SERVICE_FORMAT = "%s - imaging" +
        " service not provided.\n";

    /**
     * The error format string for a provider conflict.
     */
    private static final String PROVIDER_CONFLICT_FORMAT = "%s is not" +
        " available at slot %s\n";

    /**
     * The error format string for all technicians being busy.
     */
    private static final String TECHNICIAN_BUSY_FORMAT = "Cannot find an" +
            " available technician at all locations for %s at slot %s.\n";

    /**
     * The error format string for a patient conflict (old format).
     */
    private static final String PATIENT_CONFLICT_FORMAT = "%s has an" +
        " existing appointment at the same time slot.\n";

    /**
     * The error format string for a patient conflict (new format).
     */
    private static final String PATIENT_CONFLICT_FORMAT_DETAIL = "%s has an" +
        " existing appointment at %s %s\n";

    /**
     * The error format string for an appointment that does not exist.
     */
    private static final String DOES_NOT_EXIST_FORMAT = "%s %s %s -" +
        " appointment does not exist.\n";

    /**
     * The error format string for an invalid date range.
     */
    private static final String DATE_OUT_OF_RANGE_FORMAT = "%s is not" +
        " within %s months.\n";

    /**
     * Numbers represented in words for custom number of months.
     */
    private static final String[] NUM_OF_MONTHS = { "zero", "one", "two",
            "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve" };

    /**
     * Defines the scheduling window in months.
     */
    private static final int SCHEDULE_WINDOW_MONTHS = 6;

    /**
     * The database containing all appointment and patient records.
     */
    private final Database database;

    /**
     * Initializes the InputValidation with a centralized database of clinic
     * information.
     * 
     * @param database centralized database of clinic data
     */
    public InputValidation(Database database) {
        this.database = database;
    }

    /**
     * Checks a doctor's availability in their schedule given a requested
     * date and timeslot.
     *
     * @param npi NPI of doctor
     * @param dateToken date to check against
     * @param timeslotToken timeslot to check against
     * @return the status string indicating if there was a conflict
     */
    public String forDoctorAvailability(
            String npi, String dateToken, String timeslotToken) {
        Provider doctor = database.getDoctor(npi);
        List<Appointment> appointments = database.getAppointments(doctor);
        Date date = new Date(dateToken);
        Timeslot timeslot = database.getTimeslot(timeslotToken);
        for (Appointment appointment : appointments) {
            if (appointment.conflicts(date, timeslot)) {
                return String.format(PROVIDER_CONFLICT_FORMAT,
                        doctor, timeslotToken);
            }
        }
        return EMPTY_OUTSTR;
    }

    /**
     * Validates the technician schedule. The technician must exist and not
     * have an existing appointment at the same time slot. The technician's
     * information is validated against the appointment date and timeslot.
     * If the technician has an existing appointment at the same time slot,
     * an error message is returned.
     *
     * @param service the service that's being looked for
     * @param dateToken the date to check against
     * @param timeslotToken the timeslot to check against
     * @return a status message indicating if there were no techs available
     */
    public String forTechnicianAvailability(
            String service, String dateToken, String timeslotToken) {
        Timeslot timeslot = database.getTimeslot(timeslotToken);
        Date date = new Date(dateToken);
        for (Technician technician : database.getTechnicians()) {
            if (database.roomInUse(technician.getLocation(),
                    timeslot, Radiology.valueOf(service.toUpperCase()))) {
                database.nextTechnician();
                continue;
            }
            boolean foundConflict = false;
            for (Appointment appointment : database.getAppointments(technician)) {
                if (appointment.conflicts(date, timeslot)) {
                    foundConflict = true;
                    break;
                }
            }

            if (!foundConflict) { return EMPTY_OUTSTR; }

            database.nextTechnician();
        }

        return String.format(TECHNICIAN_BUSY_FORMAT,
            service.toUpperCase(), timeslotToken);
    }

    /**
     * Validates a patient's schedule allows the required date and timeslot to
     * be used. This overloaded method provides detailed feedback with the date
     * and timeslot conflict.
     *
     * @param fnameToken   the first name of the patient
     * @param lnameToken    the last name of the patient
     * @param dobToken      the date of birth of the patient
     * @param dateToken     the date of the appointment
     * @param timeslotToken the timeslot of the appointment
     * @return an error message if the patient information is invalid,
     *         otherwise an empty string
     */
    public String forPatientAvailabilityDetailed(
            String fnameToken, String lnameToken, String dobToken,
            String dateToken,
            String timeslotToken) {

        List<Appointment> appointments = database
                .getAppointments(fnameToken, lnameToken, dobToken);
        Date date = new Date(dateToken);
        Timeslot timeslot = database.getTimeslot(timeslotToken);
        for (Appointment appointment : appointments) {
            if (appointment.conflicts(date, timeslot)) {
                Person patient = appointment.getPatient();
                return String.format(PATIENT_CONFLICT_FORMAT_DETAIL,
                    patient, date, Format.get12Hour(timeslot));
            }
        }

        return EMPTY_OUTSTR;
    }

    /**
     * Validates a patient's schedule allows the required date and timeslot to
     * be used.
     *
     * @param fnameToken   the first name of the patient
     * @param lnameToken    the last name of the patient
     * @param dobToken      the date of birth of the patient
     * @param dateToken     the date of the appointment
     * @param timeslotToken the timeslot of the appointment
     * @return an error message if the patient information is invalid,
     *         otherwise an empty string
     */
    public String forPatientAvailability(
            String fnameToken, String lnameToken, String dobToken,
            String dateToken,
            String timeslotToken) {

        List<Appointment> appointments = database
                .getAppointments(fnameToken, lnameToken, dobToken);
        Date date = new Date(dateToken);
        Timeslot timeslot = database.getTimeslot(timeslotToken);
        for (Appointment appointment : appointments) {
            if (appointment.conflicts(date, timeslot)) {
                Person patient = appointment.getPatient();
                return String.format(PATIENT_CONFLICT_FORMAT, patient);
            }
        }

        return EMPTY_OUTSTR;
    }

    /**
     * Validates that the NPI token matches with a doctor in the database.
     *
     * @param npiToken doctor npi
     * @return error message if npi is invalid
     */
    public String forDoctorNPI(String npiToken) {
        if (!database.doctorExists(npiToken)) {
            return String.format(INVALID_PROVIDER_FORMAT, npiToken);
        }
        return EMPTY_OUTSTR;
    }

    /**
     * Checks the availability of the imaging service
     * @param service the type of service that is being requested
     * @return an empty string or if the service is not provided
     */
    public String forImagingService(String service) {
        if(!database.serviceExists(service)){
            return String.format(INVALID_SERVICE_FORMAT, service);
        }
        return EMPTY_OUTSTR;
    }
    /**
     * Validates the patient information. The patient's date of birth must be a
     * valid calendar date and not today or a date after today.
     *
     * @param fnameToken the first name of the patient.
     * @param lnameToken  the last name of the patient.
     * @param dobToken    the date of birth of the patient.
     * @return an error message if the patient information is invalid,
     * otherwise an empty string
     */
    public String forPatientInfo(String fnameToken, String lnameToken,
                                 String dobToken) {
        Date dob = Date.parseDate(dobToken);
        if (!dob.isValid()) {
            return String.format(PATIENT_DOB_PREFIX + INVALID_DATE_FORMAT,
                dobToken);
        }
        if (dob.compareTo(Date.today()) >= 0) {
            return String.format(PATIENT_DOB_PREFIX +
                DATE_TODAY_OR_AFTER_FORMAT, dobToken);
        }
        return EMPTY_OUTSTR;
    }

    /**
     * Validates the timeslot. The timeslot must be a valid time slot. The
     * character must be a digit between {@link Timeslot#START_ID} and the
     * number of timeslots in the database. If the timeslot is invalid,
     * an error message is returned.
     *
     * @param timeslotToken the timeslot of the appointment
     * @return an error message if the timeslot is invalid, otherwise an empty
     *         string
     */
    public String forTimeslot(String timeslotToken) {
        int timeslot;
        try {
            timeslot = Integer.parseInt(timeslotToken);
        } catch (NumberFormatException e) {
            return String.format(INVALID_TIMESLOT_FORMAT, timeslotToken);
        }

        if (!database.timeslotExists(timeslot)) {
            return String.format(INVALID_TIMESLOT_FORMAT, timeslotToken);
        }
        return EMPTY_OUTSTR;
    }

    /**
     * Validates the appointment date. The date must be a valid calendar date,
     * not a weekend, not today or a date before today, and within the next
     * {@link InputValidation#SCHEDULE_WINDOW_MONTHS} months.
     *
     * @param dateToken the date of the appointment
     * @return an error message if the date is invalid, otherwise an empty
     *         string
     */
    public String forScheduledDate(String dateToken) {
        Date date = Date.parseDate(dateToken);
        if (!date.isValid()) {
            return String.format(APPOINTMENT_DATE_PREFIX +
                    INVALID_DATE_FORMAT, dateToken);
        }
        if (date.compareTo(Date.today()) <= 0) {
            return String.format(APPOINTMENT_DATE_PREFIX +
                    DATE_TODAY_OR_BEFORE_FORMAT, dateToken);
        }
        if (date.isWeekend()) {
            return String.format(APPOINTMENT_DATE_PREFIX +
                    WEEKEND_DATE_FORMAT, dateToken);
        }
        Date monthsLater = Date.today().addMonths(SCHEDULE_WINDOW_MONTHS);
        if (date.compareTo(monthsLater) > 0) {
            return APPOINTMENT_DATE_PREFIX +
                String.format(DATE_OUT_OF_RANGE_FORMAT,
                    dateToken, NUM_OF_MONTHS[SCHEDULE_WINDOW_MONTHS]);
        }
        return EMPTY_OUTSTR;
    }

    /**
     * Validates the info for scheduling an appointment and the patient
     * information.
     * 
     * @param dateToken     the date of the appointment
     * @param timeslotToken the timeslot of the appointment
     * @param fnameToken   the first name of the patient
     * @param lnameToken    the last name of the patient
     * @param dobToken      the date of birth of the patient
     * @return an error message if the input is invalid, otherwise an empty
     *         string
     */
    public String forScheduleAndPatientInfo(
            String dateToken, String timeslotToken,
            String fnameToken, String lnameToken, String dobToken) {
        String error = forScheduledDate(dateToken);
        if (!error.isEmpty())
            return error;

        error = forTimeslot(timeslotToken);
        if (!error.isEmpty())
            return error;

        error = forPatientInfo(fnameToken, lnameToken, dobToken);
        if (!error.isEmpty())
            return error;

        return EMPTY_OUTSTR;
    }

    /**
     * Verifies the number of tokens in a String array matches the expected
     * count. If the count is less than expected this method communicates
     * that more tokens are needed, otherwise, if there's more than needed,
     * a usage output string is given.
     *
     * @param tokens the tokens
     * @param expectedCount how many there should be
     * @param usage usage string in case of other condition
     * @return the error that occurred
     */
    public String forTokenCount(String[] tokens,
                                int expectedCount, String usage) {
        if (tokens.length < expectedCount) {
            return MISSING_TOKENS_OUTSTR;
        }

        if (tokens.length != expectedCount) {
            return usage;
        }

        return EMPTY_OUTSTR;
    }

    /**
     * Validates the appointment exists. The appointment must exist in the
     * database. The patient's information is validated against the appointment
     * date and timeslot. If the patient has an existing appointment at the
     * same time slot, then the validation succeeds.
     * 
     * @param fnameToken   the first name of the patient
     * @param lnameToken    the last name of the patient
     * @param dobToken      the date of birth of the patient
     * @param dateToken     the date of the appointment
     * @param timeslotToken the timeslot of the appointment
     * @return an error message if the appointment does not exist, otherwise an
     *         empty string
     */
    public String forAppointmentExists(
            String dateToken,
            String timeslotToken,
            String fnameToken, String lnameToken, String dobToken) {
        return forAppointmentExists(dateToken, timeslotToken, fnameToken,
                lnameToken, dobToken, DOES_NOT_EXIST_FORMAT);
    }

    /**
     * Validates the appointment exists. The appointment must exist in the
     * database. The patient's information is validated against the appointment
     * date and timeslot. If the patient has an existing appointment at the
     * same time slot, then the validation succeeds. This method allows the
     * modification of the format string returned.
     *
     * @param fnameToken   the first name of the patient
     * @param lnameToken    the last name of the patient
     * @param dobToken      the date of birth of the patient
     * @param dateToken     the date of the appointment
     * @param timeslotToken the timeslot of the appointment
     * @param format        string to use when formatting error
     * @return an error message if the appointment does not exist, otherwise an
     *         empty string
     */
    public String forAppointmentExists(
            String dateToken, String timeslotToken,
            String fnameToken, String lnameToken, String dobToken,
            String format) {
        Date date = Date.parseDate(dateToken);
        Timeslot timeslot = database.getTimeslot(timeslotToken);
        Profile profile = new Profile(fnameToken, lnameToken, dobToken);
        Person person = new Person(profile);
        if (!database.appointmentExists(date, timeslot, person)) {
            return String.format(format,
                    dateToken, Format.get12Hour(timeslot), person);
        }
        return EMPTY_OUTSTR;
    }
}
