package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.CacheConstant;
import com.example.entity.Comment;
import com.example.service.CommentService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequiresAuthentication
    @PostMapping("/addComment")
    public Result addComment(@RequestParam String textarea, Integer blogId) {
        Comment comment = new Comment();
        comment.setUserId(ShiroUtil.getProfile().getId());
        comment.setBlogId(blogId);
        comment.setCommentContent(textarea);
        comment.setCreateDate(new Date());
        if (commentService.save(comment)) {
            Comment newComment = commentService.getOne(new QueryWrapper<Comment>().eq("user_id", ShiroUtil.getProfile().getId()).orderByDesc("create_date").last("LIMIT 1"));
            this.redisTemplate.opsForSet().add(CacheConstant.COMMENT_KEY + blogId, newComment);
            // 对相应的热搜微博的热度进行增加
            long hour = System.currentTimeMillis()/(1000*60*60);
            this.redisTemplate.opsForZSet().incrementScore(CacheConstant.HOUR_KEY + hour, blogId, 1);
        }
        Set set = this.redisTemplate.opsForSet().members(CacheConstant.COMMENT_KEY + blogId);
        return Result.success(set);
    }

    @GetMapping("getComment")
    public Result getComment(@RequestParam Integer blogId) {
        Set set = this.redisTemplate.opsForSet().members(CacheConstant.COMMENT_KEY + blogId);
        return Result.success(set);
    }

}
