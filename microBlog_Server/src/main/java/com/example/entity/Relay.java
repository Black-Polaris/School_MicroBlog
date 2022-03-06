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
public class Relay extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "relay_id", type = IdType.AUTO)
    private Integer relayId;

    private Integer userId;

    private Integer blogId;

    private String relayComment;

    public Integer getRelayId() {
        return relayId;
    }

    public void setRelayId(Integer relayId) {
        this.relayId = relayId;
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

    public String getRelayComment() {
        return relayComment;
    }

    public void setRelayComment(String relayComment) {
        this.relayComment = relayComment;
    }
}
