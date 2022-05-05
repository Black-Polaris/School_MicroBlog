package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.CacheConstant;
import com.example.entity.Comment;
import com.example.entity.User;
import com.example.service.BlogService;
import com.example.service.CommentService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
    BlogService blogService;

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

    @GetMapping("/getComment")
    public Result getComment(@RequestParam Integer blogId) {
        Set set = this.redisTemplate.opsForSet().members(CacheConstant.COMMENT_KEY + blogId);
        return Result.success(set);
    }

    @GetMapping("/getCommentList")
    public Result getCommentList(@RequestParam Long userId) {
        List<Map<String, Object>> commentList = commentService.getCommentList(userId);
        for (Map<String, Object> comment: commentList) {
            Blog blog = blogService.getBlogFromCache(comment.get("blog_id"));
            if (blog.getStatus() == 2) {
                Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                blog.setFromBlog(fromBlog);
            }
            comment.put("blog", blog);
            User user = (User) this.redisTemplate.opsForValue().get(CacheConstant.USER_KEY + comment.get("user_id"));
            comment.put("user", user);
        }
        return Result.success(commentList);
    }

}
