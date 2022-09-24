package net.toshimichi.indexer;

@FunctionalInterface
public interface ObservableFieldHandler<O, V> {

    void accept(ObservableField<O, V> field, V old, V updated);
}
