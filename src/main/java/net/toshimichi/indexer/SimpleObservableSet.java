package net.toshimichi.indexer;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleObservableSet<E> extends AbstractSet<E> {

    private final HashSet<E> internal = new HashSet<>();
    private final List<ObservableSetHandler<E>> handlers = new ArrayList<>();

    @Override
    public Iterator<E> iterator() {
        return new ObservableIterator(internal.iterator());
    }

    @Override
    public int size() {
        return internal.size();
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

    public void subscribe(ObservableSetHandler<E> handler) {
        handlers.add(handler);
    }

    public void subscribe(Consumer<E> adder, Predicate<E> remover) {
        subscribe(new ObservableSetHandler<>() {
            @Override
            public void add(SimpleObservableSet<E> set, E element) {
                adder.accept(element);
            }

            @Override
            public boolean remove(SimpleObservableSet<E> set, E element) {
                return remover.test(element);
            }
        });
    }

    public boolean unsubscribe(ObservableSetHandler<E> handler) {
        return handlers.remove(handler);
    }

    public <K> Map<K, E> createIndex(Function<E, ObservableField<E, K>> function) {
        IndexMap<K, E> map = new IndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    public <K> Map<K, E> createFlatIndex(Function<E, ObservableSet<E, K>> function) {
        FlatIndexMap<K, E> map = new FlatIndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    public <K> Map<K, Set<E>> createMultiIndex(Function<E, ObservableField<E, K>> function) {
        MultiIndexMap<K, E> map = new MultiIndexMap<>(function);
        forEach(map::add0);
        subscribe(map::add0, map::remove0);
        return map;
    }

    public <K> SimpleObservableSet<K> createFlatMap(Function<E, ObservableSet<E, K>> function) {
        FlatMapSet<E, K> set = new FlatMapSet<>(function);
        forEach(set::add0);
        subscribe(set::add0, set::remove0);
        return set;
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
            handlers.forEach(it -> it.remove(SimpleObservableSet.this, cursor));
        }
    }
}
