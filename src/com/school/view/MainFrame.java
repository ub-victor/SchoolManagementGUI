package com.school.view;

import javax.swing.*;
import com.school.controller.SchoolController;

public class MainFrame extends JFrame {
    private SchoolController controller;

    public MainFrame() {
        controller = new SchoolController();
        setTitle("School Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students", new StudentPanel(controller));
        tabbedPane.addTab("Teachers", new TeacherPanel(controller));
        tabbedPane.addTab("Courses", new CoursePanel(controller));
        tabbedPane.addTab("Enrollments", new EnrollmentPanel(controller));
        tabbedPane.addTab("Pay Fees", new PaymentPanel(controller));

        add(tabbedPane);
        setVisible(true);
    }
}