package com.jk.rcp.main.data.model.token;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.jk.rcp.main.data.model.course.Course;

import java.util.ArrayList;

public class Token {

    @SerializedName("auth")
    @Expose
    private String auth;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
    @SerializedName("rol")
    @Expose
    private String rol;
    @SerializedName("courses")
    @Expose
    private ArrayList<Course> courses;

    /**
     * @param courses
     * @param auth
     * @param rol
     * @param token
     * @param refreshToken
     */
    public Token(String auth, String token, String refreshToken, String rol, ArrayList<Course> courses) {
        super();
        this.auth = auth;
        this.token = token;
        this.refreshToken = refreshToken;
        this.rol = rol;
        this.courses = courses;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Token{" +
                "auth='" + auth + '\'' +
                ", token='" + token + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", rol='" + rol + '\'' +
                ", courses='" + courses + '\'' +
                '}';
    }
}