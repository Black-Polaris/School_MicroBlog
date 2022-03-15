package com.example.controller;

import com.example.common.Result;
import com.example.entity.CacheConstant;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    RedisTemplate redisTemplate;

    @RequiresAuthentication
    @GetMapping("/pullMsg")
    public Result pullMsg() {
        return Result.success(this.redisTemplate.opsForList().range(CacheConstant.Message, 0, -1));
    }

}
