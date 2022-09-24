package net.toshimichi.indexer;

public interface ObservableSetHandler<E> {

    void add(SimpleObservableSet<E> set, E element);

    boolean remove(SimpleObservableSet<E> set, E element);
}
