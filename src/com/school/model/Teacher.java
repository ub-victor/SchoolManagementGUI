package com.school.model;

public class Teacher {
    private String id;
    private String name;
    private String email;
    private String specialization;
    private double salary;

    public Teacher(String id, String name, String email, String specialization, double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.salary = salary;
    }

    // Getters and Setters (omitted for brevity)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}