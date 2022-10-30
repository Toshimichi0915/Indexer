package net.toshimichi.indexer.cache;

import net.toshimichi.indexer.Nation;
import net.toshimichi.indexer.ObservableSet;
import net.toshimichi.indexer.Vec2i;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ExpirationCacheStrategyTests {

    @Test
    public void test() {
        ObservableSet<Object, Nation> nations = new ObservableSet<>();

        FakeClock clock = new FakeClock();
        ExpirationCacheStrategy<Nation> strategy = new ExpirationCacheStrategy<>(1, ChronoUnit.DAYS, clock);
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
        assertEquals(4, nations.size());

        clock.skip(25, ChronoUnit.HOURS);
        cache.update(n1);
        cache.clean();

        assertEquals(1, nations.size());
        assertTrue(nations.contains(n1));

        Nation n4 = new Nation(new Vec2i(0, 4));
        nations.add(n4);
        assertEquals(2, nations.size());

        cache.clean();
        assertEquals(2, nations.size());

        clock.skip(25, ChronoUnit.HOURS);
        cache.update(n4);
        cache.clean();

        assertEquals(1, nations.size());
        assertTrue(nations.contains(n4));

        clock.skip(25, ChronoUnit.HOURS);
        cache.clean();
        assertEquals(0, nations.size());
    }
}
