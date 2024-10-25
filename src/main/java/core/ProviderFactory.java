package core;

/**
 * This class constructs providers given tokens from any string source.
 * This is primarily used in the initialization of the clinic database in
 * ClinicManager.
 *
 * @see ClinicManager
 */
public class ProviderFactory {
    /**
     * Index of the provider type.
     */
    private static final int TYPE_INDEX = 0;

    /**
     * Index of the provider first name.
     */
    private static final int FNAME_INDEX = 1;

    /**
     * Index of provider last name.
     */
    private static final int LNAME_INDEX = 2;

    /**
     * Index of date of birth.
     */
    private static final int DOB_INDEX = 3;

    /**
     * Index of provider county.
     */
    private static final int COUNTY_INDEX = 4;

    /**
     * Index of provider specialty.
     */
    private static final int SPECIALTY_INDEX = 5;

    /**
     * Index for technician rate.
     */
    private static final int TECH_RATE_INDEX = 5;

    /**
     * Index of doctor National Provider Identification number.
     */
    private static final int NPI_INDEX = 6;

    /**
     * Creates a provider with their corresponding type given in the first
     * token. When types cannot be identified an exception is thrown.
     *
     * @param str the parameters to create the provider with
     * @param delim delimiter to tokenize the raw input with
     * @return the provider the was created
     */
    public static Provider createProvider(final String str, final String delim) {
        String[] args = str.split(delim);
        return switch (args[TYPE_INDEX]) {
            case "D" -> new Doctor(args[FNAME_INDEX], args[LNAME_INDEX],
                args[DOB_INDEX], args[COUNTY_INDEX], args[SPECIALTY_INDEX],
                args[NPI_INDEX]);
            case "T" -> new Technician(args[FNAME_INDEX], args[LNAME_INDEX],
                args[DOB_INDEX], args[COUNTY_INDEX], args[TECH_RATE_INDEX]);
            default -> throw new IllegalArgumentException("Provider not implemented");
        };
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private ProviderFactory() {}
}
