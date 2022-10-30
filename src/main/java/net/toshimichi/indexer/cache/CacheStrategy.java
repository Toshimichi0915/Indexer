package net.toshimichi.indexer.cache;

/**
 * Implementations of this interface define how elements of a cache are removed.
 *
 * @param <E> the type of the elements of the cache
 * @param <C> the type of the cache key
 */
public interface CacheStrategy<E, C extends CacheKey<E>> {

    /**
     * Constructs a new cache key.
     *
     * @param element the element of the cache
     * @return the cache key
     */
    C newCacheKey(E element);

    /**
     * Inserts the element to the cache.
     *
     * @param manipulator the cache manipulator
     * @param node        the node of the cache
     */
    void insert(CacheManipulator<C> manipulator, CacheNode<C> node);

    /**
     * Updates the element of the cache.
     *
     * @param manipulator the cache manipulator
     * @param node        the node of the cache
     */
    void update(CacheManipulator<C> manipulator, CacheNode<C> node);

    /**
     * Tests whether the element of the cache should be removed.
     *
     * @param manipulator the cache manipulator
     * @param node        the node of the cache
     * @return {@code true} if the element should be removed
     */
    boolean test(CacheManipulator<C> manipulator, CacheNode<C> node);
}
