package org.cs213.clinic.cli.commands;

import org.cs213.clinic.core.Appointment;
import org.cs213.clinic.core.Database;
import org.cs213.clinic.core.Date;
import org.cs213.clinic.core.Patient;
import org.cs213.clinic.core.Provider;
import org.cs213.clinic.core.Timeslot;

/**
 * The DoctorScheduleCommand is responsible for scheduling new office
 * appointments with a doctor at the requested date, time, and the provided
 * National Provider Identification (NPI) number.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class DoctorScheduleCommand implements Command {
    /**
     * The expected number of arguments for the Doctor scheduling command.
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
     * The index of the doctor's NPI.
     */
    public static final int NPI_INDEX = 5;

    /**
     * Output string to indicate the correct usage of the
     * DoctorScheduleCommand.
     */
    private static final String USAGE_OUTSTR = "Usage: D,<MM/DD/YYYY>," +
        "<timeslot>,<patient first name>,<patient last name>," +
        "<patient date of birth>,<doctor NPI>\n";

    /**
     * Output string for a successful scheduling.
     */
    private static final String BOOKED_OUTSTR = "%s booked.\n";

    /**
     * Reference to all clinic data.
     */
    private final Database database;

    /**
     * Input validation object connected to database.
     */
    private final InputValidation inputValidation;

    /**
     * Constructs a DoctorScheduleCommand with a connection to the provided
     * database.
     *
     * @param database the database to connect to
     */
    public DoctorScheduleCommand(Database database) {
        this.database = database;
        this.inputValidation = new InputValidation(database);
    }

    /**
     * The execute method is responsible for executing the doctor scheduling
     * process. It validates the correctness of the input before creating a
     * scheduled appointment.
     *
     * @param args arguments to execute the command with
     * @return a status string of successful or unsuccessful execution
     */
    @Override
    public String execute(String[] args) {
        String err = validateInput(args);
        if (!err.isEmpty()) { return err; }

        Date scheduledDate = new Date(args[DATE_INDEX]);
        Timeslot timeslot = database.getTimeslot(args[TIMESLOT_INDEX]);
        Patient patient = database.getOrCreatePatient(
                args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                args[PT_DOB_INDEX]);
        Provider doctor = database.getDoctor(args[NPI_INDEX]);
        Appointment appointment = new Appointment(
                scheduledDate, timeslot,
                patient, doctor);

        database.addAppointment(appointment);

        return String.format(BOOKED_OUTSTR, appointment);
    }

    /**
     * Checks if the input for the Doctor schedule command is valid
     * @param args the needed arguments to validate the input
     * @return if the output is invalid it will return error
     */
    private String validateInput(String[] args) {
        Procedure[] procedures = {
                () -> inputValidation.forTokenCount(args,
                        ARGUMENT_COUNT,USAGE_OUTSTR),
                () -> inputValidation.forScheduledDate(args[DATE_INDEX]),
                () -> inputValidation.forTimeslot(args[TIMESLOT_INDEX]),
                () -> inputValidation.forPatientInfo(args[PT_FNAME_INDEX],
                        args[PT_LNAME_INDEX], args[PT_DOB_INDEX]),
                () -> inputValidation.forPatientAvailability(
                        args[PT_FNAME_INDEX], args[PT_LNAME_INDEX],
                        args[PT_DOB_INDEX], args[DATE_INDEX],
                        args[TIMESLOT_INDEX]),
                () -> inputValidation.forDoctorNPI(args[NPI_INDEX]),
                () -> inputValidation.forDoctorAvailability(args[NPI_INDEX],
                        args[DATE_INDEX], args[TIMESLOT_INDEX])
        };

        for (Procedure procedure : procedures) {
            String error = procedure.run();
            if (!error.isEmpty()) { return error; }
        }

        return EMPTY_OUTSTR;
    }
}
