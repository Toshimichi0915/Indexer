package net.toshimichi.indexer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ObservableSetTests {

    @Test
    public void testCreateIndex() {
        ObservableSet<?, Nation> nations = new ObservableSet<>();
        Map<Vec2i, Nation> headquarterIndex = nations.createIndex(Nation::getHeadquarter);
        assertEquals(0, headquarterIndex.size());

        Nation nation0 = new Nation(new Vec2i(1, 2));
        nations.add(nation0);
        assertEquals(1, headquarterIndex.size());
        assertEquals(nation0, headquarterIndex.get(new Vec2i(1, 2)));

        Nation nation1 = new Nation(new Vec2i(2, 3));
        nations.add(nation1);
        assertEquals(2, headquarterIndex.size());
        assertEquals(nation1, headquarterIndex.get(new Vec2i(2, 3)));

        nations.remove(nation0);
        assertEquals(1, headquarterIndex.size());

        nations.remove(nation1);
        assertEquals(0, headquarterIndex.size());
    }

    @Test
    public void testCreateFlatIndex() {
        ObservableSet<?, Nation> nations = new ObservableSet<>();
        Map<UUID, Nation> leaderIndex = nations.createFlatIndex(Nation::getLeaders);
        assertEquals(0, leaderIndex.size());

        UUID leader0 = UUID.randomUUID();
        UUID leader1 = UUID.randomUUID();

        Nation nation0 = new Nation(new Vec2i(1, 2));
        nation0.getLeaders().add(leader0);
        nations.add(nation0);
        assertEquals(1, leaderIndex.size());
        assertEquals(nation0, leaderIndex.get(leader0));

        Nation nation1 = new Nation(new Vec2i(2, 3));
        nation1.getLeaders().add(leader1);
        nations.add(nation1);
        assertEquals(2, leaderIndex.size());
        nation1.getLeaders().remove(leader1);
        assertEquals(1, leaderIndex.size());

        Nation nation2 = new Nation(new Vec2i(2, 3));
        nations.add(nation2);
        assertEquals(1, leaderIndex.size());
        nation2.getLeaders().add(leader1);
        assertEquals(2, leaderIndex.size());

        nations.remove(nation0);
        nations.remove(nation2);
        assertEquals(0, leaderIndex.size());
    }

    @Test
    public void testCreateMultiIndex() {
        ObservableSet<?, Nation> nations = new ObservableSet<>();
        ObservableSet<?, Factory> factories = nations.createFlatMap(Nation::getFactories);
        Map<FactoryType, Set<Factory>> factoryTypeIndex = factories.createMultiIndex(Factory::getFactoryType);
        assertEquals(0, nations.size());
        assertEquals(0, factories.size());
        assertEquals(0, factoryTypeIndex.size());

        Nation nation0 = new Nation(new Vec2i(1, 2));
        Factory factory0 = new Factory(FactoryType.FOOD, new Vec2i(2, 3), new Vec2i(3, 4));
        Factory factory1 = new Factory(FactoryType.CLOTHES, new Vec2i(4, 5), new Vec2i(3, 2));
        Factory factory2 = new Factory(FactoryType.FOOD, new Vec2i(1, 0), new Vec2i(3, 1));
        nation0.getFactories().addAll(List.of(factory0, factory1, factory2));
        nations.add(nation0);
        assertEquals(1, nations.size());
        assertEquals(3, factories.size());
        assertEquals(2, factoryTypeIndex.size());
        assertEquals(Set.of(factory1), factoryTypeIndex.get(FactoryType.CLOTHES));
        assertEquals(Set.of(factory0, factory2), factoryTypeIndex.get(FactoryType.FOOD));

        nations.remove(nation0);
        assertEquals(0, nations.size());
        assertEquals(0, factories.size());
        assertEquals(0, factoryTypeIndex.size());
    }
}
