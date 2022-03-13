package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.entity.CacheConstant;
import com.example.entity.Picture;
import com.example.service.BlogService;
import com.example.service.PictureService;
import com.example.service.UserService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    UserService userService;

    @Autowired
    PictureService pictureService;

    @Autowired
    RedisTemplate redisTemplate;

    // 查找最新微博
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        List<Blog> blogList = new ArrayList<>();
        Page page = new Page(currentPage, 5);
        IPage<Blog> pageData = blogService.page( page, new QueryWrapper<Blog>().orderByDesc("create_date"));
        List<Blog> blogs = pageData.getRecords();
        blogs.forEach(blog -> {
            String blogKey = CacheConstant.BLOG_KEY + blog.getId();
            if (!this.redisTemplate.hasKey(blogKey)) {
                blog = blogService.addBlog2BlogCache(blog);
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
                blogList.add(blog);
            }
            Blog cacheBlog = blogService.getBlogFromCache(blog.getId());
            if (cacheBlog.getStatus() == 2) {
                Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                cacheBlog.setFromBlog(fromBlog);
            }
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
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
                blogList.add(blog);
            });
        } else {
            blogSet.forEach(blogId -> {
                Blog blog = blogService.getBlogFromCache(blogId);
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
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
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
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
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
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
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
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
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
                blogList.add(blog);
            } else {
                Blog cacheBlog = blogService.getBlogFromCache(blog.getId());
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + cacheBlog.getContent());
                    cacheBlog.setFromBlog(fromBlog);
                }
                blogList.add(cacheBlog);
            }
        });
        return Result.success(blogList);
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        Assert.notNull(blog, "该博客已经被删除");
        return Result.success(blog);
    }

    @RequiresAuthentication
    @PostMapping("/addBlog")
    public Result addBlog(@RequestParam String textarea, MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Blog blog = new Blog();
        blog.setUserId(ShiroUtil.getProfile().getId());
        blog.setContent(textarea);
        blog.setStatus(1);
        blog.setCreateDate(new Date());
        Blog newBlog = new Blog();
        if (blogService.save(blog)) {
            newBlog = blogService.getOne(new QueryWrapper<Blog>().eq("user_id", ShiroUtil.getProfile().getId()).orderByDesc("create_date").last("limit 1"));
        }
        if (files!=null) {
            for (MultipartFile file : files){
                Picture picture = pictureService.upload(file, newBlog.getId());
                pictureService.save(picture);
            }
        }
        return Result.success("success");
    }

    @PostMapping("/searchBlog")
    public Result searchBlog(@RequestParam String keyWords, @RequestParam(defaultValue = "1") Integer currentPage) {

            this.redisTemplate.opsForZSet().incrementScore(CacheConstant.HotSearch, keyWords, 1);

        List<Blog> blogList = new ArrayList<>();
        Page page = new Page(currentPage, 5);
        IPage<Blog> pageData = blogService.page( page, new QueryWrapper<Blog>().like("content", keyWords).orderByDesc("create_date"));
        List<Blog> blogs = pageData.getRecords();
        blogs.forEach(blog -> {
            String blogKey = CacheConstant.BLOG_KEY + blog.getId();
            if (!this.redisTemplate.hasKey(blogKey)) {
                blog = blogService.addBlog2BlogCache(blog);
                if (blog.getStatus() == 2) {
                    Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blog.getContent());
                    blog.setFromBlog(fromBlog);
                }
                blogList.add(blog);
            }
            Blog cacheBlog = blogService.getBlogFromCache(blog.getId());
            if (blog.getStatus() == 2) {
                Blog fromBlog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + cacheBlog.getContent());
                cacheBlog.setFromBlog(fromBlog);
            }
            blogList.add(cacheBlog);
        });
        return Result.success(blogList);
    }

    @GetMapping("/hotSearch")
    public Result hotSearch() {
        Set set = this.redisTemplate.opsForZSet().reverseRangeWithScores(CacheConstant.HotSearch, 0, 50);
        return Result.success(set);
    }

    @GetMapping("/randomHotSearch")
    public Result randomHotSearch() {
        Set set = this.redisTemplate.opsForZSet().reverseRange(CacheConstant.HotSearch, 0, 30);
        List list = new ArrayList(set);
        Set result = new HashSet();
        while(result.size() < 10) {
            int randomIndex = new Random().nextInt(list.size());
            result.add(list.get(randomIndex));
        }
        return Result.success(result);
    }

    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        boolean exist = blogService.removeById(id);
        if (exist == false) {
            return Result.fail("微博删除失败");
        } else {
            return Result.success("微博删除成功");
        }
    }

}

