package core;

/**
 * The Provider abstract class serves as a contract for providers to define
 * their rates.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public abstract class Provider extends Person {
    /**
     * Location of the provider
     */
    private Location location;

    /**
     * Initializes the Provider object with the given profile.
     *
     * @param profile profile that contains attributes
     */
    public Provider(Profile profile) {
        super(profile);
    }

    /**
     * Get the charge rate for the provider.
     *
     * @return rate for provider
     */
    public abstract int rate();

    /**
     * Gets the location for the provider.
     *
     * @return the location of the provider
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location of the provider.
     *
     * @param location the location to set provider to
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}
