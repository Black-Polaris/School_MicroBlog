package com.example.controller;

import com.example.entity.User;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        User user = new User();
        user.setUsername("1");
        user.setPassword("123456");
        return "hello";
    }

    @RequestMapping("/aa")
    @ResponseBody
    public User aa() {
        User user = new User();
        user.setUsername("1");
        user.setPassword("123456");
        return user;
    }



}
