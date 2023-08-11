package src;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口限流
 * @author Ryan.Yi
 */
public class SlidingWindowRateLimiter {
    private final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
    private final int limit;
    private final long windowSizeInMs;
    private final AtomicInteger counter = new AtomicInteger(0);

    public SlidingWindowRateLimiter(int limitPerMinute) {
        this.limit = limitPerMinute;
        this.windowSizeInMs = 60 * 1000;
    }

    public boolean tryAcquire() {
        long now = System.currentTimeMillis();
        Long head = queue.peek();

        while (head != null && now - head > windowSizeInMs) {
            queue.poll();
            counter.decrementAndGet();
            head = queue.peek();
        }

        if (counter.get() < limit) {
            queue.offer(now);
            counter.incrementAndGet();
            return true;
        } else {
            return false;
        }
    }

}
