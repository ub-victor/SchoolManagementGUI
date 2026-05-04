package com.school.view;

import com.school.controller.SchoolController;
import com.school.model.Teacher;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TeacherPanel extends JPanel {
    private SchoolController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtEmail, txtSpecialization, txtSalary;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public TeacherPanel(SchoolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Table columns
        String[] columns = {"ID", "Name", "Email", "Specialization", "Salary"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Teacher Details"));
        formPanel.add(new JLabel("ID:"));
        txtId = new JTextField();
        formPanel.add(txtId);
        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Specialization:"));
        txtSpecialization = new JTextField();
        formPanel.add(txtSpecialization);
        formPanel.add(new JLabel("Salary:"));
        txtSalary = new JTextField();
        formPanel.add(txtSalary);

        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        btnAdd.addActionListener(e -> addTeacher());
        btnUpdate.addActionListener(e -> updateTeacher());
        btnDelete.addActionListener(e -> deleteTeacher());
        btnRefresh.addActionListener(e -> refreshTable());

        refreshTable();

        // Fill form on row selection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    txtSpecialization.setText(tableModel.getValueAt(row, 3).toString());
                    txtSalary.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Teacher> teachers = controller.getAllTeachers();
        if (teachers != null) {
            for (Teacher t : teachers) {
                Object[] row = {t.getId(), t.getName(), t.getEmail(),
                        t.getSpecialization(), t.getSalary()};
                tableModel.addRow(row);
            }
        }
    }

    private void addTeacher() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String spec = txtSpecialization.getText().trim();
        String salaryText = txtSalary.getText().trim();
        if (id.isEmpty() || name.isEmpty() || email.isEmpty() || spec.isEmpty() || salaryText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All teacher fields are required");
            return;
        }
        double salary;
        try {
            salary = Double.parseDouble(salaryText);
            if (salary < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary");
            return;
        }
        Teacher t = new Teacher(id, name, email, spec, salary);
        if (controller.addTeacher(t)) {
            JOptionPane.showMessageDialog(this, "Teacher added");
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Add failed (duplicate ID or email)");
        }
    }

    private void updateTeacher() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;
        Teacher t = new Teacher(id, txtName.getText().trim(), txtEmail.getText().trim(),
                txtSpecialization.getText().trim(),
                Double.parseDouble(txtSalary.getText().trim()));
        if (controller.updateTeacher(t)) {
            JOptionPane.showMessageDialog(this, "Teacher updated");
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed");
        }
    }

    private void deleteTeacher() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete teacher " + id + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteTeacher(id)) {
                JOptionPane.showMessageDialog(this, "Teacher deleted");
                refreshTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Delete failed");
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtSpecialization.setText("");
        txtSalary.setText("");
    }
}