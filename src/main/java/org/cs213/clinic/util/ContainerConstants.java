package org.cs213.clinic.util;

/**
 * This class is marked as final to prevent subclassing. This class defines
 * common constants that containers deal with.
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public final class ContainerConstants {
    /**
     * The initial capacity for containers.
     */
    public static final int INITIAL_CAPACITY = 4;

    /**
     * The initial size for containers.
     */
    public static final int INITIAL_SIZE = 0;

    /**
     * The growth factor for containers.
     */
    public static final int GROWTH_FACTOR = 2;

    /**
     * Ratio of size to capacity of when to do a resize.
     */
    public static final double LOAD_FACTOR = 0.75D;

    /**
     * The index when an element is not found.
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * Private constructor to prevent instantiation of this util class.
     */
    private ContainerConstants() {}
}
