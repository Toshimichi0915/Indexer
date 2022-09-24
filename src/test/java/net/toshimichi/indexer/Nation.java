package net.toshimichi.indexer;

import java.util.UUID;

public class Nation {

    private final ObservableSet<Nation, UUID> leaders;
    private final ObservableSet<Nation, Vec2i> regions;
    private final ObservableSet<Nation, Factory> factories;
    private final ObservableField<Nation, Vec2i> headquarter;

    public Nation(Vec2i headquarter) {
        this.leaders = new ObservableSet<>(this);
        this.regions = new ObservableSet<>(this);
        this.factories = new ObservableSet<>(this);
        this.headquarter = new ObservableField<>(this, headquarter);
    }

    public ObservableSet<Nation, UUID> getLeaders() {
        return leaders;
    }

    public ObservableSet<Nation, Vec2i> getRegions() {
        return regions;
    }

    public ObservableSet<Nation, Factory> getFactories() {
        return factories;
    }

    public ObservableField<Nation, Vec2i> getHeadquarter() {
        return headquarter;
    }
}
