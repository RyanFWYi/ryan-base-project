package RateLimiter.LeakyBucket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LeakyBucketRateLimiter {

    /**
     * 桶容量
     */
    int capacity() default 100;

    /**
     * 漏出速率
     */
    int leakRate() default 10;

}
