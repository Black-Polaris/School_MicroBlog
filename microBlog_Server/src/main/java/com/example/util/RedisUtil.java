package com.example.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * Redis工具类 -负责存储和提取数据
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> template;

    // 给Redis存数据的方法
    public Boolean set(String key, Object obj) {
        ValueOperations<String, Object> ops = template.opsForValue();
        ops.set(key, obj);
        return true;
    }

    // 从Redis中提取数据
    public Object get(String key) {
        ValueOperations<String, Object> ops = template.opsForValue();
        return ops.get(key);
    }

}
