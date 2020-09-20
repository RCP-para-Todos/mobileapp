package com.jk.rcp.main.data.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Users implements Serializable {

    @SerializedName("rol")
    @Expose
    private List<String> rol = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Created_date")
    @Expose
    private String createdDate;
    @SerializedName("__v")
    @Expose
    private Integer v;

    /**
     * No args constructor for use in serialization
     */
    public Users() {
    }

    /**
     * @param createdDate
     * @param v
     * @param name
     * @param id
     * @param rol
     */
    public Users(List<String> rol, String id, String name, String createdDate, Integer v) {
        super();
        this.rol = rol;
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.v = v;
    }

    public List<String> getRol() {
        return rol;
    }

    public void setRol(List<String> rol) {
        this.rol = rol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "Users{" +
                "rol=" + rol +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", v=" + v +
                '}';
    }
}