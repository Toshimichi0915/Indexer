package net.toshimichi.indexer;

public class ObservableSet<O, E> extends SimpleObservableSet<E> {

    private final O owner;

    public ObservableSet(O owner) {
        this.owner = owner;
    }

    public O getOwner() {
        return owner;
    }
}
