package net.toshimichi.indexer;

import java.util.ArrayList;

public class ObservableField<O, V> {

    private O owner;
    private V value;
    private final ArrayList<ObservableFieldHandler<O, V>> handlers = new ArrayList<>();

    public ObservableField(V value) {
        this.value = value;
    }

    public O getOwner() {
        if (owner == null) {
            throw new IllegalStateException("Not initialized");
        }
        return owner;
    }

    public void initialize(O owner) {
        if (this.owner != null) {
            if (this.owner.equals(owner)) return;
            throw new IllegalStateException("Already initialized");
        }

        this.owner = owner;
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
