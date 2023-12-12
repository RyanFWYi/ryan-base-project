package RateLimiter.LeakyBucket;

import RateLimiter.RateLimiterFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LeakyBucketRateLimiterAspect {

    @Pointcut("@annotation(RateLimiter.LeakyBucket.LeakyBucketRateLimiter)")
    public void leakyBucketRateLimiter() {
    }

    @Around("@annotation(leakyBucketRateLimiter)")
    public Object around(ProceedingJoinPoint joinPoint, LeakyBucketRateLimiter leakyBucketRateLimiter) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String key = signature.getMethod().getDeclaringClass().getName() + "." + signature.getMethod().getName();
        LeakyBucket bucket = RateLimiterFactory.getLeakyBucket(key, leakyBucketRateLimiter.capacity(), leakyBucketRateLimiter.leakRate());
        if (bucket.tryAcquire()) {
            return joinPoint.proceed();
        } else {
            throw new RuntimeException("请稍后重试");
        }
    }

}
