package net.toshimichi.indexer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

class IndexMap<K, V> extends AbstractMap<K, V> implements ObservableFieldHandler<V, K> {

    private final HashMap<K, V> internal = new HashMap<>();
    private final Function<V, ObservableField<V, K>> function;

    public IndexMap(Function<V, ObservableField<V, K>> function) {
        this.function = function;
    }

    private void put0(K key, V value) {
        if (internal.containsKey(key)) {
            throw new IllegalArgumentException("Duplicated key: " + key);
        }

        internal.put(key, value);
    }

    public void add0(V obj) {
        ObservableField<V, K> field = function.apply(obj);
        field.initialize(obj);
        field.subscribe(this);
        put0(field.get(), obj);
    }

    public boolean remove0(V obj) {
        ObservableField<V, K> field = function.apply(obj);
        boolean result = field.unsubscribe(this);
        if (result) internal.remove(field.get());
        return result;
    }

    @Override
    public void accept(ObservableField<? extends V, ? extends K> field, K old, K updated) {
        V owner = field.getOwner();
        internal.remove(old);
        put0(updated, owner);
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
