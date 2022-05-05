package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Comment;

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
public interface CommentMapper extends BaseMapper<Comment> {
    int getUnreadCommentCount(Long userId);

    int getReadCommentCount(Long userId);

    List<Map<String,Object>> getCommentList(Long userId);
}
