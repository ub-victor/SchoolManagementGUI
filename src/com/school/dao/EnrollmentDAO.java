package com.school.dao;

import com.school.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public void enroll(String studentId, String courseCode) throws SQLException {
        // Check capacity before enroll (can also be done in controller)
        String checkSql = "SELECT max_capacity, (SELECT COUNT(*) FROM enrollments WHERE course_code = ?) as enrolled FROM courses WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, courseCode);
            checkStmt.setString(2, courseCode);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int maxCap = rs.getInt("max_capacity");
                int enrolled = rs.getInt("enrolled");
                if (enrolled >= maxCap) {
                    throw new SQLException("Course is full");
                }
            }
        }
        String sql = "INSERT INTO enrollments (student_id, course_code) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseCode);
            pstmt.executeUpdate();
        }
    }

    public List<Student> getEnrolledStudents(String courseCode) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT s.* FROM students s JOIN enrollments e ON s.id = e.student_id WHERE e.course_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Student(rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getDouble("total_fees")));
            }
        }
        return list;
    }

    public List<Object[]> getAllEnrollments() throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT s.id, s.name, c.code, c.name FROM enrollments e " +
                     "JOIN students s ON e.student_id = s.id " +
                     "JOIN courses c ON e.course_code = c.code";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});
            }
        }
        return list;
    }

    public boolean isEnrolled(String studentId, String courseCode) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND course_code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, courseCode);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }
}