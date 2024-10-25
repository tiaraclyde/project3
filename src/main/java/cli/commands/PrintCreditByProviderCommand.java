package main.org.cs213.clinic.cli.commands;

import main.org.cs213.clinic.core.Appointment;
import main.org.cs213.clinic.core.Database;
import main.org.cs213.clinic.core.Person;
import main.org.cs213.clinic.core.Provider;
import main.org.cs213.clinic.util.List;
import main.org.cs213.clinic.util.Sort;

/**
 * This class is responsible for gathering the credit amount per provider
 * from seeing patients and returning each credit amount in a string list
 * ordered by provider.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class PrintCreditByProviderCommand implements Command {
    /**
     * Status message for empty calendar.
     */
    private static final String EMPTY_CALENDAR_OUTSTR = "Schedule calendar" +
        " is empty.\n";

    /**
     * Header for sorted list of providers and their credits.
     */
    private static final String LIST_HEADER_OUTSTR = "** Credit amount" +
        " ordered by provider. **\n";

    /**
     * Closing footer for list.
     */
    private static final String LIST_FOOTER_OUTSTR = "** end of list **\n";

    /**
     * The format string for each statement.
     */
    public static final String CREDIT_FORMAT = "(%d) %s [credit amount:" +
        " $%,.2f]\n";

    /**
     * The database that contains the clinic's data.
     */
    private final Database database;

    /**
     * Initializes the PrintCreditByProviderCommand with a given database
     * to retrieve data from.
     *
     * @param database the database with the clinic's data to connect to
     */
    public PrintCreditByProviderCommand(Database database) {
        this.database = database;
    }

    /**
     * This accesses the data in the database to return a string of all
     * credit gained per provider and ordered by provider.
     *
     * @param args All arguments for execution (ignored)
     */
    @Override
    public String execute(String[] args) {
        List<Appointment> appointments = database.getAppointments();
        if (appointments.isEmpty()) { return EMPTY_CALENDAR_OUTSTR; }

        List<Provider> providers = database.getProviders();
        Sort.bubbleSort(providers, Person::compareTo);
        int[] numOfPatients = new int[providers.size()];
        StringBuilder builder = new StringBuilder();
        for (Appointment appointment : appointments) {
            for (int index = 0; index < numOfPatients.length; index++) {
                Provider provider = providers.get(index);
                if (appointment.getProvider().equals(provider)) {
                    numOfPatients[index]++;
                    break;
                }
            }
        }

        for (int index = 0; index < providers.size(); index++) {
            Provider provider = providers.get(index);
            final int id = index + 1;
            final double credit = provider.rate() * numOfPatients[index];
            builder.append(String.format(CREDIT_FORMAT,
                id, provider.getProfile(), credit));
        }

        return LIST_HEADER_OUTSTR + builder + LIST_FOOTER_OUTSTR;
    }
}
