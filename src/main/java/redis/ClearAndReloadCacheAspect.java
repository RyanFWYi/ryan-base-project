package redis;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class ClearAndReloadCacheAspect {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 切入点
     * 切入点,基于注解实现的切入点  加上该注解的都是Aop切面的切入点
     */
    @Pointcut("@annotation(redis.ClearAndReloadCache)")
    public void pointCut() {
    }

    /**
     * 环绕通知
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     *
     * @param proceedingJoinPoint proceedingJoinPoint
     */
    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("----------- 环绕通知 -----------");
        log.info("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName());
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        //方法对象
        Method targetMethod = methodSignature.getMethod();
        //反射得到自定义注解的方法对象
        ClearAndReloadCache annotation = targetMethod.getAnnotation(ClearAndReloadCache.class);
        //获取自定义注解的方法对象的参数即name
        String name = annotation.name();
        //模糊定义key
        Set<String> keys = stringRedisTemplate.keys("*" + name + "*");
        //模糊删除redis的key值
        stringRedisTemplate.delete(Objects.requireNonNull(keys));
        //执行加入双删注解的改动数据库的业务 即controller中的方法业务
        Object proceed = null;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        //开一个线程
        // 在线程中延迟删除  同时将业务代码的结果返回 这样不影响业务代码的执行
        new Thread(() -> {
            try {
                Thread.sleep(annotation.delay());
                //模糊删除
                Set<String> keys1 = stringRedisTemplate.keys("*" + name + "*");
                stringRedisTemplate.delete(Objects.requireNonNull(keys1));
                log.info("----------- 线程中延迟删除完毕 -----------");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //返回业务代码的值
        return proceed;
    }
}
