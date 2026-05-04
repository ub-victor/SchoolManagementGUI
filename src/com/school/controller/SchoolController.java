package com.school.controller;

import com.school.dao.*;
import com.school.model.*;
import java.sql.SQLException;
import java.util.List;

public class SchoolController {
    private StudentDAO studentDAO = new StudentDAO();
    private TeacherDAO teacherDAO = new TeacherDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

    // ---------- Student operations ----------
    public boolean addStudent(Student s) {
        try {
            studentDAO.addStudent(s);
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Student> getAllStudents() {
        try { return studentDAO.getAllStudents(); } 
        catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public Student getStudent(String id) {
        try { return studentDAO.getStudentById(id); } 
        catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public boolean updateStudent(Student s) {
        try { studentDAO.updateStudent(s); return true; } 
        catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean deleteStudent(String id) {
        try { studentDAO.deleteStudent(id); return true; } 
        catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public double getFeesPaid(String studentId) {
        try { return studentDAO.getTotalPaid(studentId); } 
        catch (SQLException e) { e.printStackTrace(); return 0.0; }
    }

    // ---------- Teacher operations (similar) ----------
    // ... addTeacher, getAllTeachers, etc.

    // ---------- Course operations ----------
    // ... addCourse, getAllCourses, etc.

    // ---------- Enrollment operations ----------
    public boolean enrollStudent(String studentId, String courseCode) {
        try { enrollmentDAO.enroll(studentId, courseCode); return true; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Student> getEnrolledStudents(String courseCode) {
        try { return enrollmentDAO.getEnrolledStudents(courseCode); }
        catch (SQLException e) { e.printStackTrace(); return null; }
    }

    // ---------- Payment operations ----------
    public boolean payFees(String studentId, double amount) {
        try {
            Student s = studentDAO.getStudentById(studentId);
            if (s == null) return false;
            double paid = studentDAO.getTotalPaid(studentId);
            if (paid + amount > s.getTotalFees()) return false; // prevent overpay
            paymentDAO.addPayment(studentId, amount);
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}