package com.school.dao;

import com.school.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (code, name, max_capacity, teacher_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCode());
            pstmt.setString(2, course.getName());
            pstmt.setInt(3, course.getMaxCapacity());
            pstmt.setString(4, course.getTeacherId());
            pstmt.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getInt("max_capacity"),
                    rs.getString("teacher_id")
                ));
            }
        }
        return list;
    }

    public Course getCourseByCode(String code) throws SQLException {
        String sql = "SELECT * FROM courses WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Course(code, rs.getString("name"), rs.getInt("max_capacity"), rs.getString("teacher_id"));
            }
        }
        return null;
    }

    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE courses SET name=?, max_capacity=?, teacher_id=? WHERE code=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getName());
            pstmt.setInt(2, course.getMaxCapacity());
            pstmt.setString(3, course.getTeacherId());
            pstmt.setString(4, course.getCode());
            pstmt.executeUpdate();
        }
    }

    public void deleteCourse(String code) throws SQLException {
        String sql = "DELETE FROM courses WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.executeUpdate();
        }
    }

    public boolean assignTeacher(String courseCode, String teacherId) throws SQLException {
        String sql = "UPDATE courses SET teacher_id = ? WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, teacherId);
            pstmt.setString(2, courseCode);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<String> getAllCourseCodesWithNames() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT code, name FROM courses";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("code") + " - " + rs.getString("name"));
            }
        }
        return list;
    }
}