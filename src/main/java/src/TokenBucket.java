package src;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶限流
 * @author Ryan.Yi
 */
public class TokenBucket {
    private final long capacity;
    private final long refillRate; // 令牌每毫秒填充的速率
    private AtomicLong lastRefillTimestamp;
    private AtomicLong availableTokens;

    public TokenBucket(long capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRate = refillRatePerSecond / 1000; // 转化为每毫秒的速率
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
        long tokensToAdd = elapsedTime * refillRate;
        availableTokens.addAndGet(tokensToAdd);
        availableTokens.set(Math.min(capacity, availableTokens.get())); // 确保不超过桶的容量
        lastRefillTimestamp.set(now);
    }
    public static void main(String[] args) {
        TokenBucket bucket = new TokenBucket(10, 5); // 桶容量10，每秒填充5个令牌

        for (int i = 0; i < 15; i++) {
            System.out.println("Request " + i + ": " + bucket.tryAcquire());
        }
    }

}
