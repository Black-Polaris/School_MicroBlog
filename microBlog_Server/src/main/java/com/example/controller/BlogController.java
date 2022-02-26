package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.entity.Blog;
import com.example.service.BlogService;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class BlogController {

    @Autowired
    BlogService blogService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page( page, new QueryWrapper<Blog>().orderByDesc("create_date"));
        return Result.success(pageData);
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

    @RequiresAuthentication
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
