package cli.commands;

import core.Appointment;
import core.Database;
import core.Date;
import core.Doctor;
import core.Person;
import core.Profile;
import core.Timeslot;

/**
 * The RescheduleCommand class is a command to reschedule an appointment.
 * The command requires the following arguments: the date, timeslot,
 * patient's first name, last name, date of birth, and the new timeslot.
 * The command validates the input arguments and reschedules the appointment
 * if the input is valid. The command returns a message indicating the success
 * or failure of the rescheduling process.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class RescheduleCommand implements Command {
    /**
     * The expected number of arguments for the rescheduling command.
     */
    private static final int ARGUMENT_COUNT = 6;

    /**
     * The index of the appointment date argument.
     */
    private static final int DATE_INDEX = 0;

    /**
     * The index of the timeslot argument.
     */
    private static final int TIMESLOT_INDEX = 1;

    /**
     * The index of the patient's first name argument.
     */
    private static final int PT_FNAME_INDEX = 2;

    /**
     * The index of the patient's last name argument.
     */
    private static final int PT_LNAME_INDEX = 3;

    /**
     * The index of the patient's date of birth argument.
     */
    private static final int PT_DOB_INDEX = 4;

    /**
     * The index of the new timeslot argument.
     */
    private static final int NEW_TIMESLOT_INDEX = 5;

    /**
     * The usage message for the RescheduleCommand.
     */
    public static final String USAGE_OUTSTR = "Usage: R,<MM/DD/YYYY>," +
        "<timeslot>,<patient first name>,<patient last name>," +
        "<patient date of birth>,<new timeslot>\n";

    /**
     * The prefix used in the output message when an appointment is
     * successfully rescheduled.
     */
    public static final String RESCHEDULED_FORMAT = "Rescheduled to %s\n";

    /**
     * The centralized database that holds the list of appointments.
     */
    private final Database database;

    /**
     * The validator used to validate the rescheduling request.
     */
    private final InputValidation inputValidation;

    /**
     * Initializes the RescheduleCommand with a centralized database
     * with all the clinic's data.
     * 
     * @param database clinic's data to connect to
     */
    public RescheduleCommand(Database database) {
        this.database = database;
        this.inputValidation = new InputValidation(database);
    }

    /**
     * Executes the rescheduling command. This method first checks if the
     * arguments match the expected count. Then it validates the request and
     * attempts to reschedule the appointment.
     *
     * @param args the arguments needed to execute the command
     * @return the result of the rescheduling process
     */
    @Override
    public String execute(String[] args) {
        String error = validateInput(args);
        if (!error.isEmpty()) {
            return error;
        }

        Date date = new Date(args[DATE_INDEX]);
        Timeslot timeslot = database.getTimeslot(args[TIMESLOT_INDEX]);
        Profile profile = new Profile(args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                args[PT_DOB_INDEX]);
        Person person = new Person(profile);
        Appointment appointment = database.getAppointment(date, timeslot, person);
        database.removeAppointment(appointment);
        Timeslot newTimeslot = database.getTimeslot(args[NEW_TIMESLOT_INDEX]);
        appointment.setTimeslot(newTimeslot);
        database.addAppointment(appointment);
        return String.format(RESCHEDULED_FORMAT, appointment);
    }

    /**
     * Validates the input for rescheduling an appointment. The input includes
     * the appointment date, timeslot, patient information, and the new
     * timeslot. If any of the input is invalid, an error message is returned.
     * 
     * @param args tokens to process
     * @return An error message if the input is invalid, otherwise an empty
     * string
     */
    private String validateInput(String[] args) {
        final String customFormatForDNE = "%s %s %s does not exist.\n";
        Procedure[] procedures = {
                () -> inputValidation.forTokenCount(args, ARGUMENT_COUNT,
                        USAGE_OUTSTR),
                () -> inputValidation.forScheduleAndPatientInfo(
                    args[DATE_INDEX],
                    args[TIMESLOT_INDEX], args[PT_FNAME_INDEX],
                    args[PT_LNAME_INDEX], args[PT_DOB_INDEX]),
                () -> inputValidation.forAppointmentExists(args[DATE_INDEX],
                    args[TIMESLOT_INDEX], args[PT_FNAME_INDEX],
                    args[PT_LNAME_INDEX], args[PT_DOB_INDEX],
                    customFormatForDNE),
                () -> inputValidation.forTimeslot(args[NEW_TIMESLOT_INDEX]),
                () -> inputValidation.forPatientAvailabilityDetailed(
                    args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                    args[PT_DOB_INDEX], args[DATE_INDEX],
                    args[NEW_TIMESLOT_INDEX])
        };

        for (Procedure procedure : procedures) {
            String error = procedure.run();
            if (!error.isEmpty()) { return error; }
        }

        Date date = new Date(args[DATE_INDEX]);
        Timeslot timeslot = database.getTimeslot(args[TIMESLOT_INDEX]);
        Profile profile = new Profile(args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                args[PT_DOB_INDEX]);
        Person person = new Person(profile);
        Appointment appointment = database.getAppointment(date, timeslot,
                person);
        Doctor doctor = (Doctor) appointment.getProvider();
        return inputValidation.forDoctorAvailability(doctor.getNpi(),
                args[DATE_INDEX], args[NEW_TIMESLOT_INDEX]);
    }
}