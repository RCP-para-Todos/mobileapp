package com.jk.rcp.main.data.model.course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    /**
     * @param name
     * @param id
     */
    public Student(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    /**
     * @param name
     */
    public Student(String name) {
        super();
        this.name = name;
    }

    public Student() {

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

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;

        if (object != null && object instanceof Student) {
            sameSame = this.name == ((Student) object).name;
        }

        return sameSame;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}