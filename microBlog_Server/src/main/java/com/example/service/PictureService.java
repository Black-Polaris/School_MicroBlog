package com.example.service;

import com.example.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
public interface PictureService extends IService<Picture> {

    Picture upload(MultipartFile file, Integer id) throws IOException;
}
