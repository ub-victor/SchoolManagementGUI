public void addPayment(String studentId, double amount) throws SQLException {
    String sql = "INSERT INTO payments (student_id, amount) VALUES (?, ?)";
    try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
        pstmt.setString(1, studentId);
        pstmt.setDouble(2, amount);
        pstmt.executeUpdate();
    }
}