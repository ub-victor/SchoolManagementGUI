package com.school.model;

public class Student {
    private String id;
    private String name;
    private String email;
    private double totalFees;

    public Student(String id, String name, String email, double totalFees) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.totalFees = totalFees;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public double getTotalFees() { return totalFees; }
    public void setTotalFees(double totalFees) { this.totalFees = totalFees; }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}