package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类 -自动加载到Spring容器中
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 设置序列化工具
        // 创建一个json的序列化对象
        GenericJackson2JsonRedisSerializer jackson = new GenericJackson2JsonRedisSerializer(String.valueOf(Object.class));
        // 设置key序列化方式string
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化方式为json
        template.setValueSerializer(jackson);
        // 设置hash key的序列化方式string
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置hash value的序列化方式json
        template.setHashValueSerializer(jackson);
        template.setEnableDefaultSerializer(true);
        template.afterPropertiesSet();
        return template;
    }
}
