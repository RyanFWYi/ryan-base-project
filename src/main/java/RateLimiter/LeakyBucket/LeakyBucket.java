package RateLimiter.LeakyBucket;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 漏桶算法限流
 *
 * @author Ryan.Yi
 */
public class LeakyBucket {

    /**
     * 桶容量
     */
    private final long capacity;

    /**
     * 速率(请求/秒)
     */
    private final long leakRate;

    /**
     * 当前桶中的请求
     */
    private AtomicInteger water = new AtomicInteger(0);

    /**
     * 上次漏出时间
     */
    private AtomicLong lastLeakTime = new AtomicLong(System.currentTimeMillis());

    public LeakyBucket(long capacity, long leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRate = leakRatePerSecond;
    }

    public synchronized boolean tryAcquire() {
        leakWater();

        // 如果桶中的请求未达到容量
        if (water.get() < capacity) {
            water.incrementAndGet();
            return true;
        }
        return false;
    }

    private void leakWater() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastLeakTime.get();

        // 计算此段时间内已经漏出的水
        long leakedAmount = elapsedTime / 1000 * leakRate;
        if (water.get() > leakedAmount) {
            water.addAndGet((int) -leakedAmount);
        } else {
            water = new AtomicInteger(0);
        }

        lastLeakTime = new AtomicLong(now);
    }

}
