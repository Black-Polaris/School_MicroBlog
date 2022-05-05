package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Love;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
public interface LoveService extends IService<Love> {

    int getUnreadLikeCount(Long userId);

    int getReadLikeCount(Long userId);

    List<Map<String, Object>> getLikeList(Long userId);
}
