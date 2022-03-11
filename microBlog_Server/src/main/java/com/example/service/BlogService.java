package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Blog;

public interface BlogService extends IService<Blog> {
    Blog addBlog2Cache(Blog blog, long hour);

    Blog getBlogFromCache(Object blogId);

    Blog addBlog2BlogCache(Blog blog);

}
