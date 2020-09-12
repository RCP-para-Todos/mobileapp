package com.jk.rcp.main.data.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserPost {

    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("env")
    @Expose
    private String env;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("token")
    @Expose
    private String token;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserPost{" +
                "state='" + state + '\'' +
                ", env='" + env + '\'' +
                ", user=" + user +
                ", msg='" + msg + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}