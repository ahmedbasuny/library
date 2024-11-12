package com.library.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.AnnotationCacheOperationSource;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    @Override
    public CacheInterceptor cacheInterceptor() {
        CacheInterceptor cacheInterceptor = new CacheInterceptor();
        cacheInterceptor.setCacheResolver(cacheResolver());
        cacheInterceptor.setCacheManager(cacheManager());
        cacheInterceptor.setKeyGenerator(keyGenerator());
        cacheInterceptor.setErrorHandler(errorHandler());

        // Custom logging advice
        cacheInterceptor.setCacheOperationSources(
                new AnnotationCacheOperationSource() {
                    @Override
                    protected void logCacheHit(Object key, Cache cache) {
                        log.info("Cache hit for key: {}", key);
                    }

                    @Override
                    protected void logCacheMiss(Object key) {
                        log.info("Cache miss for key: {}", key);
                    }
                }
        );
        return cacheInterceptor;
    }

//    @Value("${cache.redis.config.ttl:60}")
//    private int ttl;
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(ttl))
//                .disableCachingNullValues()
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
//                );
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfig)
//                .transactionAware()
//                .build();
//    }
}