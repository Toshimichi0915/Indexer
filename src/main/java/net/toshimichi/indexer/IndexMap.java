package net.toshimichi.indexer;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public class IndexMap<K, V> extends AbstractMap<K, V> implements ObservableFieldHandler<V, K> {

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
    public Set<Entry<K, V>> entrySet() {
        return Collections.unmodifiableSet(internal.entrySet());
    }

    @Override
    public void accept(ObservableField<V, K> field, K old, K updated) {
        V owner = field.getOwner();
        internal.remove(old);
        put0(updated, owner);
    }
}
