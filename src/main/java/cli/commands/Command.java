package cli.commands;

/**
 * The Command interface defines the contract for all commands that the CLI
 * ClinicManager supports. Each command must implement this interface and
 * override the execute() method to provide the functionality of their given
 * command. Commands are responsible for overriding methods from other classes
 * since interfaces cannot override methods.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public interface Command {
    /**
     * Output string for no content.
     */
    String EMPTY_OUTSTR = "";

    /**
     * Output string for an unimplemented command.
     */
    String UNIMPLEMENTED_OUTSTR = "This command has not been implemented" +
        " yet.\n";

    /**
     * Output string to indicate missing tokens.
     */
    String MISSING_TOKENS_OUTSTR = "Missing data tokens.\n";

    /**
     * This default implementation of the execute() method returns an output
     * string indicating that the command has not been implemented yet. This
     * method should be overridden by each subclass to provide the
     * functionality of their given command.
     * 
     * @param args the arguments to the command (excluding the command name)
     * @return a message indicating that the command has not been implemented
     * yet
     */
    default String execute(String[] args) {
        return UNIMPLEMENTED_OUTSTR;
    }
}
