package cn.lllllan.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    private int id;

    @JsonProperty("user_name")
    private String userName;

    @JsonIgnore
    private String pwd;

    public User() {
    }

    public User(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    public User(int id, String userName, String pwd) {
        this.id = id;
        this.userName = userName;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
