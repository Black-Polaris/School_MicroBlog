package com.example.service.impl;

import com.example.entity.Love;
import com.example.mapper.LoveMapper;
import com.example.service.LoveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
@Service
public class LoveServiceImpl extends ServiceImpl<LoveMapper, Love> implements LoveService {

    @Autowired
    public LoveMapper loveMapper;

    @Override
    public int getUnreadLikeCount(Long userId) {
        int unreadLikeCount = loveMapper.getUnreadLikeCount(userId);
        return unreadLikeCount;
    }

    @Override
    public int getReadLikeCount(Long userId) {
        int readLikeCount = loveMapper.getReadLikeCount(userId);
        return readLikeCount;
    }

    @Override
    public List<Map<String, Object>> getLikeList(Long userId) {
        List<Map<String, Object>> likeList = loveMapper.getLikeList(userId);
        return likeList;
    }
}
