package ui;

import model.Assignment;
import service.AssignmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssignmentManagementPanel extends JPanel {
    private AssignmentService assignmentService;
    private JTable assignmentTable;
    private DefaultTableModel tableModel;
    private JButton addButton, refreshButton;

    public AssignmentManagementPanel() {
        assignmentService = new AssignmentService();
        initializeUI();
        loadAssignmentData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Assignment Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(41, 128, 185));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = createButton("Create Assignment", new Color(46, 204, 113));
        refreshButton = createButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(addButton);
        toolbarPanel.add(refreshButton);

        // Table
        String[] columnNames = {"ID", "Reference", "Description", "Enrollment ID", "Status", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        assignmentTable = new JTable(tableModel);
        assignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        assignmentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        assignmentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        assignmentTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(assignmentTable);
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
        addButton.addActionListener(e -> showAddAssignmentDialog());
        refreshButton.addActionListener(e -> refreshData());
    }

    private void loadAssignmentData() {
        tableModel.setRowCount(0); // Clear existing data
        List<Assignment> assignments = assignmentService.getAll();
        
        for (Assignment assignment : assignments) {
            Object[] rowData = {
                assignment.getAssignmentId(),
                assignment.getReferenceId(),
                assignment.getDescription(),
                assignment.getEnrollmentId(),
                assignment.getStatus(),
                assignment.getDate() != null ? assignment.getDate().toString() : "N/A"
            };
            tableModel.addRow(rowData);
        }
    }

    private void showAddAssignmentDialog() {
        JTextField enrollmentIdField = new JTextField();
        JTextField referenceField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] message = {
            "Enrollment ID:", enrollmentIdField,
            "Reference:", referenceField,
            "Description:", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Create Assignment", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int enrollmentId = Integer.parseInt(enrollmentIdField.getText().trim());
                String reference = referenceField.getText().trim();
                String description = descriptionField.getText().trim();

                if (!reference.isEmpty() && !description.isEmpty()) {
                    Assignment assignment = assignmentService.createAssignment(enrollmentId, reference, description);
                    if (assignment != null) {
                        refreshData();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", 
                                                JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid Enrollment ID!", "Error", 
                                            JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadAssignmentData();
    }
}
