package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Avatar;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface AvatarService extends IService<Avatar> {
    Avatar upload(MultipartFile file) throws IOException;
}
