package com.school.view;

import com.school.controller.SchoolController;
import com.school.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EnrollmentPanel extends JPanel {
    private SchoolController controller;
    private JComboBox<String> cmbStudent, cmbCourse;
    private JButton btnEnroll, btnRefresh;
    private JTable table;
    private DefaultTableModel tableModel;

    public EnrollmentPanel(SchoolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Top: enrollment form
        JPanel formPanel = new JPanel(new FlowLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Enroll Student in Course"));
        formPanel.add(new JLabel("Student:"));
        cmbStudent = new JComboBox<>();
        formPanel.add(cmbStudent);
        formPanel.add(new JLabel("Course:"));
        cmbCourse = new JComboBox<>();
        formPanel.add(cmbCourse);
        btnEnroll = new JButton("Enroll");
        btnRefresh = new JButton("Refresh");
        formPanel.add(btnEnroll);
        formPanel.add(btnRefresh);
        add(formPanel, BorderLayout.NORTH);

        // Table to show enrollments (student, course)
        String[] columns = {"Student ID", "Student Name", "Course Code", "Course Name"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        btnEnroll.addActionListener(e -> enrollStudent());
        btnRefresh.addActionListener(e -> refreshAll());

        refreshAll();
    }

    private void refreshAll() {
        refreshStudentCombo();
        refreshCourseCombo();
        refreshEnrollmentTable();
    }

    private void refreshStudentCombo() {
        cmbStudent.removeAllItems();
        List<Student> students = controller.getAllStudents();
        if (students != null) {
            for (Student s : students) {
                cmbStudent.addItem(s.getId() + " - " + s.getName());
            }
        }
    }

    private void refreshCourseCombo() {
        cmbCourse.removeAllItems();
        List<String> courses = controller.getAllCourseCodesWithNames();
        if (courses != null) {
            for (String c : courses) {
                cmbCourse.addItem(c);
            }
        }
    }

    private void refreshEnrollmentTable() {
        tableModel.setRowCount(0);
        List<Object[]> enrollments = controller.getAllEnrollments(); // returns List of [studentId, studentName, courseCode, courseName]
        if (enrollments != null) {
            for (Object[] row : enrollments) {
                tableModel.addRow(row);
            }
        }
    }

    private void enrollStudent() {
        String selectedStudent = (String) cmbStudent.getSelectedItem();
        String selectedCourse = (String) cmbCourse.getSelectedItem();
        if (selectedStudent == null || selectedCourse == null) return;

        String studentId = selectedStudent.split(" - ")[0];
        String courseCode = selectedCourse.split(" - ")[0];
        if (controller.enrollStudent(studentId, courseCode)) {
            JOptionPane.showMessageDialog(this, "Student enrolled successfully");
            refreshEnrollmentTable();
        } else {
            JOptionPane.showMessageDialog(this, "Enrollment failed (duplicate or course full)");
        }
    }
}