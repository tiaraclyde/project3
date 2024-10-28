package org.cs213.clinic.core;

/**
 * The Location enum class contains location of each clinic by their county and
 * zip code. The locations are used to determine the county and zip code of the
 * provider's clinic.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public enum Location {
    /** Bridgewater county address. */
    BRIDGEWATER("Somerset", "08807"),
    /** Edison county address. */
    EDISON("Middlesex", "08817"),
    /** Piscataway county address. */
    PISCATAWAY("Middlesex", "08854"),
    /** Princeton county address. */
    PRINCETON("Mercer", "08542"),
    /** Morristown county address. */
    MORRISTOWN("Morris", "07960"),
    /** Clark county address. */
    CLARK("Union", "07066");

    /** The county of where the clinic is located */
    private final String county;
    /** The zipcode of where the clinic is located */
    private final String zip;

    /**
     * A Location is constructed with a specific county and zip code.
     *
     * @param county The county that the clinic is located in
     * @param zip    The zip code that the clinic is located in
     */
    private Location(String county, String zip){
        this.county = county;
        this.zip = zip;
    }

    /**
     * Gets the county of where the clinic is located.
     * 
     * @return the county of the clinic
     */
    public String getCounty() {
        return county;
    }

    /**
     * Gets the zip code of where the clinic is located.
     *
     * @return zip the zip code of the clinic
     */
    public String getZip() {
        return zip;
    }

    /**
     * Provides the full address of a location as a string.
     *
     * @return full address
     */
    @Override
    public String toString() {
        final String fullAddressFormat = "%s, %s %s";
        return String.format(fullAddressFormat, this.name(), county, zip);
    }

    /**
     * Checks if a given string representation of a location exists as an
     * instance in the enum.
     *
     * @param location location string to search for
     * @return whether the instance of the location exists
     */
    public static boolean exists(String location) {
        for (Location loc : Location.values()) {
            if (loc.name().equalsIgnoreCase(location)) {
                return true;
            }
        }

        return false;
    }
}
