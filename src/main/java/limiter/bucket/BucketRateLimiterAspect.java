package limiter.bucket;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BucketRateLimiterAspect {

    @Pointcut("@annotation(limiter.bucket.BucketRateLimiter)")
    public void rateLimiter() {
    }

    @Around("@annotation(bucketRateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint, BucketRateLimiter bucketRateLimiter) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String key = signature.getMethod().getDeclaringClass().getName() + "." + signature.getMethod().getName();
        BaseBucket bucket;
        if (bucketRateLimiter.type().equals(EnumBucketType.TOKEN_BUCKET)) {
            bucket = RateLimiterFactory.getBucket(key, bucketRateLimiter.capacity(), bucketRateLimiter.rate());
        } else {
            bucket = RateLimiterFactory.getLeakyBucket(key, bucketRateLimiter.capacity(), bucketRateLimiter.rate());
        }
        if (bucket.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            throw new RuntimeException("Too many requests");
        }
    }

}
