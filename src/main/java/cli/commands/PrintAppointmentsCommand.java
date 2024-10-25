package main.org.cs213.clinic.cli.commands;

import main.org.cs213.clinic.core.Appointment;
import main.org.cs213.clinic.core.Database;

/**
 * The PrintAppointmentsCommand handles the printing of an appointment.
 * Systematic output messages provide a list of appointments in a vertical
 * format. The list of appointments displayed are sorted by appointment date,
 * time, then provider.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */

public class PrintAppointmentsCommand implements Command {
    /**
     * Output string for empty calendar.
     */
    private static final String EMPTY_CALENDAR_OUTSTR = "Schedule calendar" +
        " is empty.\n";

    /**
     * Header for sorted list of appointments.
     */
    private static final String LIST_HEADER_OUTSTR = "** List of" +
        " appointments, ordered by date/time/provider.\n";

    /**
     * Closing footer for list.
     */
    private static final String LIST_FOOTER_OUTSTR = "** end of list **\n";

    /**
     * The database that contains the list of appointments.
     */
    private final Database database;

    /**
     * Initializes the PrintAppointmentsCommand with a centralized database
     * to generate string for the list of appointments.
     *
     * @param database all the clinic's data
     */
    public PrintAppointmentsCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the appointments list in the database to sort and print
     * all appointments by appointment date, time, and finally provider.
     *
     * @param args All arguments for execution (ignored)
     */
    @Override
    public String execute(String[] args) {
        if (database.getAppointments().isEmpty()) {
            return EMPTY_CALENDAR_OUTSTR;
        }

        database.sortAppointments((a, b) -> {
            int cmp = a.getDate().compareTo(b.getDate());
            if (cmp != 0) return cmp;
            cmp = a.getTimeslot().compareTo(b.getTimeslot());
            if (cmp != 0) return cmp;
            return a.getProvider().getProfile()
                    .compareTo(b.getProvider().getProfile());}
        );

        StringBuilder builder = new StringBuilder();
        for (Appointment appointment : database.getAppointments()) {
            builder.append(appointment.toString()).append("\n");
        }

        return LIST_HEADER_OUTSTR + builder + LIST_FOOTER_OUTSTR;
    }
}
