package com.school.dao;

import com.school.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // Create
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (id, name, email, total_fees) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getEmail());
            pstmt.setDouble(4, student.getTotalFees());
            pstmt.executeUpdate();
        }
    }

    // Read all
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = new Student(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDouble("total_fees")
                );
                list.add(s);
            }
        }
        return list;
    }

    // Read by ID
    public Student getStudentById(String id) throws SQLException {
        String sql = "SELECT * FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDouble("total_fees")
                );
            }
        }
        return null;
    }

    // Update
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name=?, email=?, total_fees=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            pstmt.setDouble(3, student.getTotalFees());
            pstmt.setString(4, student.getId());
            pstmt.executeUpdate();
        }
    }

    // Delete
    public void deleteStudent(String id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    // Get total fees paid by a student (sum of payments)
    public double getTotalPaid(String studentId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0.0;
    }
}