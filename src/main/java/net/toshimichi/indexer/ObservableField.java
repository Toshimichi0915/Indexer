package net.toshimichi.indexer;

import java.util.ArrayList;
import java.util.List;

public class ObservableField<O, V> {

    private final O owner;
    private V value;
    private final List<ObservableFieldHandler<O, V>> handlers = new ArrayList<>();

    public ObservableField(O owner, V value) {
        this.owner = owner;
        this.value = value;
    }

    public O getOwner() {
        return owner;
    }

    public V get() {
        return value;
    }

    public void set(V value) {
        V old = this.value;
        this.value = value;
        this.handlers.forEach(it -> it.accept(this, old, value));
    }

    public void subscribe(ObservableFieldHandler<O, V> handler) {
        handlers.add(handler);
    }

    public boolean unsubscribe(ObservableFieldHandler<O, V> handler) {
        return handlers.remove(handler);
    }
}
