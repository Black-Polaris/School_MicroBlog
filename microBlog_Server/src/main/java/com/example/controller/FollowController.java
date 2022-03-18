package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.CacheConstant;
import com.example.entity.Follow;
import com.example.service.FollowService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequiresAuthentication
    @PostMapping("doFollow")
    public Result doFollow(@RequestParam("followeeId") Integer followeeId) {
        Follow follow = new Follow();
        follow.setFolloweeId(followeeId);
        follow.setFollowerId(ShiroUtil.getProfile().getId());
        follow.setCreateDate(new Date());
        if (followService.save(follow)) {
            this.redisTemplate.opsForSet().add(CacheConstant.Follow + followeeId, ShiroUtil.getProfile().getId());
            this.redisTemplate.opsForSet().add(CacheConstant.Follower + ShiroUtil.getProfile().getId(), followeeId);
            return Result.success("success");
        } else {
            return Result.fail("关注失败");
        }
    }

    @RequiresAuthentication
    @PostMapping("unFollow")
    public Result unFollow(@RequestParam("followeeId") Integer followeeId) {
        Follow follow = followService.getOne(new QueryWrapper<Follow>().and(i -> i.eq("followee_id", followeeId).eq("follower_id", ShiroUtil.getProfile().getId())));
        if (followService.removeById(follow.getId())) {
            this.redisTemplate.opsForSet().remove(CacheConstant.Follow + followeeId, ShiroUtil.getProfile().getId());
            this.redisTemplate.opsForSet().remove(CacheConstant.Follower + ShiroUtil.getProfile().getId(), followeeId);
            return Result.success("success");
        } else {
            return Result.fail("取消关注失败");
        }
    }



}
