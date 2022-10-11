package net.toshimichi.indexer;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class MultiIndexMap<K, V> extends AbstractMap<K, Set<V>> implements ObservableFieldHandler<V, K> {

    private final HashMap<K, Set<V>> internal = new HashMap<>();
    private final Function<V, ObservableField<V, K>> function;

    public MultiIndexMap(Function<V, ObservableField<V, K>> function) {
        this.function = function;
    }

    private void put0(K key, V value) {
        internal.computeIfAbsent(key, k -> new HashSet<>()).add(value);
    }

    public void add0(V obj) {
        ObservableField<V, K> field = function.apply(obj);
        field.initialize(obj);
        field.subscribe(this);

        K key = field.get();
        if (key == null) return;
        put0(key, obj);
    }

    public boolean remove0(V obj) {
        ObservableField<V, K> field = function.apply(obj);
        boolean result = field.unsubscribe(this);
        if (result) {
            K key = field.get();
            if (key != null) {
                Collection<V> collection = internal.get(key);
                collection.remove(obj);
                if (collection.isEmpty()) {
                    internal.remove(key);
                }
            }
        }

        return result;
    }

    @Override
    public Set<Entry<K, Set<V>>> entrySet() {
        return Collections.unmodifiableSet(internal.entrySet());
    }

    @Override
    public void accept(ObservableField<V, K> field, K old, K updated) {
        if (old != null) internal.remove(old);
        if (updated != null) put0(updated, field.getOwner());
    }
}
