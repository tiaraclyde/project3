package org.cs213.clinic.core;

/**
 * The Specialty enum class contains the charge/cost of each of specialties
 * that the providers offer. The specialties are used to calculate the cost of
 * a visit based on the provider's specialty. The cost of a visit is determined
 * by the specialty of the provider.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public enum Specialty {
    /** The cost of a visit for a general practitioner. */
    FAMILY(250),
    /** The cost of a visit for a pediatrician. */
    PEDIATRICIAN(300),
    /** The cost of a visit for an allergist. */
    ALLERGIST(350);

    /** The charge of the specialty per visit. */
    private final int charge;

    /**
     * A Specialty is constructed with a specific costs per visit
     * depending on the specialty. The cost of a visit is determined
     * by the specialty of the provider.
     *
     * @param charge The cost of that specific specialty
     */
    private Specialty(int charge) {
        this.charge = charge;
    }

    /**
     * Returns the charge of the specialty per visit
     *
     * @return the charge of the specialty per visit
     */
    public int getCharge() {
        return charge;
    }

    /**
     * Checks if a string representation of a specialty exists as an instance
     * in the enum class.
     *
     * @param name the name of the specialty as a string
     * @return if the specialty exists as an instance
     */
    public static boolean exists(String name) {
        for (Specialty s : Specialty.values()) {
            if (s.name().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }
}
