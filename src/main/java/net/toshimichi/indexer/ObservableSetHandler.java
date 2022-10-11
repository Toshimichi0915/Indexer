package net.toshimichi.indexer;

public interface ObservableSetHandler<O, E> {

    void add(ObservableSet<O, E> set, E element);

    boolean remove(ObservableSet<O, E> set, E element);
}
