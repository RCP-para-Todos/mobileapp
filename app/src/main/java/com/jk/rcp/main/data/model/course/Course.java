package com.jk.rcp.main.data.model.course;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Course {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("event_date")
    @Expose
    private String eventDate;
    @SerializedName("students")
    @Expose
    private ArrayList<Student> students;
    @SerializedName("instructor")
    @Expose
    private Instructor instructor;

    /**
     * @param instructor
     * @param name
     * @param students
     * @param id
     * @param eventDate
     */
    public Course(String id, String name, String eventDate, ArrayList<Student> students, Instructor instructor) {
        super();
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.students = students;
        this.instructor = instructor;
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

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", students=" + students +
                ", instructor=" + instructor +
                '}';
    }
}