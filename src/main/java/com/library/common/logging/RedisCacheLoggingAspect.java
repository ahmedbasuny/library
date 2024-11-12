package com.library.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RedisCacheLoggingAspect {

    @Pointcut("@annotation(cacheable)")
    public void cacheableMethods(Cacheable cacheable) {
    }

    @Pointcut("@annotation(cachePut)")
    public void cachePutMethods(CachePut cachePut) {
    }

    @Pointcut("@annotation(cacheEvict)")
    public void cacheEvictMethods(CacheEvict cacheEvict) {
    }

    @Around("cacheableMethods(cacheable)")
    public Object logCacheable(ProceedingJoinPoint joinPoint,
                               Cacheable cacheable) throws Throwable {
        String cacheName = cacheable.value().length > 0 ? cacheable.value()[0] : "unknown";
        String cacheKey = Arrays.toString(joinPoint.getArgs());

        log.info("Checking cache: '{}' for key: '{}'", cacheName, cacheKey);
        Object result = joinPoint.proceed();

        if (result != null) {
            log.info("Cache hit for key: '{}' in cache: '{}'", cacheKey, cacheName);
        } else {
            log.info("Cache miss for key: '{}' in cache: '{}'", cacheKey, cacheName);
        }
        return result;
    }

    @Around("cachePutMethods(cachePut)")
    public Object logCachePut(ProceedingJoinPoint joinPoint,
                              CachePut cachePut) throws Throwable {
        String cacheName = cachePut.value().length > 0 ? cachePut.value()[0] : "unknown";
        String cacheKey = Arrays.toString(joinPoint.getArgs());

        log.info("Updating cache: '{}' with key: {}", cacheName, cacheKey);
        Object result = joinPoint.proceed();

        log.info("Cache updated for key: '{}' in cache: '{}'", cacheKey, cacheName);
        return result;
    }

    @Around("cacheEvictMethods(cacheEvict)")
    public Object logCacheEvict(ProceedingJoinPoint joinPoint,
                                CacheEvict cacheEvict) throws Throwable {
        String cacheName = cacheEvict.value().length > 0 ? cacheEvict.value()[0] : "unknown";
        String cacheKey = Arrays.toString(joinPoint.getArgs());

        log.info("Evicting cache: '{}' for key: {}", cacheName, cacheKey);
        Object result = joinPoint.proceed();

        log.info("Cache evicted for key: '{}' in cache: '{}'", cacheKey, cacheName);
        return result;
    }
}
