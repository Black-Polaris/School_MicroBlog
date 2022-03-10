package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BaseEntity  {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
