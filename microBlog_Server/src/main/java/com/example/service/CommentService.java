package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Comment;

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
public interface CommentService extends IService<Comment> {

    int getUnreadCommentCount(Long userId);

    int getReadCommentCount(Long userId);

    List<Map<String, Object>> getCommentList(Long userId);
}
