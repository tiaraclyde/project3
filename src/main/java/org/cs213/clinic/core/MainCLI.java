package org.cs213.clinic.core;

/**
 * Main entry point for the command line interface version of the 
 * Clinic Management System. This class initializes the ClinicManager
 * and handles any command line arguments.
 */
public class MainCLI {
    
    /**
     * Main method to start the CLI version of the application.
     * @param args command line arguments. Use "--no-load" to start 
     *            without loading providers file.
     */
    public static void main(String[] args) {
        try {
            ClinicManager manager;
            
            if (args.length > 0 && args[0].equals("--no-load")) {
                // Initialize without loading providers file
                manager = new ClinicManager(true);
            } else {
                // Initialize and load providers file
                manager = new ClinicManager();
            }
            
            // Start the CLI interface
            manager.run();
            
        } catch (Exception e) {
            System.err.println("Error starting Clinic Manager: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}