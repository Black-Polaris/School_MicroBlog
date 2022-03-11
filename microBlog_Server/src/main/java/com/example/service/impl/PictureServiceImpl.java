package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Picture;
import com.example.mapper.PictureMapper;
import com.example.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {

    @Override
    public Picture upload(MultipartFile file, Integer id) throws IOException {
        String filename = System.currentTimeMillis() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String upload = "F:/Document/Desktop/MicroBlog/BlogPicture/";
        File parentFile = new File(upload);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String path = upload + filename;
        File filePath = new File(path);

        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        outputStream.write(file.getBytes());
        outputStream.flush();
        outputStream.close();

        Picture picture = new Picture();
        picture.setBlogId(id);
        picture.setPictureUrl(filename);
        picture.setCreateDate(new Date());
        return picture;
    }
}
