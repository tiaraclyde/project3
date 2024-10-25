package main.org.cs213.clinic.cli.commands;

import main.org.cs213.clinic.core.Database;
import main.org.cs213.clinic.core.Date;
import main.org.cs213.clinic.core.Imaging;
import main.org.cs213.clinic.core.Patient;
import main.org.cs213.clinic.core.Provider;
import main.org.cs213.clinic.core.Radiology;
import main.org.cs213.clinic.core.Timeslot;

/**
 * The TechnicianScheduleCommand is responsible for scheduling new office
 * appointments with a technician with the requested date, time, and the
 * imaging service.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class TechnicianScheduleCommand implements Command {
    /**
     * The expected number of arguments for the Technician scheduling command.
     */
    public static final int ARGUMENT_COUNT = 6;

    /**
     * The index of the date argument.
     */
    public static final int DATE_INDEX = 0;

    /**
     * The index of the timeslot argument.
     */
    public static final int TIMESLOT_INDEX = 1;

    /**
     * The index of the patient's first name argument.
     */
    public static final int PT_FNAME_INDEX = 2;

    /**
     * The index of the patient's last name argument.
     */
    public static final int PT_LNAME_INDEX = 3;

    /**
     * The index of the patient's date of birth.
     */
    public static final int PT_DOB_INDEX = 4;

    /**
     * The index of the requested imaging service.
     */
    public static final int SERVICE_INDEX = 5;

    /**
     * Describes correct way to execute command.
     */
    public static final String USAGE_OUTSTR = "Usage: T,<MM/DD/YYYY>," +
            "<timeslot>,<patient first name>,<patient last name>," +
            "<patient date of birth>,<imaging service>";

    /**
     * Output format string for a successful scheduled appointment.
     */
    private static final String BOOKED_OUTSTR = "%s booked.\n";

    /**
     * Database of all the clinic's data.
     */
    private Database database;

    /**
     * Input validation object connected to database.
     */
    private InputValidation inputValidation;

    /**
     * Constructs a TechnicianScheduleCommand with a connection to the provided
     * database with all the clinic's data.
     *
     * @param database the database to connect to
     */
    public TechnicianScheduleCommand(Database database) {
        this.database = database;
        this.inputValidation = new InputValidation(database);
    }

    /**
     * The execute method is responsible for executing the technician
     * scheduling process. It validates the correctness of the input before
     * creating a scheduled appointment with a technician and a given service.
     *
     * @param args arguments to execute the command with
     * @return a status string of successful or unsuccessful execution
     */
    @Override
    public String execute(String[] args) {
        String error = validateInput(args);
        if (!error.isEmpty()) { return error; }
        Date scheduledDate = new Date(args[DATE_INDEX]);
        Timeslot timeslot = database.getTimeslot(args[TIMESLOT_INDEX]);
        Patient patient = database.getOrCreatePatient(
                args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                args[PT_DOB_INDEX]);
        Provider technician = database.getTechnician();
        database.nextTechnician();
        Radiology room = Radiology.valueOf(args[SERVICE_INDEX].toUpperCase());
        Imaging imaging = new Imaging(
                scheduledDate, timeslot,
                patient, technician, room);

        database.addAppointment(imaging);

        return String.format(BOOKED_OUTSTR, imaging);
    }

    /**
     * Executes series of procedures to validate the tokens received from
     * the CLI for an acceptable technician scheduling.
     *
     * @param args inputs received
     * @return any error encountered
     */
    private String validateInput(String[] args) {
        String error = inputValidation.forTokenCount(args,
                ARGUMENT_COUNT,USAGE_OUTSTR);
        if (!error.isEmpty()) { return error; }

        error = inputValidation.forScheduledDate(args[DATE_INDEX]);
        if (!error.isEmpty()) { return error; }

        error = inputValidation.forTimeslot(args[TIMESLOT_INDEX]);
        if (!error.isEmpty()) { return error; }

        error = inputValidation.forPatientInfo(args[PT_FNAME_INDEX],
                args[PT_LNAME_INDEX], args[PT_DOB_INDEX]);
        if (!error.isEmpty()) { return error; }

        error = inputValidation.forPatientAvailability(
                args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                args[PT_DOB_INDEX], args[DATE_INDEX], args[TIMESLOT_INDEX]);
        if (!error.isEmpty()) { return error; }

        //if the service exist
        error = inputValidation.forImagingService(args[SERVICE_INDEX]);
        if (!error.isEmpty()) { return error; }

        //service availability for booking
        return inputValidation.forTechnicianAvailability(args[SERVICE_INDEX],
                args[DATE_INDEX], args[TIMESLOT_INDEX]);
    }
}
