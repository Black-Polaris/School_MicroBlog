package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.CacheConstant;
import com.example.service.BlogService;
import com.example.service.UserService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    // 查找最新微博
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        Page page = new Page(currentPage, 5);
        IPage<Blog> pageData = blogService.page( page, new QueryWrapper<Blog>().orderByDesc("create_date"));
        List<Blog> blogs = pageData.getRecords();
        long hour = System.currentTimeMillis()/(1000*60*60);
        blogs.forEach(blog -> {
            String blogKey = CacheConstant.BLOG_KEY + blog.getId();
            if (!this.redisTemplate.hasKey(blogKey)) {
                blog = blogService.addBlog2BlogCache(blog);
                blogList.add(blog);
            }
            Blog cacheBlog = blogService.getBlogFromCache(blog.getId());
            blogList.add(cacheBlog);
        });
        return Result.success(blogList);
    }

    // 查找微博小时榜
    @GetMapping("/getHour")
    public Result getHour(@RequestParam Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        long hour = System.currentTimeMillis()/(1000*60*60);
        Set blogSet = this.redisTemplate.opsForZSet().reverseRange(CacheConstant.HOUR_KEY + hour, 5*(currentPage-1), 5*currentPage-1);
        if (CollectionUtils.isEmpty(blogSet)) {
            List<Blog> blogs = blogService.list(new QueryWrapper<Blog>().orderByDesc("create_date").last("Limit "+ 5*(currentPage-1)+","+5*currentPage));
            blogs.forEach(blog -> {
                blog = blogService.addBlog2Cache(blog, hour);
                blogList.add(blog);
            });
        } else {
            blogSet.forEach(blogId -> {
                Blog blog = blogService.getBlogFromCache(blogId);
                blogList.add(blog);
            });
        }
        return Result.success(blogList);
    }

    // 查找微博日榜
    @GetMapping("/getDay")
    public Result getDay(@RequestParam Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        Set blogSet = this.redisTemplate.opsForZSet().reverseRange(CacheConstant.DAY_KEY,5*(currentPage-1), 5*currentPage-1);
        if (!CollectionUtils.isEmpty(blogSet)) {
            blogSet.forEach(blogId -> {
                Blog blog = blogService.getBlogFromCache(blogId);
                blogList.add(blog);
            });
        }
        return Result.success(blogList);
    }

    // 查找微博周榜
    @GetMapping("/getWeek")
    public Result getWeek(@RequestParam Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        Set blogSet = this.redisTemplate.opsForZSet().reverseRange(CacheConstant.WEEK_KEY,5*(currentPage-1), 5*currentPage-1);
        if (!CollectionUtils.isEmpty(blogSet)) {
            blogSet.forEach(blogId -> {
                Blog blog = blogService.getBlogFromCache(blogId);
                blogList.add(blog);
            });
        }
        return Result.success(blogList);
    }

    // 查找微博月榜
    @GetMapping("/getMonth")
    public Result getMonth(@RequestParam Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        Set blogSet = this.redisTemplate.opsForZSet().reverseRange(CacheConstant.MONTH_KEY,5*(currentPage-1), 5*currentPage-1);
        if (!CollectionUtils.isEmpty(blogSet)) {
            blogSet.forEach(blogId -> {
                Blog blog = blogService.getBlogFromCache(blogId);
                blogList.add(blog);
            });
        }
        return Result.success(blogList);
    }


    @RequiresAuthentication
    @GetMapping("/myBlogs")
    public Result myBlogs(@RequestParam(defaultValue = "1") Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        Page page = new Page(currentPage, 5);
        IPage<Blog> pageData = blogService.page(page, new QueryWrapper<Blog>().eq("user_id", ShiroUtil.getProfile().getId()).orderByDesc("create_date"));
        List<Blog> blogs = pageData.getRecords();
        blogs.forEach(blog -> {
            String blogKey = CacheConstant.BLOG_KEY + blog.getId();
            if (!this.redisTemplate.hasKey(blogKey)) {
                blog = blogService.addBlog2BlogCache(blog);
                blogList.add(blog);
            }
            Blog cacheBlog = blogService.getBlogFromCache(blog.getId());
            blogList.add(cacheBlog);
        });
        return Result.success(blogList);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已经被删除");
        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {
        Blog tempBlog = null;
        if (blog.getId() != null) {
            tempBlog = blogService.getById(blog.getId());
            Assert.isTrue(tempBlog.getUserId() == ShiroUtil.getProfile().getId(), "没有权限编辑");
        } else {
            tempBlog = new Blog();
            tempBlog.setUserId(ShiroUtil.getProfile().getId());
            tempBlog.setCreateDate(new Date());
            tempBlog.setStatus(0);
        }

        BeanUtils.copyProperties(blog, tempBlog, "id", "userId", "create_date", "status");
        blogService.saveOrUpdate(tempBlog);

        return Result.success(null);

    }

    @PostMapping("/blog/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean exist = blogService.removeById(id);
        if (exist == false) {
            return Result.fail("微博删除失败");
        } else {
            return Result.success("微博删除成功");
        }
    }

}

