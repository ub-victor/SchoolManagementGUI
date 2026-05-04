package com.school.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public void addPayment(String studentId, double amount) throws SQLException {
        String sql = "INSERT INTO payments (student_id, amount) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setDouble(2, amount);
            pstmt.executeUpdate();
        }
    }

    public List<Object[]> getPaymentsByStudent(String studentId) throws SQLException {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT id, amount, payment_date FROM payments WHERE student_id = ? ORDER BY payment_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("id"), rs.getDouble("amount"), rs.getDate("payment_date")});
            }
        }
        return list;
    }

    public double getTotalPaidByStudent(String studentId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        }
        return 0.0;
    }
}