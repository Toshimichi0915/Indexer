package net.toshimichi.indexer;

/**
 * This interface is used to handle updates of an {@link ObservableSet}.
 *
 * @param <O> the type of the owner of the set
 * @param <E> the type of the elements of the set
 */
public interface ObservableSetHandler<O, E> {

    /**
     * Called when an element is added to the set.
     *
     * @param set     the set
     * @param element the added element
     */
    void add(ObservableSet<? extends O, ? extends E> set, E element);

    /**
     * Called when an element is removed from the set.
     *
     * @param set     the set
     * @param element the removed element
     * @return true if the element is removed
     */
    boolean remove(ObservableSet<? extends O, ? extends E> set, E element);
}
