package net.toshimichi.indexer;

import java.util.UUID;

public class Nation implements ObservableFieldHandler<Nation, Vec2i> {

    private final ObservableSet<Nation, UUID> leaders;
    private final ObservableSet<Nation, Factory> factories;
    private final ObservableField<Nation, Vec2i> headquarter;

    private final ObservableField<Nation, Vec2i> start;
    private final ObservableField<Nation, Vec2i> end;
    private final ObservableSet<Nation, Vec2i> chunks;

    public Nation(Vec2i headquarter) {
        this.leaders = new ObservableSet<>();
        this.factories = new ObservableSet<>();
        this.headquarter = new ObservableField<>(headquarter);

        this.start = new ObservableField<>(new Vec2i(0, 0));
        this.start.subscribe(this);
        this.end = new ObservableField<>(new Vec2i(0, 0));
        this.end.subscribe(this);
        this.chunks = new ObservableSet<>();

        updateChunks();
    }

    public ObservableSet<Nation, UUID> getLeaders() {
        return leaders;
    }

    public ObservableSet<Nation, Factory> getFactories() {
        return factories;
    }

    public ObservableField<Nation, Vec2i> getHeadquarter() {
        return headquarter;
    }

    public ObservableField<Nation, Vec2i> getStart() {
        return start;
    }

    public ObservableField<Nation, Vec2i> getEnd() {
        return end;
    }

    public ObservableSet<Nation, Vec2i> getChunks() {
        return chunks;
    }

    private void updateChunks() {
        Vec2i start = this.start.get();
        Vec2i startChunk = new Vec2i(start.x() >> 4, start.y() >> 4);

        Vec2i end = this.end.get();
        Vec2i endChunk = new Vec2i(end.x() >> 4, end.y() >> 4);

        chunks.clear();
        for (int x = startChunk.x(); x <= endChunk.x(); x++) {
            for (int y = startChunk.y(); y <= endChunk.y(); y++) {
                Vec2i chunk = new Vec2i(x, y);
                chunks.add(chunk);
            }
        }
    }

    @Override
    public void accept(ObservableField<? extends Nation, ? extends Vec2i> field, Vec2i old, Vec2i updated) {
        updateChunks();
    }
}
