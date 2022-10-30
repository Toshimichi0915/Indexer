package net.toshimichi.indexer.cache;

/**
 * This interface is used to manipulate a cache.
 *
 * @param <E> the type of the elements of the cache
 */
public interface CacheManipulator<E> {

    /**
     * Gets the first element of the cache.
     *
     * @return the first element of the cache
     */
    CacheNode<E> head();

    /**
     * Gets the last element of the cache.
     *
     * @return the last element of the cache
     */
    CacheNode<E> tail();

    /**
     * Gets the size of the cache.
     *
     * @return the size of the cache
     */
    int size();

    /**
     * Swaps the position of the two elements.
     *
     * @param n1 the element
     * @param n2 the element
     */
    void swap(CacheNode<E> n1, CacheNode<E> n2);

    /**
     * Sets the next element of the destination element.
     *
     * @param node the node to be set
     * @param dest the destination node
     */
    void moveAfter(CacheNode<E> node, CacheNode<E> dest);

    /**
     * Sets the previous element of the destination element.
     *
     * @param node the element to be set
     * @param dest the destination element
     */
    void moveBefore(CacheNode<E> node, CacheNode<E> dest);

    /**
     * Moves the element to the tail of the cache.
     *
     * @param node the element to be moved
     */
    void moveToTail(CacheNode<E> node);

    /**
     * Moves the element to the head of the cache.
     *
     * @param node the element to be moved
     */
    void moveToHead(CacheNode<E> node);
}
