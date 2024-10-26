package cli.commands;

import core.Appointment;
import core.Database;
import util.List;

/**
 * The PrintByPatientCommand is responsible for printing all appointments by
 * a patient's last name, first name, date of birth, then appointment date and
 * time.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class PrintByPatientCommand implements Command {
    /**
     * Status message for empty calendar.
     */
    private static final String EMPTY_CALENDAR_OUTSTR = "Schedule calendar" +
        " is empty.\n";

    /**
     * Header for sorted list of appointments.
     */
    private static final String LIST_HEADER_OUTSTR = "** Appointments" +
        " ordered by patient/date/time.\n";

    /**
     * Closing footer for list.
     */
    private static final String LIST_FOOTER_OUTSTR = "** end of list **\n";

    /**
     * The database that contains the clinic's data.
     */
    private final Database database;

    /**
     * Initializes the PrintByPatientCommand with a centralized database
     * of all the clinic's data.
     * 
     * @param database the clinic's data to connect to
     */
    public PrintByPatientCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the appointments list in the database to print all
     * appointments by the patient ordering (patient/date/time).
     * 
     * @param args All arguments for execution (ignored)
     */
    @Override
    public String execute(String[] args) {
        List<Appointment> appointments = database.getAppointments();
        if (appointments.isEmpty()) {
            return EMPTY_CALENDAR_OUTSTR;
        }

        database.sortAppointments((a, b) -> {
            int cmp = a.getPatient().getProfile()
                    .compareTo(b.getPatient().getProfile());
            if (cmp != 0) { return cmp; }
            cmp = a.getDate().compareTo(b.getDate());
            if (cmp != 0) { return cmp; }
            return a.getTimeslot().compareTo(b.getTimeslot());
        });

        StringBuilder builder = new StringBuilder();
        for (Appointment appointment : appointments) {
            builder.append(appointment.toString()).append("\n");
        }

        return LIST_HEADER_OUTSTR + builder + LIST_FOOTER_OUTSTR;
    }
}
