package RateLimiter.bucket;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RateLimiterAspect {

    @Pointcut("@annotation(RateLimiter.bucket.BucketRateLimiter)")
    public void rateLimiter() {
    }

    @Around("@annotation(bucketRateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint, BucketRateLimiter bucketRateLimiter) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String key = signature.getMethod().getDeclaringClass().getName() + "." + signature.getMethod().getName();
        TokenBucket bucket = TokenBucketFactory.getBucket(key, bucketRateLimiter.capacity(), bucketRateLimiter.rate());
        if (bucket.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            throw new RuntimeException("请稍后重试");
        }
    }

}
