package net.toshimichi.indexer.cache;

/**
 * This class represents a cache strategy that removes the least recently used element when the cache is full.
 *
 * @param <E> the type of the elements of the cache
 */
public class CapacityCacheStrategy<E> implements CacheStrategy<E, CacheKey<E>> {

    private final int capacity;

    /**
     * Constructs a new cache strategy.
     *
     * @param capacity the capacity of the cache
     */
    public CapacityCacheStrategy(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public CacheKey<E> newCacheKey(E element) {
        return () -> element;
    }

    @Override
    public void insert(CacheManipulator<CacheKey<E>> manipulator, CacheNode<CacheKey<E>> node) {
        manipulator.moveToTail(node);
    }

    @Override
    public void update(CacheManipulator<CacheKey<E>> manipulator, CacheNode<CacheKey<E>> node) {
        manipulator.moveToTail(node);
    }

    @Override
    public boolean test(CacheManipulator<CacheKey<E>> manipulator, CacheNode<CacheKey<E>> node) {
        return manipulator.size() > capacity;
    }
}
