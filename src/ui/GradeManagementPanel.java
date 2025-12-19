package ui;

import model.Grade;
import service.GradeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GradeManagementPanel extends JPanel {
    private GradeService gradeService;
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JButton addButton, refreshButton;

    public GradeManagementPanel() {
        gradeService = new GradeService();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Grade Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(41, 128, 185));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addButton = createButton("Record Grade", new Color(46, 204, 113));
        refreshButton = createButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(addButton);
        toolbarPanel.add(refreshButton);

        // Info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(new Color(255, 255, 225));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.YELLOW),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel infoLabel = new JLabel("<html><b>Note:</b> Use this panel to record grades for assignments. " +
                                    "You need the Assignment ID to record a grade.</html>");
        infoPanel.add(infoLabel, BorderLayout.CENTER);

        // Table
        String[] columnNames = {"Grade ID", "Score", "Grade", "Comments", "Assignment ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gradeTable = new JTable(tableModel);
        gradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gradeTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        gradeTable.setFont(new Font("Arial", Font.PLAIN, 12));
        gradeTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(gradeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to panel
        add(headerLabel, BorderLayout.NORTH);
        add(toolbarPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
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
                showAddGradeDialog();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });
    }

    private void showAddGradeDialog() {
        JTextField assignmentIdField = new JTextField();
        JTextField scoreField = new JTextField();
        JTextField gradeField = new JTextField();
        JTextField commentsField = new JTextField();

        Object[] message = {
            "Assignment ID:", assignmentIdField,
            "Score:", scoreField,
            "Grade Letter:", gradeField,
            "Comments:", commentsField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Record Grade", 
                                                 JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int assignmentId = Integer.parseInt(assignmentIdField.getText().trim());
                String score = scoreField.getText().trim();
                String gradeLetter = gradeField.getText().trim();
                String comments = commentsField.getText().trim();

                if (!score.isEmpty() && !gradeLetter.isEmpty()) {
                    Grade grade = gradeService.recordGrade(assignmentId, score, gradeLetter, comments);
                    if (grade != null) {
                        refreshData();
                        // Add the new grade to the table
                        Object[] rowData = {
                            grade.getGradeId(),
                            grade.getScore(),
                            grade.getGradeLetter(),
                            grade.getComments(),
                            grade.getAssignmentId()
                        };
                        tableModel.addRow(rowData);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Score and Grade are required!", "Error", 
                                                JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid Assignment ID!", "Error", 
                                            JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        // Clear the table since we don't have a getAll method for grades
        tableModel.setRowCount(0);
        JOptionPane.showMessageDialog(this, 
            "Grade data refreshed. Note: Individual grades are typically viewed by assignment.", 
            "Refresh Complete", JOptionPane.INFORMATION_MESSAGE);
    }
}