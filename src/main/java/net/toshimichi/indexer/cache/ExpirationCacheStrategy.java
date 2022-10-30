package net.toshimichi.indexer.cache;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.TemporalUnit;

/**
 * This class represents a cache strategy that removes entries after a certain amount of time.
 *
 * @param <E> the type of the elements of the cache
 */
public class ExpirationCacheStrategy<E> implements CacheStrategy<E, ExpirationCacheStrategy.ExpirationCacheKey<E>> {

    private final long duration;
    private final TemporalUnit temporalUnit;
    private final Clock clock;

    /**
     * Constructs a new cache strategy.
     *
     * @param duration     the duration of the cache
     * @param temporalUnit the temporal unit of the duration
     * @param clock        the clock
     */
    public ExpirationCacheStrategy(long duration, TemporalUnit temporalUnit, Clock clock) {
        this.temporalUnit = temporalUnit;
        this.duration = duration;
        this.clock = clock;
    }

    /**
     * Constructs a new cache strategy.
     *
     * @param duration     the duration of the cache
     * @param temporalUnit the temporal unit of the duration
     */
    public ExpirationCacheStrategy(long duration, TemporalUnit temporalUnit) {
        this(duration, temporalUnit, Clock.systemUTC());
    }

    @Override
    public ExpirationCacheKey<E> newCacheKey(E element) {
        return new ExpirationCacheKey<>(element, Instant.now(clock).plus(duration, temporalUnit));
    }

    @Override
    public void insert(CacheManipulator<ExpirationCacheKey<E>> manipulator, CacheNode<ExpirationCacheKey<E>> node) {
        manipulator.moveToTail(node);
    }

    @Override
    public void update(CacheManipulator<ExpirationCacheKey<E>> manipulator, CacheNode<ExpirationCacheKey<E>> node) {
        manipulator.moveToTail(node);
        node.value().expiredAt = Instant.now(clock).plus(duration, temporalUnit);
    }

    @Override
    public boolean test(CacheManipulator<ExpirationCacheKey<E>> manipulator, CacheNode<ExpirationCacheKey<E>> node) {
        return node.value().expiredAt.isBefore(Instant.now(clock));
    }

    /**
     * This class holds when the cache entry expires.
     *
     * @param <E> the type of the element of the cache
     */
    public static class ExpirationCacheKey<E> implements CacheKey<E> {

        private final E key;
        private Instant expiredAt;

        /**
         * Constructs a new cache key.
         *
         * @param key       the key of the cache
         * @param expiredAt the instant when the cache entry expires
         */
        public ExpirationCacheKey(E key, Instant expiredAt) {
            this.key = key;
            this.expiredAt = expiredAt;
        }

        @Override
        public E getKey() {
            return key;
        }
    }
}
