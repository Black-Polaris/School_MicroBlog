package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 *
 * </p>
 *
 * @author shenqinbin
 * @since 2022-03-04
 */
@TableName(value = "picture", resultMap = "PictureMapper")
public class Picture extends BaseEntity  {

    @TableId(value = "picture_id", type = IdType.AUTO)
    private Integer id;

    private Integer blogId;

    private String pictureUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
