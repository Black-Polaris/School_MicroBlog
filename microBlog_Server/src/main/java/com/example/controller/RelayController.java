package com.example.controller;


import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.CacheConstant;
import com.example.entity.Relay;
import com.example.service.BlogService;
import com.example.service.RelayService;
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
@RequestMapping("/relay")
public class RelayController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RelayService relayService;

    @Autowired
    BlogService blogService;

    @RequiresAuthentication
    @PostMapping("/doRelay")
    public Result doRelay(@RequestParam Integer blogId) {
        Relay relay = new Relay();
        relay.setUserId(ShiroUtil.getProfile().getId());
        relay.setBlogId(blogId);
        relay.setCreateDate(new Date());
        if (relayService.save(relay)) {
            Blog blog = new Blog();
            blog.setUserId(ShiroUtil.getProfile().getId());
            blog.setContent(String.valueOf(blogId));
            blog.setStatus(2);
            blog.setCreateDate(new Date());
            blogService.save(blog);
            this.redisTemplate.opsForSet().add(CacheConstant.LOVE_KEY + blogId, ShiroUtil.getProfile().getId());
//           // 对相应的热搜微博的热度进行增加
            long hour = System.currentTimeMillis()/(1000*60*60);
            this.redisTemplate.opsForZSet().incrementScore(CacheConstant.HOUR_KEY + hour, blogId, 1);
        }
        return Result.success("success");
    }

}
