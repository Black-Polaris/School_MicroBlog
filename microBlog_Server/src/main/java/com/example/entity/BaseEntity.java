package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 7550766086746707203L;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
