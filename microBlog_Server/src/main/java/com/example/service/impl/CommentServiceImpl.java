package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    public CommentMapper commentMapper;

    @Override
    public int getUnreadCommentCount(Long userId) {
        int unreadCommentCount = commentMapper.getUnreadCommentCount(userId);
        return unreadCommentCount;
    }

    @Override
    public int getReadCommentCount(Long userId) {
        int readCommentCount = commentMapper.getReadCommentCount(userId);
        return readCommentCount;
    }

    @Override
    public List<Map<String, Object>> getCommentList(Long userId) {
        List<Map<String, Object>> commentList = commentMapper.getCommentList(userId);
        return commentList;
    }


}
