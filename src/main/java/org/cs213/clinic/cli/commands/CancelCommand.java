package org.cs213.clinic.cli.commands;

import org.cs213.clinic.core.Database;
import org.cs213.clinic.core.Date;
import org.cs213.clinic.core.Person;
import org.cs213.clinic.core.Profile;
import org.cs213.clinic.core.Timeslot;
import org.cs213.clinic.util.Format;

/**
 * The CancelCommand class implements the Command interface to interface with
 * CLI handled by the ClinicManager class. This class handles the cancellation
 * of appointment's as long as the information inputted is valid. It
 * incorporates methods for checking the validity and implements the
 * cancellation process itself.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class CancelCommand implements Command {
    /**
     * The number of arguments required for canceling an appointment.
     */
    private static final int ARGUMENT_COUNT = 5;

    /**
     * The index in the arguments array for the appointment date.
     */
    private static final int DATE_INDEX = 0;

    /**
     * The index in the arguments array for the appointment timeslot.
     */
    private static final int TIMESLOT_INDEX = 1;

    /**
     * The index in the arguments array for the patient's first name.
     */
    private static final int PT_FNAME_INDEX = 2;

    /**
     * The index in the arguments array for the patient's last name.
     */
    private static final int PT_LNAME_INDEX = 3;

    /**
     * The index in the arguments array for the patient's date of birth.
     */
    private static final int PT_DOB_INDEX = 4;

    /**
     * Output string to indicate the correct usage of the CancelCommand.
     */
    private static final String USAGE_OUTSTR = "Usage: C,<MM/DD/YYYY>," +
        "<timeslot>,<patient first name>,<patient last name>," +
        "<patient date of birth>";

    /**
     * Output format string when an appointment is canceled.
     */
    public static final String CANCELED_FORMAT = "%s %s %s - appointment has" +
            " been canceled.\n";

    /**
     * The database containing all the clinic data.
     */
    private final Database database;

    /**
     * Validator for checking the validity of input.
     */
    private final InputValidation inputValidation;

    /**
     * Initializes the CancelCommand with a centralized database to manage
     * cancellation.
     *
     * @param database where all clinic data is held
     */
    public CancelCommand(Database database) {
        this.database = database;
        this.inputValidation = new InputValidation(database);
    }

    /**
     * This method handles the canceling of an appointment. It expects the
     * following arguments: the date, timeslot, patient's first name,
     * last name, and date of birth. Systematic output messages are provided to
     * indicate the success or failure of the canceling process.
     *
     * @param args the date, timeslot, patient's first name, last name,
     *             and date of birth
     * @return a message indicating the success or failure of the canceling
     *         process
     */
    @Override
    public String execute(String[] args) {
        String validationError = validateInput(args);
        if (!validationError.isEmpty()) {
            return validationError;
        }

        Date date = new Date(args[DATE_INDEX]);
        Timeslot timeslot = database.getTimeslot(args[TIMESLOT_INDEX]);
        Profile profile = new Profile(args[PT_FNAME_INDEX],
                args[PT_LNAME_INDEX], args[PT_DOB_INDEX]);
        Person person = new Person(profile);
        database.removeAppointment(date, timeslot, person);
        return String.format(CANCELED_FORMAT,
                date, Format.get12Hour(timeslot), person);
    }

    /**
     * Validates the input for canceling an appointment. If any of the input is
     * invalid, an error message is returned. Otherwise, an empty string is
     * returned.
     * 
     * @param args tokens from the terminal to use in cancel request
     * @return an error message if the input is invalid, otherwise an empty
     * string
     */
    private String validateInput(String[] args) {
        String error = inputValidation.forTokenCount(args, ARGUMENT_COUNT,
            USAGE_OUTSTR);
        if (!error.isEmpty()) { return error; }

        // Validate schedule info
        error = inputValidation.forScheduleAndPatientInfo(args[DATE_INDEX],
                args[TIMESLOT_INDEX], args[PT_FNAME_INDEX],
                args[PT_LNAME_INDEX], args[PT_DOB_INDEX]);
        if (!error.isEmpty()) { return error; }

        // Validate appointment exists
        return inputValidation.forAppointmentExists(args[DATE_INDEX],
                args[TIMESLOT_INDEX], args[PT_FNAME_INDEX],
                args[PT_LNAME_INDEX], args[PT_DOB_INDEX]);
    }
}
