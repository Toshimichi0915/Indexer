package net.toshimichi.indexer.cache;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;

public class FakeClock extends Clock {

    private final ZoneId zoneId;
    private long offset;

    public FakeClock(ZoneId zoneId, long offset) {
        this.zoneId = zoneId;
        this.offset = offset;
    }

    public FakeClock() {
        this(ZoneOffset.UTC, 0);
    }

    @Override
    public ZoneId getZone() {
        return zoneId;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new FakeClock(zone, offset);
    }

    @Override
    public Instant instant() {
        return Instant.now().plusMillis(offset);
    }

    public void skip(long duration, TemporalUnit temporalUnit) {
        offset += temporalUnit.getDuration().multipliedBy(duration).toMillis();
    }
}
