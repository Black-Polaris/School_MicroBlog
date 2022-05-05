package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.CacheConstant;
import com.example.entity.Love;
import com.example.entity.User;
import com.example.service.BlogService;
import com.example.service.LoveService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/love")
public class LoveController {

    @Autowired
    LoveService loveService;

    @Autowired
    BlogService blogService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequiresAuthentication
    @PostMapping("/doLove")
    public Result doLove(@RequestBody Blog map) throws ParseException {
        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("blog_id", map.getId()));
        Assert.notNull(blog, "微博不存在");
        Love love = new Love();
        love.setBlogId((Integer) map.getId());
        love.setUserId(ShiroUtil.getProfile().getId());
        love.setCreateDate(new Date());
        if (loveService.save(love)) {
            this.redisTemplate.opsForSet().add(CacheConstant.LOVE_KEY + map.getId(), ShiroUtil.getProfile().getId());
            // 对相应的热搜微博的热度进行增加
            long hour = System.currentTimeMillis()/(1000*60*60);
            this.redisTemplate.opsForZSet().incrementScore(CacheConstant.HOUR_KEY + hour, map.getId(), 1);
            return Result.success("success");
        }
        return Result.fail("点赞失败");
    }

    @RequiresAuthentication
    @PostMapping("/unLove")
    public Result unLove(@RequestBody Blog map) {
        Blog blog = blogService.getOne(new QueryWrapper<Blog>().eq("blog_id", map.getId()));
        Assert.notNull(blog, "微博不存在");
        Love love = loveService.getOne(new QueryWrapper<Love>().and(i -> i.eq("blog_id", map.getId()).eq("user_id", ShiroUtil.getProfile().getId())));
        if (loveService.removeById(love.getLoveId())) {
            this.redisTemplate.opsForSet().remove(CacheConstant.LOVE_KEY + map.getId(), ShiroUtil.getProfile().getId());
            // 对相应的热搜微博的热度进行减少
            long hour = System.currentTimeMillis()/(1000*60*60);
            this.redisTemplate.opsForZSet().incrementScore(CacheConstant.HOUR_KEY + hour, map.getId(), -1);
            return Result.success("success");
        }
        return Result.fail("取消点赞失败");
    }

    @GetMapping("/getLoveList")
    public Result getLoveList(@RequestParam Long userId) {
        List<Map<String, Object>> likeList = loveService.getLikeList(userId);
        for (Map<String, Object> like: likeList) {
            Blog blog = blogService.getBlogFromCache(like.get("blog_id"));
            if (blog.getStatus() == 2) {
                Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                blog.setFromBlog(fromBlog);
            }
            like.put("blog", blog);
            User user = (User) this.redisTemplate.opsForValue().get(CacheConstant.USER_KEY + like.get("user_id"));
            like.put("user", user);
        }
        return Result.success(likeList);
    }



}
