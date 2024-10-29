package org.cs213.clinic.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.cs213.clinic.util.ContainerConstants.*;

/**
 * Ordered collection of elements that allows duplicates and is resizable. This
 * class implements Iterable for compatibility with enhanced for loops.
 *
 * @param <E> the List's element type
 *
 * @author Elian Deogracia-Brito
 * @author Tiara Clyde
 */
public class List<E> implements Iterable<E> {
    /**
     * Contains the elements for the List.
     */
    private E[] objects;

    /**
     * Represents the current number of elements contained, not capacity.
     */
    private int size;

    /**
     * Default constructor that instantiates a List container with an initial
     * capacity of {@link ContainerConstants#INITIAL_CAPACITY}.
     */
    @SuppressWarnings("unchecked")
    public List() {
        objects = (E[]) new Object[INITIAL_CAPACITY];
        size = INITIAL_SIZE;
    }

    /**
     * Find the element instance within the collection and return its index.
     *
     * @param e element to look for
     * @return index of element found, otherwise
     *         {@link ContainerConstants#INDEX_NOT_FOUND}
     */
    private int find(E e) {
        for (int index = 0; index < size(); index++) {
            if (objects[index].equals(e)) {
                return index;
            }
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * Grow the objects array by {@link ContainerConstants#GROWTH_FACTOR}.
     */
    @SuppressWarnings("unchecked")
    private void grow() {
        E[] newObjects = (E[]) new Object[objects.length * GROWTH_FACTOR];
        System.arraycopy(objects, 0, newObjects, 0, objects.length);
        objects = newObjects;
    }

    /**
     * Checks if the List object contains an element.
     *
     * @param e the element to search for
     * @return a boolean value representing whether the element was found
     */
    public boolean contains(E e) {
        return find(e) != INDEX_NOT_FOUND;
    }

    /**
     * Add element to List and resize accordingly.
     *
     * @param e element to add
     */
    public void add(E e) {
        final double ratio = (double) size / objects.length;
        if (ratio >= LOAD_FACTOR) grow();
        objects[size++] = e;
    }

    /**
     * Remove element from List container.
     *
     * @param e element to remove
     */
    public void remove(E e) {
        int index = find(e);
        if (index == INDEX_NOT_FOUND) { return; }
        for (int i = index; i < objects.length - 1; i++) {
            if (objects[i] == null) { break; }
            objects[i] = objects[i + 1];
        }
        size--;
    }

    /**
     * Checks if the List collection is empty.
     *
     * @return if List is empty as a boolean value
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Get size of the List; the number of elements contained.
     *
     * @return the size of List
     */
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over elements of type {@code E}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator<>();
    }

    /**
     * Gets the specified object at a specific index.
     *
     * @param index the index
     * @return the object at the specific index
     */
    public E get(int index) {
        return objects[index];
    }

    /**
     * Sets a new object reference at the specified index.
     *
     * @param elem elem to add
     * @param index the index to insert in
     */
    public void set(E elem, int index) {
        objects[index] = elem;
    }

    /**
     * Returns the index of the element to search for.
     *
     * @param elem the element to search for
     * @return index the index of the element that is being
     * searched
     */
    public int indexOf(E elem) {
        for (int index = 0; index < size; index++) {
            if (objects[index].equals(elem)) {
                return index;
            }
        }

        return INDEX_NOT_FOUND;
    }

    /**
     * Responsible for iterating through the list of elements.
     *
     * @param <E> element type
     */
    @SuppressWarnings("TypeParameterHidesVisibleType")
    private class ListIterator<E> implements Iterator<E> {
        /**
         * Default constructor used by List.
         */
        public ListIterator() {}

        /**
         * Keep track of current index of element.
         */
        private int index = 0;

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return index < size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            return (E) objects[index++];
        }
    }
}
