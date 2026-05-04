public void enroll(String studentId, String courseCode) throws SQLException {
    String sql = "INSERT INTO enrollments (student_id, course_code) VALUES (?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, studentId);
        pstmt.setString(2, courseCode);
        pstmt.executeUpdate();
    }
}
public List<Student> getEnrolledStudents(String courseCode) throws SQLException {
    String sql = "SELECT s.* FROM students s JOIN enrollments e ON s.id = e.student_id WHERE e.course_code = ?";
    // ... execute and return list
}