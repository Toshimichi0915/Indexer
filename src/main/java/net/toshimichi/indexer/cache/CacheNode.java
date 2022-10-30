package net.toshimichi.indexer.cache;

/**
 * This class represents a node of a cache.
 *
 * @param <E> the type of the elements of the cache
 */
public class CacheNode<E> {

    private final E value;
    CacheNode<E> prev;
    CacheNode<E> next;

    CacheNode(E value, CacheNode<E> prev, CacheNode<E> next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

    CacheNode(E value) {
        this(value, null, null);
    }


    /**
     * Gets the value of the node.
     *
     * @return the value of the node
     */
    public E value() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "null" : value.toString();
    }
}
