package cn.lllllan.springdemo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class User {

    private int id;

    private String name;

    private String pwd;

    @JsonProperty("head_img")
    private String headImg;

    private String phone;

    @JsonProperty("create_time")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public User() {
    }

    public User(int id, String name, String pwd, String headImg, String phone, Date createTime) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
        this.headImg = headImg;
        this.phone = phone;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", headImg='" + headImg + '\'' +
                ", phone='" + phone + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
