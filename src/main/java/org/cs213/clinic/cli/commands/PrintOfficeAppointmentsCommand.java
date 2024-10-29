package org.cs213.clinic.cli.commands;

import org.cs213.clinic.core.Appointment;
import org.cs213.clinic.core.Database;
import org.cs213.clinic.core.Imaging;

/**
 * The PrintByOfficeAppointmentsCommand handles the printing of a list of
 * appointments. Systematic output messages provide a list of appointments
 * in a vertical format. The list of appointments displayed are sorted by
 * county name, appointment date, then appointment time
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */

public class PrintOfficeAppointmentsCommand implements Command {
    /**
     * Output string for when the calander is empty.
     */
    private static final String EMPTY_CALENDER_OUTSTR =
        "Schedule calendar is empty.\n";

    /**
     * Header for the list of appointments
     */
    private static final String HEADER_OUTSTR =
        "** List of office appointments ordered by county/date/time.\n";

    /**
     * Footer for this list of appointments
     */
    private static final String FOOTER_OUTSTR =
        "** end of list **\n";

    /**
     * The database that contains all the appointments.
     */
    private final Database database;

    /**
     * Initializes the PrintOfficeAppointmentsCommand with a centralized
     * database to generate string for the list of appointments.
     *
     * @param database all the clinic's data
     */
    public PrintOfficeAppointmentsCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the appointment list in the database in order to print
     * the office appointments ordered by county, date, and time.
     *
     * @param args all inputs from the CLI
     */
    @Override
    public String execute(String[] args) {
        if (database.getAppointments().isEmpty()) {
            return EMPTY_CALENDER_OUTSTR;
        }

        database.sortAppointments((a, b) -> {
            int compare = a.getProvider().getLocation().getCounty()
                .compareTo(b.getProvider().getLocation().getCounty());
            if (compare != 0)
                return compare;
            compare = a.getDate().compareTo(b.getDate());
            if (compare != 0)
                return compare;
            return a.getTimeslot().compareTo(b.getTimeslot());
        });
        StringBuilder builder = new StringBuilder();
        for (Appointment appointment : database.getAppointments()) {
            if (!(appointment instanceof Imaging imaging)) {
                builder.append(appointment).append("\n");
            }
        }
        return HEADER_OUTSTR + builder + FOOTER_OUTSTR;
    }
}
