package com.school.model;

public class Course {
    private String code;
    private String name;
    private int maxCapacity;
    private String teacherId;   // Store only teacher ID, not the whole Teacher object

    public Course(String code, String name, int maxCapacity, String teacherId) {
        this.code = code;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.teacherId = teacherId;
    }

    // Getters and Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
}