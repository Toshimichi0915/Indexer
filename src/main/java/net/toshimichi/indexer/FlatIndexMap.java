package net.toshimichi.indexer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

class FlatIndexMap<K, V> extends AbstractMap<K, V> implements ObservableSetHandler<V, K> {

    private final HashMap<K, V> internal = new HashMap<>();
    private final Function<V, ObservableSet<V, K>> function;

    public FlatIndexMap(Function<V, ObservableSet<V, K>> function) {
        this.function = function;
    }

    private void put0(K key, V value) {
        if (internal.containsKey(key)) {
            throw new IllegalArgumentException("Duplicated key: " + key);
        }

        internal.put(key, value);
    }

    public void add0(V obj) {
        ObservableSet<V, K> set = function.apply(obj);
        set.initialize(obj);
        set.subscribe(this);
        set.forEach(it -> put0(it, obj));
    }

    public boolean remove0(V obj) {
        ObservableSet<V, K> set = function.apply(obj);
        boolean result = set.unsubscribe(this);
        if (result) set.forEach(internal::remove);
        return result;
    }

    @Override
    public void add(ObservableSet<? extends V, ? extends K> set, K element) {
        put0(element, set.getOwner());
    }

    @Override
    public void remove(ObservableSet<? extends V, ? extends K> set, K element) {
        internal.remove(element);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(internal.entrySet());
    }

    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(internal.keySet());
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(internal.values());
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
    public boolean containsValue(Object value) {
        return internal.containsValue(value);
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public V get(Object key) {
        return internal.get(key);
    }
}
