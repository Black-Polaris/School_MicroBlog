package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import com.example.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


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
            return Result.success(afterUpdate);
        }
        return Result.fail("修改失败");
    }

    @PostMapping("/changePass")
    public Result changePass(@RequestBody User user) {
        User user1 = userService.getById(ShiroUtil.getProfile().getId());
        Assert.notNull(user1, "用户不存在");
        user1.setPassword(user.getPassword());
        if (userService.updateById(user1)) {
            User afterUpdate = userService.getById(user1.getId());
            return Result.success(afterUpdate);
        }
        return Result.fail("修改失败");
    }



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



}
