package main.org.cs213.clinic.cli.commands;

import main.org.cs213.clinic.core.Appointment;
import main.org.cs213.clinic.core.Database;

/**
 * The PrintByLocationCommand handles the printing of an appointments by their
 * location. The list of appointments displayed are sorted by the county name,
 * then the appointment date and time.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class PrintByLocationCommand implements Command {
    /**
     * Output string for empty calendar.
     */
    private static final String EMPTY_CALENDAR_OUTSTR = "Schedule calendar" +
        " is empty.\n";

    /**
     * Header for sorted list of appointments.
     */
    private static final String LIST_HEADER_OUTSTR = "** List of" +
        " appointments, ordered by county/date/time.\n";

    /**
     * Closing footer for list.
     */
    private static final String LIST_FOOTER_OUTSTR = "** end of list **\n";

    /**
     * The database that contains all the clinic's data.
     */
    private final Database database;

    /**
     * Initializes the PrintByLocationCommand with a centralized database with
     * the clinic's data.
     * 
     * @param database all the clinic's data to connect to
     */
    public PrintByLocationCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the appointments list in the database to print all
     * appointments by the ordering of list of appointments, sorted by the
     * county name, then the appointment date and time.
     *
     * @param args All arguments for execution (ignored)
     */
    @Override
    public String execute(String[] args) {
        if (database.getAppointments().isEmpty()) {
            return EMPTY_CALENDAR_OUTSTR;
        }

        database.sortAppointments((a, b) -> {
            int cmp = a.getProvider().getLocation().getCounty()
                    .compareTo(b.getProvider().getLocation().getCounty());
            if (cmp != 0) { return cmp; }
            cmp = a.getDate().compareTo(b.getDate());
            if (cmp != 0) { return cmp; }
            return a.getTimeslot().compareTo(b.getTimeslot());
        });

        StringBuilder builder = new StringBuilder();
        for (Appointment appointment : database.getAppointments()) {
            builder.append(appointment.toString()).append("\n");
        }

        return LIST_HEADER_OUTSTR + builder + LIST_FOOTER_OUTSTR;
    }
}
