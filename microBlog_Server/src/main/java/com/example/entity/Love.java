package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
public class Love extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "love_id", type = IdType.AUTO)
    private Integer loveId;

    private Integer userId;

    private Integer blogId;

    public Integer getLoveId() {
        return loveId;
    }

    public void setLoveId(Integer loveId) {
        this.loveId = loveId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

}
