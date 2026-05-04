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

        // Top panel: student selection and payment
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder("Make Payment"));
        topPanel.add(new JLabel("Select Student:"));
        cmbStudent = new JComboBox<>();
        topPanel.add(cmbStudent);
        topPanel.add(new JLabel("Amount:"));
        txtAmount = new JTextField();
        topPanel.add(txtAmount);
        btnPay = new JButton("Pay Fees");
        btnRefresh = new JButton("Refresh");
        topPanel.add(btnPay);
        topPanel.add(btnRefresh);

        // Middle panel: fee summary
        JPanel summaryPanel = new JPanel(new FlowLayout());
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Fee Summary"));
        lblTotalFees = new JLabel("Total Fees: ");
        lblPaid = new JLabel("Paid: ");
        lblRemaining = new JLabel("Remaining: ");
        summaryPanel.add(lblTotalFees);
        summaryPanel.add(lblPaid);
        summaryPanel.add(lblRemaining);
        topPanel.add(summaryPanel); // Actually we add after? Let's restructure cleanly:

        // Better layout: top for payment form, then summary, then table of payment history
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.add(new JLabel("Student:"));
        formPanel.add(cmbStudent);
        formPanel.add(new JLabel("Payment Amount:"));
        formPanel.add(txtAmount);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnPay);
        buttonPanel.add(btnRefresh);
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(formPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        northPanel.add(summaryPanel, BorderLayout.NORTH); // summary above form

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