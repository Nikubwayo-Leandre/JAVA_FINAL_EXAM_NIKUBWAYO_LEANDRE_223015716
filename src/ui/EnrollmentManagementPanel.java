package ui;

import model.Enrollment;
import service.EnrollmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EnrollmentManagementPanel extends JPanel {
    private EnrollmentService enrollmentService;
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, refreshButton;

    public EnrollmentManagementPanel() {
        enrollmentService = new EnrollmentService();
        initializeUI();
        loadEnrollmentData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Enrollment Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(41, 128, 185));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = createButton("Enroll Student", new Color(46, 204, 113));
        refreshButton = createButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(addButton);
        toolbarPanel.add(refreshButton);

        // Table
        String[] columnNames = {"ID", "Reference", "Student ID", "Course ID", "Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrollmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        enrollmentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        enrollmentTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to panel
        add(headerLabel, BorderLayout.NORTH);
        add(toolbarPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Event handlers
        setupEventHandlers();
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddEnrollmentDialog();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
    }

    private void loadEnrollmentData() {
        tableModel.setRowCount(0); // Clear existing data
        List<Enrollment> enrollments = enrollmentService.getAll();
        
        for (Enrollment enrollment : enrollments) {
            Object[] rowData = {
                enrollment.getEnrollmentId(),
                enrollment.getReferenceId(),
                enrollment.getStudentId(),
                enrollment.getCourseId(),
                enrollment.getStatus(),
                enrollment.getDate() != null ? enrollment.getDate().toString() : "N/A"
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddEnrollmentDialog() {
        JTextField studentIdField = new JTextField();
        JTextField courseIdField = new JTextField();

        Object[] message = {
            "Student ID:", studentIdField,
            "Course ID:", courseIdField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Enroll Student in Course", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                int courseId = Integer.parseInt(courseIdField.getText().trim());

                Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
                if (enrollment != null) {
                    refreshData();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric IDs!", "Error", 
                                            JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadEnrollmentData();
    }
}