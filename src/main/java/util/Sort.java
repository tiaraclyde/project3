package util;

/**
 * This Sort class defines common sorting algorithms and furthermore common
 * actions when it comes to implementing custom sorting solutions.
 * 
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class Sort {

    /**
     * Takes a list and turns it into an array and calls bubbleSort to sort
     * the array.
     *
     * @param list the list to sort
     * @param <T> the type of elements in the list
     * @return the sorted array
     */

    public static <T extends Comparable<? super T>> T[] sortList(
        List<T> list) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        bubbleSort(array);

        return array;
    }

    /**
     * Sorts a list using the Comparable implementation of the elements.
     * Follows the bubble sort algorithm implemented in place using converting
     * the Comparable interface into CustomComparator.
     *
     * @param <T>   the type of the elements in the list
     * @param list the list to sort
     * @see CustomComparator
     */
    public static <T extends Comparable<? super T>> void bubbleSort(
        List<T> list) {
        bubbleSort(list, (a, b) -> a.compareTo(b));
    }


    /**
     * Sorts a list using bubble sort in place using the provided comparator.
     * Bubble sort repeatedly steps through the list, compares adjacent
     * elements and swaps them if they are in the wrong order. The
     * CustomComparator interface is used to define the implementation of the
     * comparator.
     *
     * @param list       the list to get contents from
     * @param comparator algorithm to order by
     * @param <T>        type of items in list
     */
    public static <T extends Comparable<? super T>> void bubbleSort(
            List<T> list, CustomComparator<? super T> comparator) {
        boolean swapped;
        int unsorted = list.size();
        for (int left = 0; left < unsorted - 1; left++) {
            swapped = false;
            for (int right = 0; right < unsorted - left - 1; right++) {
                int comparison = comparator.compare(list.get(right),
                    list.get(right + 1));
                if (comparison > 0) {
                    swap(list, right, right + 1);
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    /**
     * Swaps to elements within a generic list given their indices.
     *
     * @param <T>    the type of the elements in the array
     * @param list  the list that contains the elements
     * @param indexA the first element index
     * @param indexB the second element index
     */
    private static <T> void swap(List<T> list, int indexA, int indexB) {
        T cache = list.get(indexA);
        list.set(list.get(indexB), indexA);
        list.set(cache, indexB);
    }

    /**
     * Sorts an array using the Comparable implementation of the elements.
     * Follows the bubble sort algorithm implemented in place using converting
     * the Comparable interface into CustomComparator.
     *
     * @param <T>   the type of the elements in the array
     * @param array the array to sort
     * @see CustomComparator
     */
    public static <T extends Comparable<? super T>> void bubbleSort(
        T[] array) {
        bubbleSort(array, (a, b) -> a.compareTo(b));
    }

    /**
     * Sorts an array using bubble sort in place using the provided comparator.
     * Bubble sort repeatedly steps through the list, compares adjacent
     * elements and swaps them if they are in the wrong order. The
     * CustomComparator interface is used to define the implementation of the
     * comparator.
     *
     * @param <T>        the type of elements in the array
     * @param array      the array to sort
     * @param comparator the comparator to used to compare elements
     * @see CustomComparator
     */
    public static <T> void bubbleSort(
        T[] array, CustomComparator<? super T> comparator) {
        boolean swapped;
        int unsorted = array.length;
        for (int left = 0; left < unsorted - 1; left++) {
            swapped = false;
            for (int right = 0; right < unsorted - left - 1; right++) {
                int comparison = comparator.compare(array[right],
                    array[right + 1]);
                if (comparison > 0) {
                    swap(array, right, right + 1);
                    swapped = true;
                }
            }
            if (!swapped)
                break;
        }
    }

    /**
     * Swaps to elements within a generic array given their indices.
     *
     * @param <T>    the type of the elements in the array
     * @param array  the array that contains the elements
     * @param indexA the first element index
     * @param indexB the second element index
     */
    private static <T> void swap(T[] array, int indexA, int indexB) {
        T cache = array[indexA];
        array[indexA] = array[indexB];
        array[indexB] = cache;
    }

    /**
     * Private constructor to prevent instantiation of Sort class.
     */
    private Sort() {}
}

