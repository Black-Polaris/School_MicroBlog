package com.example.controller;

import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

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
