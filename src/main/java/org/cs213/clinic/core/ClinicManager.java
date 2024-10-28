package org.cs213.clinic.core;

import org.cs213.clinic.cli.commands.CancelCommand;
import org.cs213.clinic.cli.commands.DoctorScheduleCommand;
import org.cs213.clinic.cli.commands.PrintAppointmentsCommand;
import org.cs213.clinic.cli.commands.PrintByLocationCommand;
import org.cs213.clinic.cli.commands.PrintByPatientCommand;
import org.cs213.clinic.cli.commands.PrintCreditByProviderCommand;
import org.cs213.clinic.cli.commands.PrintImagingAppointmentsCommand;
import org.cs213.clinic.cli.commands.PrintOfficeAppointmentsCommand;
import org.cs213.clinic.cli.commands.PrintStatementsByPatientCommand;
import org.cs213.clinic.cli.commands.RescheduleCommand;
import org.cs213.clinic.cli.commands.TechnicianScheduleCommand;
import org.cs213.clinic.cli.registry.CommandRegistry;
import org.cs213.clinic.cli.registry.CommandRegistryEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * This is the user interface class that will be used to manage appointments.
 * Users will be able to manage appointments through a single or multiple
 * command line inputs, including empty inputs. When the user is done, they can
 * exit the program by typing "Q".
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class ClinicManager {
    /**
     * The header message to display when the ClinicManager is running.
     */
    private static final String INIT_HEADER = "Clinic Manager is running...";

    /**
     * The header message for the providers that were loaded.
     */
    private static final String PROVIDERS_LOADED_HEADER = "Providers loaded" +
        " to the list.";

    /**
     * The header message for the rotation of technicians.
     */
    private static final String ROTATION_LIST_HEADER = "Rotation list for the" +
        " technicians.";

    /**
     * The footer message to display when the ClinicManager is terminated.
     */
    private static final String QUIT_FOOTER = "Clinic Manager terminated.";

    /**
     * Directory where files are contained.
     */
    private static final String DIR = "input/";

    /**
     * Name of file that contains providers.
     */
    private static final String PROVIDERS_TXT = "providers.txt";

    /**
     * Delimiter for reading from files.
     */
    private static final String FILE_DELIM = "  ";

    /**
     * Delimiter for reading from command line.
     */
    private static final String CLI_DELIM = ",";

    /**
     * The CommandRegistry object that will be used to manage programmatic
     * execution of commands.
     */
    private final CommandRegistry commandRegistry;

    /**
     * The Database object that will be used to manage the medical record and
     * appointments.
     */
    private final Database database;

    /**
     * This constructor initializes a user interface for the user to manage
     * their appointments. The ClinicManager is responsible for processing the
     * inputs from the user. The constructor initializes a CommandRegistry
     * object to manage programmatic execution of commands. Commands are
     * defined within the ClinicManager as an array of CommandRegistryEntry
     * objects.
     *
     * @throws FileNotFoundException if the providers.txt file can't be found
     * @see CommandRegistry
     * @see CommandRegistryEntry
     */
    public ClinicManager() throws FileNotFoundException {
        database = new Database();
        final int[] timeslotsBegin = {9, 14};
        final int numOfTimeslots = 6, slotDurationMinutes = 30;
        for (int startHour : timeslotsBegin) {
            int minutes = 0;
            for (int slot = 0; slot < numOfTimeslots; slot++) {
                Timeslot timeslot = new Timeslot(startHour, minutes,
                    0, slotDurationMinutes * slot);
                database.addTimeslot(timeslot);
            }
        }

        Scanner fileStream = new Scanner(new File(DIR + PROVIDERS_TXT));
        while (fileStream.hasNextLine()) {
            String tokens = fileStream.nextLine();
            Provider provider = ProviderFactory.createProvider(tokens, FILE_DELIM);
            database.addProvider(provider);
        }
        System.out.println(PROVIDERS_LOADED_HEADER);
        System.out.println(database.getProvidersAsString());

        System.out.println(ROTATION_LIST_HEADER);
        System.out.println(database.getRotationAsString());

        commandRegistry = getDefaultCommandRegistry(database);
    }

    /**
     * Create a command registry with all default commands mentioned in the
     * project 2 description.
     *
     * @param database database to tie commands to
     * @return the registry with all commands
     */
    private static CommandRegistry getDefaultCommandRegistry(Database database) {
        CommandRegistryEntry[] registryEntries = {
            new CommandRegistryEntry("D", new DoctorScheduleCommand(database)),
            new CommandRegistryEntry("T", new TechnicianScheduleCommand(database)),
            new CommandRegistryEntry("C", new CancelCommand(database)),
            new CommandRegistryEntry("R", new RescheduleCommand(database)),
            new CommandRegistryEntry("PA", new PrintAppointmentsCommand(database)),
            new CommandRegistryEntry("PP", new PrintByPatientCommand(database)),
            new CommandRegistryEntry("PL", new PrintByLocationCommand(database)),
            new CommandRegistryEntry("PS", new PrintStatementsByPatientCommand(database)),
            new CommandRegistryEntry("PI", new PrintImagingAppointmentsCommand(database)),
            new CommandRegistryEntry("PC", new PrintCreditByProviderCommand(database)),
            new CommandRegistryEntry("PO", new PrintOfficeAppointmentsCommand(database))
        };
        return new CommandRegistry(registryEntries);
    }

    /**
     * This method processes multiple command line inputs from the user. The
     * user can input multiple commands with their arguments separated with
     * commas. The method will process each command in the order they were
     * inputted. Empty inputs are ignored. The method will continue
     * to process inputs until the user types "Q" to quit the program. A loop
     * is continually ran to process inputs from the user. Scanner is used to
     * read inputs from the user. The CommandRegistry is used to
     * programmatically execute commands for the user.
     *
     * @see CommandRegistry
     */
    public void run() {
        System.out.println(INIT_HEADER);

        final Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine();

            if (input.isEmpty()) { // Ignore empty inputs
                continue;
            }

            if (input.equals("Q")) {
                break;
            }

            StringTokenizer tokenizer = new StringTokenizer(input, CLI_DELIM);
            // We will always have at least one token; empty inputs are ignored
            String commandToken = tokenizer.nextToken();
            // Command classes are responsible for handling size 0 args so
            // we don't need to check for them
            String[] args = new String[tokenizer.countTokens()];
            for (int index = 0; index < args.length; index++) {
                args[index] = tokenizer.nextToken();
            }

            String outstr = commandRegistry.executeCommand(commandToken, args);
            System.out.print(outstr);
        }

        scanner.close();
        System.out.println(QUIT_FOOTER);
    }

    /**
     * Gets the command registry from the clinic manager.
     *
     * @return command registry
     */
    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    /**
     * Gets the database from the clinic manager.
     *
     * @return database used by other commands
     */
    public Database getDatabase() {
        return database;
    }
}
