package com.jk.rcp.main.data.model.course;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewCourse {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("instructor")
    @Expose
    private String instructor;
    @SerializedName("students")
    @Expose
    private List<String> student = null;

    /**
     * No args constructor for use in serialization
     */
    public NewCourse() {
    }

    /**
     * @param instructor
     * @param student
     * @param name
     * @param eventDate
     */
    public NewCourse(String name, String eventDate, String instructor, List<String> student) {
        super();
        this.name = name;
        this.eventDate = eventDate;
        this.instructor = instructor;
        this.student = student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

}