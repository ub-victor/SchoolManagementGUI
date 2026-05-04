package com.school.view;

import com.school.controller.SchoolController;
import com.school.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentPanel extends JPanel {
    private SchoolController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtEmail, txtFees;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public StudentPanel(SchoolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Name", "Email", "Total Fees", "Paid", "Remaining"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel (south)
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        formPanel.add(new JLabel("ID:"));
        txtId = new JTextField();
        formPanel.add(txtId);
        formPanel.add(new JLabel("Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Total Fees:"));
        txtFees = new JTextField();
        formPanel.add(txtFees);

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

        // Add action listeners
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnRefresh.addActionListener(e -> refreshTable());

        // Load data
        refreshTable();

        // When a row is selected, fill the form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 2).toString());
                    txtFees.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Student> students = controller.getAllStudents();
        if (students != null) {
            for (Student s : students) {
                double paid = controller.getFeesPaid(s.getId());
                double remaining = s.getTotalFees() - paid;
                Object[] row = {
                    s.getId(), s.getName(), s.getEmail(),
                    s.getTotalFees(), paid, remaining
                };
                tableModel.addRow(row);
            }
        }
    }

    private void addStudent() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        double fees;
        try {
            fees = Double.parseDouble(txtFees.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid fee amount");
            return;
        }
        Student s = new Student(id, name, email, fees);
        if (controller.addStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student added");
            refreshTable();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Add failed (maybe duplicate ID/email)");
        }
    }

    private void updateStudent() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;
        Student s = new Student(id, txtName.getText().trim(), txtEmail.getText().trim(),
                Double.parseDouble(txtFees.getText().trim()));
        if (controller.updateStudent(s)) {
            JOptionPane.showMessageDialog(this, "Student updated");
            refreshTable();
        }
    }

    private void deleteStudent() {
        String id = txtId.getText().trim();
        if (id.isEmpty()) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete student " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteStudent(id)) {
                JOptionPane.showMessageDialog(this, "Student deleted");
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
        txtFees.setText("");
    }
}