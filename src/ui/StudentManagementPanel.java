package ui;

import model.Student;
import service.StudentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentManagementPanel extends JPanel {
    private StudentService studentService;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton;

    public StudentManagementPanel() {
        studentService = new StudentService();
        initializeUI();
        loadStudentData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.red);

        // Header
        JLabel headerLabel = new JLabel("Student Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(41, 128, 185));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.red);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField(20);
        searchField.setToolTipText("Search students by name or email...");

        addButton = createButton("Add Student", new Color(46, 204, 113));
        editButton = createButton("Edit Student", new Color(52, 152, 219));
        deleteButton = createButton("Delete Student", new Color(231, 76, 60));
        refreshButton = createButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(new JLabel("Search:"));
        toolbarPanel.add(searchField);
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(refreshButton);

        // Table
        String[] columnNames = {"ID", "Name", "Email", "Major", "Created At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(studentTable);
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
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddStudentDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditStudentDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedStudent();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
    }

    public void loadStudentData() {
        tableModel.setRowCount(0); // Clear existing data
        List<Student> students = studentService.getAllStudents();
        
        for (Student student : students) {
            Object[] rowData = {
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getMajor(),
                "2024-01-01" // You can add actual created date from database
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddStudentDialog() {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField majorField = new JTextField();

        Object[] message = {
            "Name:", nameField,
            "Email:", emailField,
            "Major:", majorField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Student", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String major = majorField.getText().trim();

            if (!name.isEmpty() && !email.isEmpty() && !major.isEmpty()) {
                Student student = studentService.createStudent(name, email, major);
                if (student != null) {
                    refreshData();
                    JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", 
                                                JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", 
                                            JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditStudentDialog() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to edit!", "No Selection", 
                                        JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        Student student = studentService.getStudentById(studentId);

        if (student != null) {
            JTextField nameField = new JTextField(student.getName());
            JTextField emailField = new JTextField(student.getEmail());
            JTextField majorField = new JTextField(student.getMajor());

            Object[] message = {
                "Name:", nameField,
                "Email:", emailField,
                "Major:", majorField
            };

            int option = JOptionPane.showConfirmDialog(this, message, "Edit Student", 
                                                     JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String major = majorField.getText().trim();

                if (!name.isEmpty() && !email.isEmpty() && !major.isEmpty()) {
                    boolean success = studentService.updateStudent(studentId, name, email, major);
                    if (success) {
                        refreshData();
                        JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", 
                                                    JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", 
                                                JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", "No Selection", 
                                        JOptionPane.WARNING_MESSAGE);
            return;
        }

        int studentId = (int) tableModel.getValueAt(selectedRow, 0);
        String studentName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete student: " + studentName + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = studentService.deleteStudent(studentId);
            if (success) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Student deleted successfully!", "Success", 
                                            JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadStudentData();
    }
}