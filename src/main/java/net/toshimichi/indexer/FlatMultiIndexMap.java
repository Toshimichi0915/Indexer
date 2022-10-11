package net.toshimichi.indexer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class FlatMultiIndexMap<K, V> extends AbstractMap<K, Set<V>> implements ObservableSetHandler<V, K> {

    private final Map<K, Set<V>> internal = new HashMap<>();
    private final Function<V, ObservableSet<V, K>> function;

    public FlatMultiIndexMap(Function<V, ObservableSet<V, K>> function) {
        this.function = function;
    }

    @Override
    public Set<Entry<K, Set<V>>> entrySet() {
        return Collections.unmodifiableSet(internal.entrySet());
    }

    private void put0(K key, V value) {
        internal.computeIfAbsent(key, k -> new HashSet<>()).add(value);
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
        if (result) {
            for (K key : set) {
                Collection<V> collection = internal.get(key);
                collection.remove(obj);
                if (collection.isEmpty()) internal.remove(key);
            }
        }

        return result;
    }

    @Override
    public void add(ObservableSet<V, K> set, K element) {
        put0(element, set.getOwner());
    }

    @Override
    public boolean remove(ObservableSet<V, K> set, K element) {
        Collection<V> collection = internal.get(element);
        boolean result = collection.remove(set.getOwner());
        if (collection.isEmpty()) internal.remove(element);

        return result;
    }
}
