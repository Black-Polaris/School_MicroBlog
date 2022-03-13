package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Avatar;
import com.example.entity.Blog;
import com.example.entity.CacheConstant;
import com.example.entity.User;
import com.example.mapper.BlogMapper;
import com.example.service.BlogService;
import com.example.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Blog addBlog2Cache(Blog blog, long hour) {
        Avatar avatar = blog.getUser().getAvatar();
        redisTemplate.opsForValue().set(CacheConstant.AVATAR_KEY + avatar.getId(), avatar);
        User user = blog.getUser();
        redisTemplate.opsForValue().set(CacheConstant.USER_KEY + user.getId(), user);
        redisTemplate.opsForValue().set(CacheConstant.BLOG_KEY + blog.getId(), blog);
        Map relayMap = new HashMap();
        relayMap.put("relaySize", this.redisTemplate.opsForSet().size(CacheConstant.RELAY_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.RELAY_KEY + blog.getId()));
        relayMap.put("isRelay", this.redisTemplate.opsForSet().isMember(CacheConstant.RELAY_KEY + blog.getId(), ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
        blog.setRelay(relayMap);
        blog.setComment(this.redisTemplate.opsForSet().size(CacheConstant.COMMENT_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.COMMENT_KEY + blog.getId()));
        Map loveMap = new HashMap();
        loveMap.put("loveSize", this.redisTemplate.opsForSet().size(CacheConstant.LOVE_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.LOVE_KEY + blog.getId()));
        loveMap.put("isLove", this.redisTemplate.opsForSet().isMember(CacheConstant.LOVE_KEY + blog.getId(), ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
        blog.setLove(loveMap);
        this.redisTemplate.opsForZSet().add(CacheConstant.HOUR_KEY + hour, blog.getId(),
                this.redisTemplate.opsForSet().size("relay:" + blog.getId()) +
                        this.redisTemplate.opsForValue().size("comment:" + blog.getId()) +
                        this.redisTemplate.opsForSet().size("love:" + blog.getId()));
        return blog;
    }

    @Override
    public Blog getBlogFromCache(Object blogId) {
        Blog blog = (Blog) this.redisTemplate.opsForValue().get(CacheConstant.BLOG_KEY + blogId);
        Map relayMap = new HashMap();
        relayMap.put("relaySize", this.redisTemplate.opsForSet().size(CacheConstant.RELAY_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.RELAY_KEY + blog.getId()));
        relayMap.put("isRelay", this.redisTemplate.opsForSet().isMember(CacheConstant.RELAY_KEY + blog.getId(), ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
        blog.setRelay(relayMap);
        blog.setComment(this.redisTemplate.opsForSet().size(CacheConstant.COMMENT_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.COMMENT_KEY + blog.getId()));
        Map loveMap = new HashMap();
        loveMap.put("loveSize", this.redisTemplate.opsForSet().size(CacheConstant.LOVE_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.LOVE_KEY + blog.getId()));
        loveMap.put("isLove", this.redisTemplate.opsForSet().isMember(CacheConstant.LOVE_KEY + blog.getId(), ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
        blog.setLove(loveMap);
        return blog;
    }

    @Override
    public Blog addBlog2BlogCache(Blog blog) {
        Avatar avatar = blog.getUser().getAvatar();
        redisTemplate.opsForValue().set(CacheConstant.AVATAR_KEY + avatar.getId(), avatar);
        User user = blog.getUser();
        redisTemplate.opsForValue().set(CacheConstant.USER_KEY + user.getId(), user);
        redisTemplate.opsForValue().set(CacheConstant.BLOG_KEY + blog.getId(), blog);
        Map relayMap = new HashMap();
        relayMap.put("relaySize", this.redisTemplate.opsForSet().size(CacheConstant.RELAY_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.RELAY_KEY + blog.getId()));
        relayMap.put("isRelay", this.redisTemplate.opsForSet().isMember(CacheConstant.RELAY_KEY + blog.getId(), ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
        blog.setRelay(relayMap);
        blog.setComment(this.redisTemplate.opsForSet().size(CacheConstant.COMMENT_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.COMMENT_KEY + blog.getId()));
        Map loveMap = new HashMap();
        loveMap.put("loveSize", this.redisTemplate.opsForSet().size(CacheConstant.LOVE_KEY + blog.getId()) == 0 ? 0 : this.redisTemplate.opsForSet().size(CacheConstant.LOVE_KEY + blog.getId()));
        loveMap.put("isLove", this.redisTemplate.opsForSet().isMember(CacheConstant.LOVE_KEY + blog.getId(), ShiroUtil.getProfile() == null ? 0 : ShiroUtil.getProfile().getId()));
        blog.setLove(loveMap);
        return blog;
    }
}
