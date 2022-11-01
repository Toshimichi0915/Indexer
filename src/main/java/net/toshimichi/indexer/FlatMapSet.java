package net.toshimichi.indexer;

import java.util.Iterator;
import java.util.function.Function;

class FlatMapSet<K, V> extends ObservableSet<Object, V> implements ObservableSetHandler<K, V> {

    private final Function<K, ObservableSet<K, V>> function;

    public FlatMapSet(Function<K, ObservableSet<K, V>> function) {
        this.function = function;
    }

    private void add1(V key) {
        if (contains(key)) {
            throw new IllegalArgumentException("Duplicated key: " + key);
        }
        super.add(key);
    }

    public void add0(K key) {
        ObservableSet<K, V> set = function.apply(key);
        set.initialize(key);

        // cannot be replaced by super#addAll because it causes UnsupportedOperationException
        set.forEach(super::add);
        set.subscribe(this);
    }

    public boolean remove0(K key) {
        ObservableSet<K, V> set = function.apply(key);
        boolean result = set.unsubscribe(this);
        if (result) set.forEach(super::remove);
        return result;
    }

    @Override
    public void add(ObservableSet<? extends K, ? extends V> set, V element) {
        add1(element);
    }

    @Override
    public void remove(ObservableSet<? extends K, ? extends V> set, V element) {
        super.remove(element);
    }

    @Override
    public boolean add(V v) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<V> iterator() {
        return new ImmutableIterator<>(super.iterator());
    }

    private static class ImmutableIterator<E> implements Iterator<E> {

        private final Iterator<E> internal;

        public ImmutableIterator(Iterator<E> internal) {
            this.internal = internal;
        }

        @Override
        public boolean hasNext() {
            return internal.hasNext();
        }

        @Override
        public E next() {
            return internal.next();
        }
    }
}
