package util;

/**
 * This CustomComparator interface is used to define the type of comparison
 * that happens between any two generic objects. This is primarily used in
 * the Sort class to implement different sorting algorithms.
 *
 * @param <T> the type of objects that this comparator can compare
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 * @see Sort
 */
public interface CustomComparator<T> {
    /**
     * This compare() method is responsible for evaluating two individual
     * elements by their respective properties for order.
     * 
     * @param a the first object to compare
     * @param b the second object to compare
     * @return a negative integer, zero, or a positive as the first argument
     *         is less than, equal to, or greater than the second respectively
     */
    int compare(T a, T b);
}
