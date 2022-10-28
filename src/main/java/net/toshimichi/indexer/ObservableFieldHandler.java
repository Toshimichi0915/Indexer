package net.toshimichi.indexer;

/**
 * This interface is used to handle updates of an {@link ObservableField}.
 *
 * @param <O> the type of the owner of the field
 * @param <V> the type of the value of the field
 */
@FunctionalInterface
public interface ObservableFieldHandler<O, V> {

    /**
     * Called when the value of the field is updated.
     *
     * @param field   the field
     * @param old     the old value
     * @param updated the updated value
     */
    void accept(ObservableField<O, V> field, V old, V updated);
}
