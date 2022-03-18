package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;
import java.util.Map;

@TableName(value = "blog", resultMap = "BlogMapper")
public class Blog extends BaseEntity  {

    @TableId(value = "blog_id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    @TableField(exist = false)
    private User user;

    private String content;

    private int status;

    @TableField(exist = false)
    private Blog fromBlog;

    @TableField(exist = false)
    private String[] pictures;

    @TableField(exist = false)
    private Map relay;

    @TableField(exist = false)
    private Long comment;

    @TableField(exist = false)
    private List<Comment> commentList;

    @TableField(exist = false)
    private Map love;

    @TableField(exist = false)
    private Boolean hadFollow;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String[] getPictures() {
        return pictures;
    }

    public void setPictures(String[] pictures) {
        this.pictures = pictures;
    }

    public Map getRelay() {
        return relay;
    }

    public void setRelay(Map relay) {
        this.relay = relay;
    }

    public Long getComment() {
        return comment;
    }

    public void setComment(Long comment) {
        this.comment = comment;
    }

    public Map getLove() {
        return love;
    }

    public void setLove(Map love) {
        this.love = love;
    }

    public Blog getFromBlog() {
        return fromBlog;
    }

    public void setFromBlog(Blog fromBlog) {
        this.fromBlog = fromBlog;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public Boolean getHadFollow() {
        return hadFollow;
    }

    public void setHadFollow(Boolean hadFollow) {
        this.hadFollow = hadFollow;
    }
}
