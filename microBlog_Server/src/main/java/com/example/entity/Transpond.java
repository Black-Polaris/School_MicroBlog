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
public class Transpond extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "transpond_id", type = IdType.AUTO)
    private Integer transpondId;

    private Integer userId;

    private Integer blogId;

    private String transpondComment;

    public Integer getTranspondId() {
        return transpondId;
    }

    public void setTranspondId(Integer transpondId) {
        this.transpondId = transpondId;
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

    public String getTranspondComment() {
        return transpondComment;
    }

    public void setTranspondComment(String transpondComment) {
        this.transpondComment = transpondComment;
    }
}
