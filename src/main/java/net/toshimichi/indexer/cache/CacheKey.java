package net.toshimichi.indexer.cache;

/**
 * Implementations of this interface are used to determine whether a cache entry should be removed.
 *
 * @param <E> the type of the elements of the cache
 */
@FunctionalInterface
public interface CacheKey<E> {

    /**
     * Returns the element of the cache.
     *
     * @return the element of the cache
     */
    E getKey();
}
