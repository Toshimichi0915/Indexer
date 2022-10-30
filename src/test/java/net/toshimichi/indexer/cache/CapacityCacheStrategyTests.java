package net.toshimichi.indexer.cache;

import net.toshimichi.indexer.Nation;
import net.toshimichi.indexer.ObservableSet;
import net.toshimichi.indexer.Vec2i;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CapacityCacheStrategyTests {

    @Test
    public void test() {
        ObservableSet<Object, Nation> nations = new ObservableSet<>();

        CapacityCacheStrategy<Nation> strategy = new CapacityCacheStrategy<>(3);
        CacheHandler<Object, Nation, ?> cache = new CacheHandler<>(nations, strategy);
        nations.subscribe(cache);

        Nation n0 = new Nation(new Vec2i(0, 0));
        Nation n1 = new Nation(new Vec2i(0, 1));
        Nation n2 = new Nation(new Vec2i(0, 2));
        Nation n3 = new Nation(new Vec2i(0, 3));

        nations.add(n0);
        nations.add(n1);
        nations.add(n2);
        nations.add(n3);
        assertEquals(4, nations.size());

        cache.clean();
        assertEquals(3, nations.size());
        assertFalse(nations.contains(n0));

        Nation n4 = new Nation(new Vec2i(0, 4));
        nations.add(n4);

        cache.update(n1);
        cache.clean();
        assertEquals(3, nations.size());
        assertFalse(nations.contains(n2));
        assertTrue(nations.contains(n1));
    }
}
