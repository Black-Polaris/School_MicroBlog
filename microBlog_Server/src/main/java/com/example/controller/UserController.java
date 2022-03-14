package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.Result;
import com.example.entity.Avatar;
import com.example.entity.Blog;
import com.example.entity.User;
import com.example.service.AvatarService;
import com.example.service.BlogService;
import com.example.service.UserService;
import com.example.util.ShiroUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//  TODO 如下待删除
    @GetMapping("/{id}")
    public User obj(@PathVariable("id") int id){
        User u = new User();
        u.setUsername("111");
        u.setPassword("2222");
        userService.save(u);
        return u;

    }

    @RequiresAuthentication
    @RequestMapping("/test")
    public Result test() {
        User user = userService.getById(1L);
        return Result.success(user);
    }

    @RequestMapping("/save")
    public Result save(@Validated @RequestBody User user) {

        return Result.success(user);
    }

    @RequestMapping("/redisTest")
    public Result redisTest() {

        List<User> users = mapper.convertValue(redisTemplate.opsForZSet().range("users:user", 0, -1), new TypeReference<List<User>>() {});
//        List<User> user = (List<User>) redisTemplate.opsForZSet().range("user",0,-1);

        if (users.size() == 0){
            users = userService.list(new QueryWrapper<User>().orderByAsc("create_date"));
            users.forEach(u -> {
                redisTemplate.opsForZSet().add("users:user", u, u.getId()+u.getAvatarId());
            });
        }
        return Result.success(users);
    }



}
