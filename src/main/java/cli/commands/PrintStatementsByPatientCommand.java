package main.org.cs213.clinic.cli.commands;

import main.org.cs213.clinic.core.Appointment;
import main.org.cs213.clinic.core.Database;
import main.org.cs213.clinic.core.Patient;
import main.org.cs213.clinic.util.List;
import main.org.cs213.clinic.util.Sort;

/**
 * The StatementsByPatientCommand is the representation of a command to print
 * all billing statements by the patient ordering. There are no additional
 * arguments that can be passed. The command accesses the appointments list in
 * the database to print all appointments by the patient ordering.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class PrintStatementsByPatientCommand implements Command {
    /**
     * Status message for empty calendar.
     */
    private static final String EMPTY_CALENDAR_OUTSTR = "Schedule calendar is" +
        " empty.\n";

    /**
     * Header for sorted list of statements.
     */
    private static final String LIST_HEADER_OUTSTR = "** Billing statement" +
        " ordered by patient. **\n";

    /**
     * Closing footer for list.
     */
    private static final String LIST_FOOTER_OUTSTR = "** end of list **\n";;

    /**
     * The format string for each statement.
     */
    public static final String STATEMENT_FORMAT = "(%d) %s [due: $%,.2f]\n";

    /**
     * The database that contains the list of appointments.
     */
    private final Database database;

    /**
     * Initializes the PrintStatementsByPatientCommand with a centralized list
     * of appointments contained in a database to handle printing.
     * 
     * @param database clinic's database to connect to
     */
    public PrintStatementsByPatientCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the appointments list in the database to print and
     * mark them as visits and present statements by patient ordering.
     * The appointments list is cleared after the statements are printed.
     * 
     * @param args All arguments for execution (ignored)
     */
    @Override
    public String execute(String[] args) {
        List<Appointment> appointments = database.getAppointments();
        if (appointments.isEmpty()) { return EMPTY_CALENDAR_OUTSTR; }

        List<Patient> patients = database.getPatients();
        Sort.bubbleSort(patients);
        StringBuilder builder = new StringBuilder();
        // Finalize corresponding appointments to patient's visits
        for (Appointment appointment : appointments) {
            for (Patient patient : patients) {
                if (appointment.getPatient().equals(patient)) {
                    patient.addVisit(appointment);
                    break;
                }
            }
        }
        database.clearActiveAppointments();

        for (int index = 0; index < patients.size(); index++) {
            Patient patient = patients.get(index);
            final int id = index + 1;
            final double charge = patient.charge();
            builder.append(String.format(STATEMENT_FORMAT,
                    id, patient, charge));
        }

        return LIST_HEADER_OUTSTR + builder + LIST_FOOTER_OUTSTR;
    }
}
