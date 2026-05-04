package com.school.view;

import com.school.controller.SchoolController;
import com.school.model.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    private SchoolController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCode, txtName, txtCapacity;
    private JComboBox<String> cmbTeacher; // drop-down for teacher IDs
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnAssignTeacher;

    public CoursePanel(SchoolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Table columns
        String[] columns = {"Code", "Name", "Max Capacity", "Teacher ID"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        formPanel.add(new JLabel("Code:"));
        txtCode = new JTextField();
        formPanel.add(txtCode);
        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        formPanel.add(new JLabel("Max Capacity:"));
        txtCapacity = new JTextField();
        formPanel.add(txtCapacity);
        formPanel.add(new JLabel("Teacher ID:"));
        cmbTeacher = new JComboBox<>();
        formPanel.add(cmbTeacher);

        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnAssignTeacher = new JButton("Assign Teacher");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAssignTeacher);
        buttonPanel.add(btnRefresh);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load teacher IDs into combo box
        refreshTeacherCombo();

        // Action listeners
        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnRefresh.addActionListener(e -> refreshTable());
        btnAssignTeacher.addActionListener(e -> assignTeacherToCourse());

        refreshTable();

        // Fill form on row selection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtCode.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtCapacity.setText(tableModel.getValueAt(row, 2).toString());
                    String teacherId = tableModel.getValueAt(row, 3).toString();
                    if (teacherId != null && !teacherId.isEmpty())
                        cmbTeacher.setSelectedItem(teacherId);
                    else
                        cmbTeacher.setSelectedIndex(0);
                }
            }
        });
    }

    private void refreshTeacherCombo() {
        cmbTeacher.removeAllItems();
        cmbTeacher.addItem(""); // empty selection
        List<String> teacherIds = controller.getAllTeacherIds();
        if (teacherIds != null) {
            for (String id : teacherIds) {
                cmbTeacher.addItem(id);
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Course> courses = controller.getAllCourses();
        if (courses != null) {
            for (Course c : courses) {
                Object[] row = {c.getCode(), c.getName(), c.getMaxCapacity(), c.getTeacherId()};
                tableModel.addRow(row);
            }
        }
    }

    private void addCourse() {
        String code = txtCode.getText().trim();
        String name = txtName.getText().trim();
        int capacity;
        try {
            capacity = Integer.parseInt(txtCapacity.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid capacity");
            return;
        }
        String teacherId = (String) cmbTeacher.getSelectedItem();
        if (teacherId != null && teacherId.isEmpty()) teacherId = null;
        Course c = new Course(code, name, capacity, teacherId);
        if (controller.addCourse(c)) {
            JOptionPane.showMessageDialog(this, "Course added");
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Add failed (duplicate code)");
        }
    }

    private void updateCourse() {
        String code = txtCode.getText().trim();
        if (code.isEmpty()) return;
        Course c = new Course(code, txtName.getText().trim(),
                Integer.parseInt(txtCapacity.getText().trim()),
                (String) cmbTeacher.getSelectedItem());
        if (controller.updateCourse(c)) {
            JOptionPane.showMessageDialog(this, "Course updated");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
    }

    private void deleteCourse() {
        String code = txtCode.getText().trim();
        if (code.isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete course " + code + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteCourse(code)) {
                JOptionPane.showMessageDialog(this, "Course deleted");
                refreshTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed");
            }
        }
    }

    private void assignTeacherToCourse() {
        String courseCode = txtCode.getText().trim();
        String teacherId = (String) cmbTeacher.getSelectedItem();
        if (courseCode.isEmpty() || teacherId == null || teacherId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a course and teacher");
            return;
        }
        if (controller.assignTeacherToCourse(courseCode, teacherId)) {
            JOptionPane.showMessageDialog(this, "Teacher assigned");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Assignment failed");
        }
    }

    private void clearForm() {
        txtCode.setText("");
        txtName.setText("");
        txtCapacity.setText("");
        cmbTeacher.setSelectedIndex(0);
    }
}