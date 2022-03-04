package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Avatar;
import com.example.mapper.AvatarMapper;
import com.example.service.AvatarService;
import com.example.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;

@Service
public class AvatarServiceImpl extends ServiceImpl<AvatarMapper, Avatar> implements AvatarService {

    @Override
    public Avatar upload(MultipartFile multipartFile) throws IOException {
        String filename = multipartFile.getOriginalFilename();
        String upload = "F:/Document/Desktop/MyAvatar/";
        File fileDir = new File(upload);

        String path = upload + filename;
        File filePath = new File(path);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        outputStream.write(multipartFile.getBytes());
        outputStream.flush();
        outputStream.close();

        Avatar tempAvatar = new Avatar();
        tempAvatar.setUserId(ShiroUtil.getProfile().getId());
        tempAvatar.setAvatarUrl(path);
        tempAvatar.setCreateDate(new Date());

        return tempAvatar;

    }
}
