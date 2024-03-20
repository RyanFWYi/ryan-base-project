package limiter.bucket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RateLimiterFactory {

    private static final ConcurrentMap<String, TokenBucket> tokenBucketMap = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, LeakyBucket> leakyBucketMap = new ConcurrentHashMap<>();

    public static TokenBucket getBucket(String key, int capacity, int rate) {
        return tokenBucketMap.computeIfAbsent(key, k -> new TokenBucket(capacity, rate));
    }

    public static LeakyBucket getLeakyBucket(String key, int capacity, int rate) {
        return leakyBucketMap.computeIfAbsent(key, k -> new LeakyBucket(capacity, rate));
    }

}
