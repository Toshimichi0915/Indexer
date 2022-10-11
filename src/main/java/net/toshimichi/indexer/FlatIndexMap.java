package net.toshimichi.indexer;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class FlatIndexMap<K, V> extends AbstractMap<K, V> implements ObservableSetHandler<V, K> {

    private final Map<K, V> internal = new HashMap<>();
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
    public Set<Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(internal.entrySet());
    }

    @Override
    public void add(ObservableSet<V, K> set, K element) {
        put0(element, set.getOwner());
    }

    @Override
    public boolean remove(ObservableSet<V, K> set, K element) {
        return internal.remove(element) != null;
    }
}
