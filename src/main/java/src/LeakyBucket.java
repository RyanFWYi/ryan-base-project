package src;

/**
 * 漏桶算法限流
 *
 * @author Ryan.Yi
 */
public class LeakyBucket {
    private final long capacity; // 桶的容量
    private final long leakRate; // 漏出的速率（单位: 请求/毫秒）
    private long water; // 当前桶中的水（请求）
    private long lastLeakTime; // 上次漏水的时间

    public LeakyBucket(long capacity, long leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRate = leakRatePerSecond / 1000; // 转化为每毫秒的速率
        this.water = 0;
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        leakWater();

        // 如果桶中的水未达到容量
        if (water < capacity) {
            water++;
            return true;
        }
        return false;
    }

    private void leakWater() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastLeakTime;

        // 计算此段时间内已经漏出的水
        long leakedAmount = elapsedTime * leakRate;
        if (water > leakedAmount) {
            water -= leakedAmount;
        } else {
            water = 0;
        }

        lastLeakTime = now;
    }

    public static void main(String[] args) {
        LeakyBucket bucket = new LeakyBucket(10, 5); // 桶容量10，每秒漏出5个请求

        for (int i = 0; i < 15; i++) {
            System.out.println("Request " + i + ": " + bucket.tryAcquire());
        }
    }

}
