package net.toshimichi.indexer;

import java.util.ArrayList;

/**
 * This class acts as a container of a value and notifies its subscribers when the value is updated.
 * <p>
 * This class must be initialized by calling {@link #initialize(Object)} before using it.
 * However, initialization is automatically done by {@link ObservableSet} so in most cases you don't have to
 * manually call {@link #initialize(Object)}.
 *
 * @param <O> The type of the owner of this field.
 * @param <V> The type of the value of this field.
 */
public class ObservableField<O, V> {

    private O owner;
    private V value;
    private final ArrayList<ObservableFieldHandler<O, V>> handlers = new ArrayList<>();

    /**
     * Constructs a new instance of this class.
     *
     * @param value the initial value of this field, can be {@code null}
     */
    public ObservableField(V value) {
        this.value = value;
    }

    /**
     * Gets the owner of this field.
     *
     * @return the owner of this field
     * @throws IllegalStateException if this set is not initialized
     */
    public O getOwner() {
        if (owner == null) {
            throw new IllegalStateException("Not initialized");
        }
        return owner;
    }

    /**
     * Initializes this field.
     *
     * @param owner the owner of this field
     * @throws IllegalStateException if this field is already initialized and the owner is different
     */
    public void initialize(O owner) {
        if (this.owner != null) {
            if (this.owner.equals(owner)) return;
            throw new IllegalStateException("Already initialized");
        }

        this.owner = owner;
    }

    /**
     * Gets the value of this field.
     *
     * @return the value of this field
     */
    public V get() {
        return value;
    }

    /**
     * Sets the value of this field.
     *
     * @param value the value of this field
     */
    public void set(V value) {
        V old = this.value;
        this.value = value;
        this.handlers.forEach(it -> it.accept(this, old, value));
    }

    /**
     * Subscribes a handler to this field.
     *
     * @param handler the handler to subscribe
     */
    public void subscribe(ObservableFieldHandler<O, V> handler) {
        handlers.add(handler);
    }

    /**
     * Unsubscribes a handler from this field.
     *
     * @param handler the handler to unsubscribe
     * @return true if the handler was subscribed to this field
     */
    public boolean unsubscribe(ObservableFieldHandler<O, V> handler) {
        return handlers.remove(handler);
    }
}
