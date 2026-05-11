package com.school.view;

import com.school.controller.SchoolController;
import javax.swing.*;

public class MainFrame extends JFrame {
    private SchoolController controller;

    public MainFrame() {
        controller = new SchoolController();
        setTitle("School Management System - MVC + MySQL");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students", new StudentPanel(controller));
        tabbedPane.addTab("Teachers", new TeacherPanel(controller));
        tabbedPane.addTab("Courses", new CoursePanel(controller));
        tabbedPane.addTab("Enrollments", new EnrollmentPanel(controller));
        tabbedPane.addTab("Fee Payments", new PaymentPanel(controller)); 

        add(tabbedPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}