package RateLimiter.bucket;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶限流
 * @author Ryan.Yi
 */
public class TokenBucket {

    /**
     * 令牌桶的容量
     */
    private final long capacity;

    /**
     * 每毫秒的填充速率
     */
    private final long refillRate;

    /**
     * 上次填充令牌的时间戳
     */
    private final AtomicLong lastRefillTimestamp;

    /**
     * 当前可用的令牌数量
     */
    private final AtomicLong availableTokens;

    public TokenBucket(long capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRate = refillRatePerSecond;
        this.availableTokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = new AtomicLong(System.currentTimeMillis());
    }

    public synchronized boolean tryAcquire() {
        refill();
        if (availableTokens.get() > 0) {
            availableTokens.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastRefillTimestamp.get();
        long tokensToAdd = (elapsedTime / 1000) * refillRate;
        availableTokens.updateAndGet(n -> Math.min(capacity, n + tokensToAdd));
        lastRefillTimestamp.set(now);
    }

}
