package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Avatar;
import com.example.entity.User;
import com.example.service.AvatarService;
import com.example.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @Autowired
    AvatarService avatarService;

    @Autowired
    UserService userService;

    @PostMapping("/uploadAvatar")
    public Avatar upload(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.upload(file);
        User user = userService.getById(avatar.getUserId());
        user.setAvatar(avatar);
        if (avatarService.save(avatar)) {
            userService.updateById(user);
        }

        Avatar newAvatar = avatarService.getOne(new QueryWrapper<Avatar>().orderByDesc("create_date").last("LIMIT 1"));
        return newAvatar;
    }

}
