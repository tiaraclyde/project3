package main.org.cs213.clinic.cli.registry;

import main.org.cs213.clinic.cli.commands.Command;
import main.org.cs213.clinic.cli.commands.InvalidCommand;

/**
 * The CommandRegistry acts as a registry for all the commands that the CLI
 * ClinicManager supports. It is responsible for mapping command tokens to their
 * respective Command classes.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class CommandRegistry {
    /**
     * Command object representing an invalid or unrecognized command.
     */
    private final Command invalidCommand = new InvalidCommand();

    /**
     * Array of CommandRegistryEntry objects that map command tokens to their
     * respective Command objects.
     */
    private final CommandRegistryEntry[] registryEntries;

    /**
     * This constructor initializes the registry with the given array of
     * CommandRegistryEntry objects. The user must provide the entries since
     * some may require external resources.
     * 
     * @param registryEntries the array of CommandRegistryEntry objects to
     *                        initialize
     * @see CommandRegistryEntry
     */
    public CommandRegistry(CommandRegistryEntry[] registryEntries) {
        this.registryEntries = registryEntries;
    }

    /**
     * This method retrieves the Command object associated with the given
     * command token. If the command token is not found, the InvalidCommand
     * object is returned.
     * 
     * @param commandToken the token linked to the command to retrieve
     * @return the Command object associated with the given command token, or the
     *         InvalidCommand object if the command token is not found
     */
    private Command getCommand(String commandToken) {
        for (CommandRegistryEntry entry : registryEntries) {
            // Command tokens are treated as case-sensitive
            if (entry.getToken().equals(commandToken)) {
                return entry.getCommand();
            }
        }
        return invalidCommand;
    }

    /**
     * This method retrieves the Command object associated with the given
     * command token and executes the command with the given arguments. If the
     * command token is not found, the InvalidCommand object is executed.
     * This method is useful for programmatically executing commands.
     * 
     * @param commandToken the token of the command to execute
     * @param args         the arguments to the command
     * @return the result of executing the command
     */
    public String executeCommand(String commandToken, String[] args) {
        Command command = getCommand(commandToken);
        return command.execute(args);
    }
}
