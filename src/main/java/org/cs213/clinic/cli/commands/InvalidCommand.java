package org.cs213.clinic.cli.commands;

/**
 * This class represents a command that was not found in the registry. The
 * command could either be not supported by the CLI ClinicManager or was
 * misspelled by the user.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class InvalidCommand implements Command {
    /**
     * The message to be displayed when the command is not found.
     */
    public static final String INVALID_OUTSTR = "Invalid command!\n";

    /**
     * Initializes the InvalidCommand object.
     */
    public InvalidCommand() {}

    /**
     * This method returns a message indicating that the command was not found.
     * 
     * @param args the arguments passed to the command (ignored)
     * @return a message indicating that the command was not found
     */
    @Override
    public String execute(String[] args) {
        return INVALID_OUTSTR;
    }
}
