package ui;

import model.Instructor;
import service.InstructorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InstructorManagementPanel extends JPanel {
    private InstructorService instructorService;
    private JTable instructorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private JComboBox<String> statusFilter;

    public InstructorManagementPanel() {
        instructorService = new InstructorService();
        initializeUI();
        loadInstructorData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.yellow);

        // Header
        JLabel headerLabel = new JLabel("Instructor Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(41, 128, 185));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.yellow);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField(20);
        searchField.setToolTipText("Search instructors by name or identifier...");

        statusFilter = new JComboBox<>(new String[]{"All Status", "Active", "Inactive"});
        
        addButton = createButton("Add Instructor", new Color(46, 204, 113));
        editButton = createButton("Edit Instructor", new Color(52, 152, 219));
        deleteButton = createButton("Delete Instructor", new Color(231, 76, 60));
        refreshButton = createButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(new JLabel("Search:"));
        toolbarPanel.add(searchField);
        toolbarPanel.add(new JLabel("Status:"));
        toolbarPanel.add(statusFilter);
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(refreshButton);

        // Table
        String[] columnNames = {"ID", "Name", "Identifier", "Status", "Location", "Contact", "Assigned Since"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        instructorTable = new JTable(tableModel);
        instructorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instructorTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        instructorTable.setFont(new Font("Arial", Font.PLAIN, 12));
        instructorTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(instructorTable);
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
                showAddInstructorDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditInstructorDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedInstructor();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        // Search functionality
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchInstructors();
            }
        });
    }

    public void loadInstructorData() {
        tableModel.setRowCount(0);
        List<Instructor> instructors = instructorService.getAll(); // This should work now
        
        if (instructors != null) {
            for (Instructor instructor : instructors) {
                Object[] rowData = {
                    instructor.getInstructorId(),
                    instructor.getName(),
                    instructor.getIdentifier(),
                    instructor.getStatus(),
                    instructor.getLocation(),
                    instructor.getContact(),
                    instructor.getAssignedSince() != null ? instructor.getAssignedSince().toString() : "N/A"
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void searchInstructors() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        List<Instructor> instructors = instructorService.getAll();
        
        if (instructors != null) {
            for (Instructor instructor : instructors) {
                // Apply status filter
                if (!selectedStatus.equals("All Status") && !instructor.getStatus().equalsIgnoreCase(selectedStatus)) {
                    continue;
                }
                
                // Apply search filter
                if (!searchTerm.isEmpty()) {
                    boolean matches = instructor.getName().toLowerCase().contains(searchTerm) ||
                                     instructor.getIdentifier().toLowerCase().contains(searchTerm) ||
                                     (instructor.getLocation() != null && instructor.getLocation().toLowerCase().contains(searchTerm));
                    if (!matches) continue;
                }
                
                Object[] rowData = {
                    instructor.getInstructorId(),
                    instructor.getName(),
                    instructor.getIdentifier(),
                    instructor.getStatus(),
                    instructor.getLocation(),
                    instructor.getContact(),
                    instructor.getAssignedSince() != null ? instructor.getAssignedSince().toString() : "N/A"
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void showAddInstructorDialog() {
        final JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Instructor", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JTextField nameField = new JTextField();
        final JTextField identifierField = new JTextField();
        final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
        final JTextField locationField = new JTextField();
        final JTextField contactField = new JTextField();
        final JTextField assignedSinceField = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Identifier:"));
        formPanel.add(identifierField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusCombo);
        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);
        formPanel.add(new JLabel("Contact:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Assigned Since (YYYY-MM-DD):"));
        formPanel.add(assignedSinceField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String identifier = identifierField.getText().trim();
                
                if (!name.isEmpty() && !identifier.isEmpty()) {
                    String status = (String) statusCombo.getSelectedItem();
                    String location = locationField.getText().trim();
                    String contact = contactField.getText().trim();
                    String dateStr = assignedSinceField.getText().trim();
                    
                    LocalDate assignedSince = null;
                    if (!dateStr.isEmpty()) {
                        try {
                            assignedSince = LocalDate.parse(dateStr);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    
                    Instructor instructor = instructorService.createInstructorWithDetails(name, identifier, status, location, contact, assignedSince);
                    if (instructor != null) {
                        dialog.dispose();
                        refreshData();
                        JOptionPane.showMessageDialog(InstructorManagementPanel.this, "Instructor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to add instructor. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(dialog, "Name and Identifier are required!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditInstructorDialog() {
        int selectedRow = instructorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an instructor to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int instructorId = (int) tableModel.getValueAt(selectedRow, 0);
        final Instructor instructor = instructorService.getById(instructorId);

        if (instructor != null) {
            final JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Instructor", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(this);

            JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            final JTextField nameField = new JTextField(instructor.getName());
            final JTextField identifierField = new JTextField(instructor.getIdentifier());
            final JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
            statusCombo.setSelectedItem(instructor.getStatus());
            final JTextField locationField = new JTextField(instructor.getLocation() != null ? instructor.getLocation() : "");
            final JTextField contactField = new JTextField(instructor.getContact() != null ? instructor.getContact() : "");
            final JTextField assignedSinceField = new JTextField(instructor.getAssignedSince() != null ? instructor.getAssignedSince().toString() : "");

            formPanel.add(new JLabel("Name:"));
            formPanel.add(nameField);
            formPanel.add(new JLabel("Identifier:"));
            formPanel.add(identifierField);
            formPanel.add(new JLabel("Status:"));
            formPanel.add(statusCombo);
            formPanel.add(new JLabel("Location:"));
            formPanel.add(locationField);
            formPanel.add(new JLabel("Contact:"));
            formPanel.add(contactField);
            formPanel.add(new JLabel("Assigned Since (YYYY-MM-DD):"));
            formPanel.add(assignedSinceField);

            JButton saveButton = new JButton("Save Changes");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText().trim();
                    String identifier = identifierField.getText().trim();
                    
                    if (!name.isEmpty() && !identifier.isEmpty()) {
                        instructor.setName(name);
                        instructor.setIdentifier(identifier);
                        instructor.setStatus((String) statusCombo.getSelectedItem());
                        instructor.setLocation(locationField.getText().trim());
                        instructor.setContact(contactField.getText().trim());
                        
                        String dateStr = assignedSinceField.getText().trim();
                        if (!dateStr.isEmpty()) {
                            try {
                                instructor.setAssignedSince(LocalDate.parse(dateStr));
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(dialog, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            instructor.setAssignedSince(null);
                        }
                        
                        boolean success = instructorService.updateInstructor(instructor);
                        if (success) {
                            dialog.dispose();
                            refreshData();
                            JOptionPane.showMessageDialog(InstructorManagementPanel.this, "Instructor updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Failed to update instructor. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Name and Identifier are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }
    }

    private void deleteSelectedInstructor() {
        int selectedRow = instructorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an instructor to delete!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int instructorId = (int) tableModel.getValueAt(selectedRow, 0);
        String instructorName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete instructor: " + instructorName + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = instructorService.deleteInstructor(instructorId);
            if (success) {
                refreshData();
                JOptionPane.showMessageDialog(this, "Instructor deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete instructor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadInstructorData();
    }
}