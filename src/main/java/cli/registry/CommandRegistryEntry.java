package cli.registry;

import cli.commands.Command;

/**
 * The CommandRegistryEntry class is a simple data structure that holds a token
 * and a command. The token is used to identify the command in the CLI, and the
 * Command is the object that provides the functionality of the command.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 * @see Command
 */
public class CommandRegistryEntry {
    /**
     * The token that represents the command.
     */
    private final String token;

    /**
     * The command that the token represents.
     */
    private final Command command;

    /**
     * This constructor initializes the token and command fields.
     * 
     * @param token   the token that represents the command
     * @param command the command that the token represents
     */
    public CommandRegistryEntry(String token, Command command) {
        this.token = token;
        this.command = command;
    }

    /**
     * This method returns the token that represents the command. The token is
     * used to identify the command in the CLI.
     * 
     * @return the token that represents the command
     */
    public String getToken() {
        return token;
    }

    /**
     * This method returns the command that the token represents. The command
     * is the object that provides the functionality of the command.
     * 
     * @return the command that the token represents
     */
    public Command getCommand() {
        return command;
    }

    /**
     * This method returns a string representation of the CommandRegistryEntry
     * object.
     * 
     * @return a string representation of the CommandRegistryEntry object
     */
    @Override
    public String toString() {
        return String.format("CommandRegistryEntry [token=%s, command=%s]",
                token, command);
    }
}
