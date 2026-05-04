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

    // ---------- Student ----------
    public boolean addStudent(Student s) {
        try { studentDAO.addStudent(s); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Student> getAllStudents() {
        try { return studentDAO.getAllStudents(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public Student getStudent(String id) {
        try { return studentDAO.getStudentById(id); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public boolean updateStudent(Student s) {
        try { studentDAO.updateStudent(s); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean deleteStudent(String id) {
        try { studentDAO.deleteStudent(id); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public double getFeesPaid(String studentId) {
        try { return studentDAO.getTotalPaid(studentId); } catch (SQLException e) { e.printStackTrace(); return 0.0; }
    }

    // ---------- Teacher ----------
    public boolean addTeacher(Teacher t) {
        try { teacherDAO.addTeacher(t); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Teacher> getAllTeachers() {
        try { return teacherDAO.getAllTeachers(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public Teacher getTeacher(String id) {
        try { return teacherDAO.getTeacherById(id); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public boolean updateTeacher(Teacher t) {
        try { teacherDAO.updateTeacher(t); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean deleteTeacher(String id) {
        try { teacherDAO.deleteTeacher(id); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<String> getAllTeacherIds() {
        try { return teacherDAO.getAllTeacherIds(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }

    // ---------- Course ----------
    public boolean addCourse(Course c) {
        try { courseDAO.addCourse(c); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Course> getAllCourses() {
        try { return courseDAO.getAllCourses(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public Course getCourse(String code) {
        try { return courseDAO.getCourseByCode(code); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
    public boolean updateCourse(Course c) {
        try { courseDAO.updateCourse(c); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean deleteCourse(String code) {
        try { courseDAO.deleteCourse(code); return true; } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public boolean assignTeacherToCourse(String courseCode, String teacherId) {
        try { return courseDAO.assignTeacher(courseCode, teacherId); } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<String> getAllCourseCodesWithNames() {
        try { return courseDAO.getAllCourseCodesWithNames(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }

    // ---------- Enrollment ----------
    public boolean enrollStudent(String studentId, String courseCode) {
        try {
            if (enrollmentDAO.isEnrolled(studentId, courseCode)) return false;
            enrollmentDAO.enroll(studentId, courseCode);
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Object[]> getAllEnrollments() {
        try { return enrollmentDAO.getAllEnrollments(); } catch (SQLException e) { e.printStackTrace(); return null; }
    }

    // ---------- Payment ----------
    public boolean payFees(String studentId, double amount) {
        try {
            Student s = studentDAO.getStudentById(studentId);
            if (s == null) return false;
            double paid = studentDAO.getTotalPaid(studentId);
            if (paid + amount > s.getTotalFees()) return false;
            paymentDAO.addPayment(studentId, amount);
            return true;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
    public List<Object[]> getPaymentsByStudent(String studentId) {
        try { return paymentDAO.getPaymentsByStudent(studentId); } catch (SQLException e) { e.printStackTrace(); return null; }
    }
}