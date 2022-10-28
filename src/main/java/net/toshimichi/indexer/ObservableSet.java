package net.toshimichi.indexer;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This class provides an implementation of the {@link Set} interface which tracks changes to the set.
 * <p>
 * Element additions and removals can be observed by registering an {@link ObservableSetHandler}
 * with the {@link #subscribe(ObservableSetHandler)} method. There are also methods to build indexes
 * which automatically gets updated when the set changes. You can use these methods or implement your own
 * index by implementing the {@link ObservableSetHandler} interface.
 * <p>
 * This class must be initialized with an owner object to build some kinds of indexes.
 * Although this class can be used without initialization, it is recommended to initialize this class whenever possible.
 *
 * @param <O> the type of the owner of this set
 * @param <E> the type of elements maintained by this set
 */
public class ObservableSet<O, E> extends AbstractSet<E> {

    private final HashSet<E> internal = new HashSet<>();
    private final ArrayList<ObservableSetHandler<O, E>> handlers = new ArrayList<>();
    private O owner;

    /**
     * Gets the owner of this set.
     *
     * @return the owner of this set
     * @throws IllegalStateException if this set is not initialized
     */
    public O getOwner() {
        if (owner == null) {
            throw new IllegalStateException("Not initialized");
        }
        return owner;
    }

    /**
     * Initializes this set.
     *
     * @param owner the owner of this set
     */
    public void initialize(O owner) {
        if (this.owner != null) {
            if (this.owner.equals(owner)) return;
            throw new IllegalStateException("Already initialized");
        }

        this.owner = owner;
    }

    /**
     * Subscribes the specified handler to this set.
     *
     * @param handler the handler to subscribe
     */
    public void subscribe(ObservableSetHandler<O, E> handler) {
        handlers.add(handler);
    }

    /**
     * Subscribes the specified handler to this set.
     *
     * @param adder   a consumer which is called when an element is added to this set
     * @param remover a consumer which is called when an element is removed from this set
     */
    public void subscribe(Consumer<E> adder, Predicate<E> remover) {
        subscribe(new ObservableSetHandler<>() {
            @Override
            public void add(ObservableSet<O, E> set, E element) {
                adder.accept(element);
            }

            @Override
            public boolean remove(ObservableSet<O, E> set, E element) {
                return remover.test(element);
            }
        });
    }

    /**
     * Unsubscribes the specified handler from this set.
     *
     * @param handler the handler to unsubscribe
     * @return true if the handler was subscribed to this set
     */
    public boolean unsubscribe(ObservableSetHandler<O, E> handler) {
        return handlers.remove(handler);
    }

    /**
     * Builds an index which maps elements to their owners.
     * <p>
     * Duplicated elements are not allowed in this index. If you want to have duplicated elements,
     * use {@link #createMultiIndex(Function)}  instead
     *
     * @param function a function which maps the owners to their elements
     * @param <K>      the type of the elements in the index
     * @return the index
     */
    public <K> Map<K, E> createIndex(Function<E, ObservableField<E, K>> function) {
        IndexMap<K, E> map = new IndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    /**
     * Builds an index which maps elements to their owners.
     * <p>
     * Duplicated elements are not allowed in this index. If you want to have duplicated elements,
     * use {@link #createFlatMultiIndex(Function)} instead.
     *
     * @param function a function which maps the owners to their elements
     * @param <K>      the type of the elements in the index
     * @return the index
     */
    public <K> Map<K, E> createFlatIndex(Function<E, ObservableSet<E, K>> function) {
        FlatIndexMap<K, E> map = new FlatIndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    /**
     * Builds an index which maps elements to their owners.
     * <p>
     * This method almost works exactly the same as {@link #createIndex(Function)}.
     * However, duplicated elements are allowed in this index.
     *
     * @param function a function which maps the owners to their elements
     * @param <K>      the type of the elements in the index
     * @return the index
     */
    public <K> Map<K, Set<E>> createMultiIndex(Function<E, ObservableField<E, K>> function) {
        MultiIndexMap<K, E> map = new MultiIndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    /**
     * Builds an index which maps elements to their owners.
     * <p>
     * This method almost works exactly the same as {@link #createFlatIndex(Function)}.
     * However, duplicated elements are allowed in this index.
     *
     * @param function a function which maps the owners to their elements
     * @param <K>      the type of the elements in the index
     * @return the index
     */
    public <K> Map<K, Set<E>> createFlatMultiIndex(Function<E, ObservableSet<E, K>> function) {
        FlatMultiIndexMap<K, E> map = new FlatMultiIndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    /**
     * Builds a set which maps the owners to their elements.
     *
     * @param function a function which maps the owners to their elements
     * @param <K>      the type of the elements in the set
     * @return the set
     */
    public <K> ObservableSet<?, K> createFlatMap(Function<E, ObservableSet<E, K>> function) {
        FlatMapSet<E, K> set = new FlatMapSet<>(function);
        forEach(set::add0);
        subscribe(set::add0, set::remove0);
        return set;
    }

    @Override
    public Iterator<E> iterator() {
        return new ObservableIterator(internal.iterator());
    }

    @Override
    public boolean add(E e) {
        boolean result = internal.add(e);
        if (result) handlers.forEach(it -> it.add(this, e));
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        boolean result = internal.remove(o);
        if (result) handlers.forEach(it -> it.remove(this, (E) o));
        return result;
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return internal.contains(o);
    }

    private class ObservableIterator implements Iterator<E> {

        private final Iterator<E> iterator;
        private E cursor;

        public ObservableIterator(Iterator<E> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public E next() {
            cursor = iterator.next();
            return cursor;
        }

        @Override
        public void remove() {
            iterator.remove();
            handlers.forEach(it -> it.remove(ObservableSet.this, cursor));
        }
    }
}
