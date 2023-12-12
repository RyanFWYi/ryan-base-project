package RateLimiter.bucket;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TokenBucketFactory {

    private static final ConcurrentMap<String, TokenBucket> tokenBucketMap = new ConcurrentHashMap<>();

    public static TokenBucket getBucket(String key, int capacity, int rate) {
        return tokenBucketMap.computeIfAbsent(key, k -> new TokenBucket(capacity, rate));
    }

}
