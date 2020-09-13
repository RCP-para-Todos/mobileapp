package com.jk.rcp.main.data.model.user;

import android.app.Application;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jk.rcp.main.data.model.course.Course;

import java.util.ArrayList;

public class User extends Application {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("rol")
    @Expose
    private String rol;

    private String token;
    private String refreshToken;
    private ArrayList<Course> courses;
    private Boolean auth;

    public User() {
    }

    /**
     * @param password
     * @param rol
     * @param username
     */
    public User(String username, String password, String rol) {
        super();
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public User(String username, String rol, String token, String refreshToken, ArrayList<Course> courses, Boolean auth) {
        this.username = username;
        this.rol = rol;
        this.token = token;
        this.refreshToken = refreshToken;
        this.courses = courses;
        this.auth = auth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public Boolean getAuth() {
        return auth;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rol='" + rol + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", courses=" + courses +
                ", auth=" + auth +
                '}';
    }
}