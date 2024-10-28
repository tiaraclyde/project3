package org.cs213.clinic.cli.commands;

import org.cs213.clinic.core.Appointment;
import org.cs213.clinic.core.Database;
import org.cs213.clinic.core.Imaging;

/**
 * This class gathers all imaging appointments from a database and returns a
 * formatted string list ordered by county/date/time.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class PrintImagingAppointmentsCommand implements Command {
    /**
     * Status message for empty calendar.
     */
    private static final String EMPTY_CALENDAR_OUTSTR = "Schedule calendar is" +
        " empty.\n";

    /**
     * Header for sorted list of imaging appointments.
     */
    private static final String LIST_HEADER_OUTSTR = "** List of radiology" +
        " appointments ordered by county/date/time.\n";

    /**
     * Closing footer for list.
     */
    private static final String LIST_FOOTER_OUTSTR = "** end of list **\n";

    /**
     * The database that contains all data regarding clinic.
     */
    private final Database database;

    /**
     * Initializes the PrintImagingAppointmentsCommand with a centralized
     * database to handle data gathering from.
     *
     * @param database centralized database of all clinic data to connect to
     */
    public PrintImagingAppointmentsCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the appointments list in the database to print all
     * imaging appointments sorted by county, date and time.
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
            if (cmp != 0) return cmp;
            cmp = a.getDate().compareTo(b.getDate());
            if (cmp != 0) return cmp;
            return a.getTimeslot().compareTo(b.getTimeslot());
        });

        StringBuilder builder = new StringBuilder();
        for (Appointment appointment : database.getAppointments()) {
            if (appointment instanceof Imaging imaging) {
                builder.append(imaging).append("\n");
            }
        }

        return LIST_HEADER_OUTSTR + builder + LIST_FOOTER_OUTSTR;
    }
}
