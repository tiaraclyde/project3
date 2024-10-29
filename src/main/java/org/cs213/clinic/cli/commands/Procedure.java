package org.cs213.clinic.cli.commands;

/**
 * This class is useful for running a number of methods in consecutive order.
 * Methods are provided with different return types to make this interface
 * compatible with any possible implementations.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public interface Procedure {
    /**
     * The implementation of the procedures that needs to be run with a string
     * return type.
     *
     * @return any output string
     */
    String run();
}
