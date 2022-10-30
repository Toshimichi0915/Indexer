package net.toshimichi.indexer.cache;

import net.toshimichi.indexer.ObservableSet;
import net.toshimichi.indexer.ObservableSetHandler;

import java.util.HashMap;

/**
 * This class provides a mechanism to remove specific elements from {@link ObservableSet} based on {@link CacheStrategy}.
 *
 * @param <O> the type of the owner of the set
 * @param <E> the type of the elements of the set
 * @param <C> the type of the cache key, usually provided by {@link CacheStrategy}
 */
public class CacheHandler<O, E, C extends CacheKey<E>> implements ObservableSetHandler<O, E> {

    private final ObservableSet<?, E> set;
    private final CacheStrategy<E, C> strategy;

    private final HashMap<E, CacheNode<C>> map = new HashMap<>();
    private final CacheNodeList<C> list = new CacheNodeList<>();
    private boolean autoCleanEnabled;

    /**
     * Constructs a new instance.
     *
     * @param set      the set to be handled
     * @param strategy the strategy to be used
     */
    public CacheHandler(ObservableSet<?, E> set, CacheStrategy<E, C> strategy) {
        this.set = set;
        this.strategy = strategy;
    }

    /**
     * Removes elements from the set based on the strategy.
     */
    public void clean() {
        CacheNode<C> head;
        while ((head = list.head()) != null && strategy.test(list, head)) {
            set.remove(head.value().getKey());
        }
    }

    /**
     * Updates the state of the element.
     *
     * @param element the element
     */
    public void update(E element) {
        CacheNode<C> node = map.get(element);
        if (node == null) {
            throw new IllegalArgumentException("Not cached: " + element);
        }

        strategy.update(list, node);
    }

    @Override
    public void add(ObservableSet<O, E> set, E element) {
        CacheNode<C> node = list.addLast(strategy.newCacheKey(element));
        strategy.insert(list, node);
        map.put(element, node);
    }

    @Override
    public void remove(ObservableSet<O, E> set, E element) {
        CacheNode<C> node = map.remove(element);
        if (node == null) return;
        list.remove(node);
    }
}
