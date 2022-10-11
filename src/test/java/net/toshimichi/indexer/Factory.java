package net.toshimichi.indexer;

public class Factory {

    private final ObservableField<Factory, FactoryType> factoryType;
    private final ObservableField<Factory, Vec2i> start;
    private final ObservableField<Factory, Vec2i> end;

    public Factory(FactoryType factoryType, Vec2i start, Vec2i end) {
        this.factoryType = new ObservableField<>(factoryType);
        this.start = new ObservableField<>(start);
        this.end = new ObservableField<>(end);
    }

    public ObservableField<Factory, FactoryType> getFactoryType() {
        return factoryType;
    }

    public ObservableField<Factory, Vec2i> getStart() {
        return start;
    }

    public ObservableField<Factory, Vec2i> getEnd() {
        return end;
    }
}
