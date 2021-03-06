package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Avatar;
import com.example.entity.Blog;
import com.example.entity.Relation;
import com.example.entity.User;
import com.example.service.AvatarService;
import com.example.service.BlogService;
import com.example.service.RelationService;
import com.example.service.UserService;
import com.example.util.ShiroUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    BlogService blogService;

    @Autowired
    AvatarService avatarService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RelationService relationService;

    @Autowired
    ObjectMapper mapper;

    @RequiresAuthentication
    @PostMapping("/update")
    public Result update(@RequestBody User user) {
        User user1 = userService.getById(ShiroUtil.getProfile().getId());
        Assert.notNull(user1, "用户不存在");
        user1.setUsername(user.getUsername());
        user1.setNickname(user.getNickname());
        user1.setDescription(user.getDescription());
        user1.setGender(user.getGender());
        user1.setBirthDate(user.getBirthDate());
        if (userService.updateById(user1)) {
            User afterUpdate = userService.getById(user1.getId());
            List<Blog> blogList = blogService.list(new QueryWrapper<Blog>().eq("user_id", afterUpdate.getId()));
            blogList.forEach(blog -> {
                blog.setUser(afterUpdate);
                blogService.addBlog2BlogCache(blog);
            });
            return Result.success(afterUpdate);
        }
        return Result.fail("修改失败");
    }

    @RequiresAuthentication
    @PostMapping("/changePass")
    public Result changePass(@RequestBody User user) {
        User user1 = userService.getById(ShiroUtil.getProfile().getId());
        Assert.notNull(user1, "用户不存在");
        user1.setPassword(user.getPassword());
        if (userService.updateById(user1)) {
            User afterUpdate = userService.getById(user1.getId());
            List<Blog> blogList = blogService.list(new QueryWrapper<Blog>().eq("user_id", afterUpdate.getId()));
            blogList.forEach(blog -> {
                blog.setUser(afterUpdate);
                blogService.addBlog2BlogCache(blog);
            });
            return Result.success(afterUpdate);
        }
        return Result.fail("修改失败");
    }

    @PostMapping("/findUsers")
    public Result findUsers(@RequestParam String keyWords) {
        List<User> users = userService.list(new QueryWrapper<User>().like("nickname", keyWords));
        users.forEach(user -> {
            Avatar avatar = avatarService.getOne(new QueryWrapper<Avatar>().eq("user_id", user.getId()).orderByDesc("create_date").last("LIMIT 1"));
            user.setAvatar(avatar);
        });
        return Result.success(users);
    }

    // 通过用户id查找用户
    @GetMapping("/findUserById")
    public Result findUserById (@RequestParam Long userId) {
        User user = userService.getById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return Result.fail("查询不到用户");
        }
        Avatar avatar = avatarService.getOne(new QueryWrapper<Avatar>().eq("user_id", user.getId()).orderByDesc("create_date").last("LIMIT 1"));
        user.setAvatar(avatar);
        return Result.success(user);
    }

    // 添加新用户
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        List<User> list = userService.list(new QueryWrapper<User>().eq("username", user.getUsername()).or().eq("nickname", user.getNickname()));
        if (CollectionUtils.isEmpty(list)) {
            user.setCreateDate(new Date());
            if (userService.save(user)) {
                Avatar avatar = new Avatar();
                avatar.setUserId(user.getId());
                avatar.setAvatarUrl("0.jpeg");
                avatar.setCreateDate(new Date());
                avatarService.save(avatar);
                return Result.success(user);
            } else {
                return Result.fail("新建失败");
            }
        } else {
            return Result.fail("用户名或昵称已被使用");
        }
    }

    // 查看好友
    @PostMapping("/getFriends")
    public Result getFriends(@RequestParam("id") Long id) {
        List<Relation> list = relationService.list(new QueryWrapper<Relation>().eq("from_id", id).or().eq("to_id", id));
        Set<Integer> set = new HashSet<>();
        for (Relation relation : list) {
            set.add(relation.getFromId());
            set.add(relation.getToId());
        }
        List<User> users = new ArrayList<>();
        for (Integer i : set) {
            if (i != 0){
                users.add(userService.getById(i));
            }
        }
        return Result.success(users);
    }

}
