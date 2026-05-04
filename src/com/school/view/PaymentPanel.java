package com.school.view;

import com.school.controller.SchoolController;
import com.school.model.Student;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PaymentPanel extends JPanel {
    private SchoolController controller;
    private JComboBox<String> cmbStudent;
    private JTextField txtAmount;
    private JButton btnPay, btnRefresh;
    private JLabel lblTotalFees, lblPaid, lblRemaining;
    private JTable paymentTable;
    private DefaultTableModel tableModel;

    public PaymentPanel(SchoolController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());

        // Payment form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Make Payment"));
        formPanel.add(new JLabel("Select Student:"));
        cmbStudent = new JComboBox<>();
        formPanel.add(cmbStudent);
        formPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        formPanel.add(txtAmount);

        btnPay = new JButton("Pay Fees");
        btnRefresh = new JButton("Refresh");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnPay);
        buttonPanel.add(btnRefresh);

        // Fee summary
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Fee Summary"));
        lblTotalFees = new JLabel("Total Fees: $");
        lblPaid = new JLabel("Paid: $");
        lblRemaining = new JLabel("Remaining: $");
        summaryPanel.add(lblTotalFees);
        summaryPanel.add(lblPaid);
        summaryPanel.add(lblRemaining);

        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.add(summaryPanel, BorderLayout.NORTH);
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        // Payment history table
        String[] columns = {"Payment ID", "Amount", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        paymentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(paymentTable);
        add(scrollPane, BorderLayout.CENTER);

        // Event listeners
        cmbStudent.addActionListener(e -> updateFeeSummary());
        btnPay.addActionListener(e -> makePayment());
        btnRefresh.addActionListener(e -> {
            refreshStudentCombo();
            updateFeeSummary();
            refreshPaymentHistory();
        });

        refreshStudentCombo();
    }

    private void refreshStudentCombo() {
        cmbStudent.removeAllItems();
        List<Student> students = controller.getAllStudents();
        if (students != null) {
            for (Student s : students) {
                cmbStudent.addItem(s.getId() + " - " + s.getName());
            }
        }
        if (cmbStudent.getItemCount() > 0) updateFeeSummary();
    }

    private void updateFeeSummary() {
        String selected = (String) cmbStudent.getSelectedItem();
        if (selected == null) return;
        String studentId = selected.split(" - ")[0];
        Student s = controller.getStudent(studentId);
        if (s != null) {
            double paid = controller.getFeesPaid(studentId);
            double total = s.getTotalFees();
            double remaining = total - paid;
            lblTotalFees.setText("Total Fees: $" + total);
            lblPaid.setText("Paid: $" + paid);
            lblRemaining.setText("Remaining: $" + remaining);
            refreshPaymentHistory();
        }
    }

    private void refreshPaymentHistory() {
        tableModel.setRowCount(0);
        String selected = (String) cmbStudent.getSelectedItem();
        if (selected == null) return;
        String studentId = selected.split(" - ")[0];
        List<Object[]> payments = controller.getPaymentsByStudent(studentId); // returns [id, amount, date]
        if (payments != null) {
            for (Object[] p : payments) {
                tableModel.addRow(p);
            }
        }
    }

    private void makePayment() {
        String selected = (String) cmbStudent.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a student");
            return;
        }
        String studentId = selected.split(" - ")[0];
        double amount;
        try {
            amount = Double.parseDouble(txtAmount.getText().trim());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid positive amount");
            return;
        }
        if (controller.payFees(studentId, amount)) {
            JOptionPane.showMessageDialog(this, "Payment recorded");
            updateFeeSummary();
            txtAmount.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed (amount exceeds remaining fees)");
        }
    }
}