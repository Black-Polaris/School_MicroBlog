package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Love;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
public interface LoveMapper extends BaseMapper<Love> {

    int getUnreadLikeCount(Long userId);

    int getReadLikeCount(Long userId);

    List<Map<String, Object>> getLikeList(Long userId);
}
