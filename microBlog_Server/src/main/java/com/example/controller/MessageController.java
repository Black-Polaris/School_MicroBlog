package com.example.controller;

import com.example.common.Result;
import com.example.entity.CacheConstant;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

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

    @RequiresAuthentication
    @PostMapping("/pullBothMsg")
    public Result pullBothMsg(@RequestParam("fromId") Long fromId, @RequestParam("toId") Long toId) {
        String key = LongStream.of(fromId, toId)
                .sorted()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("-"));
        return Result.success(this.redisTemplate.opsForList().range(CacheConstant.Message + key, 0, -1));
    }

}
