package RateLimiter.bucket;

public interface BaseBucket {

    default boolean tryAcquire() {
        return true;
    }

}
