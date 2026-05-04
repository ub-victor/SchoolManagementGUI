package com.school.dao;

import com.school.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherDAO {

    public void addTeacher(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO teachers (id, name, email, specialization, salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teacher.getId());
            pstmt.setString(2, teacher.getName());
            pstmt.setString(3, teacher.getEmail());
            pstmt.setString(4, teacher.getSpecialization());
            pstmt.setDouble(5, teacher.getSalary());
            pstmt.executeUpdate();
        }
    }

    public List<Teacher> getAllTeachers() throws SQLException {
        List<Teacher> list = new ArrayList<>();
        String sql = "SELECT * FROM teachers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Teacher(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("specialization"),
                    rs.getDouble("salary")
                ));
            }
        }
        return list;
    }

    public Teacher getTeacherById(String id) throws SQLException {
        String sql = "SELECT * FROM teachers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Teacher(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("specialization"),
                    rs.getDouble("salary")
                );
            }
        }
        return null;
    }

    public void updateTeacher(Teacher teacher) throws SQLException {
        String sql = "UPDATE teachers SET name=?, email=?, specialization=?, salary=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teacher.getName());
            pstmt.setString(2, teacher.getEmail());
            pstmt.setString(3, teacher.getSpecialization());
            pstmt.setDouble(4, teacher.getSalary());
            pstmt.setString(5, teacher.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteTeacher(String id) throws SQLException {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<String> getAllTeacherIds() throws SQLException {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT id FROM teachers";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) ids.add(rs.getString("id"));
        }
        return ids;
    }
}