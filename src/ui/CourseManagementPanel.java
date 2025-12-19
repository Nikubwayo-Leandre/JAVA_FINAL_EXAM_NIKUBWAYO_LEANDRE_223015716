package ui;

import model.Course;
import service.CourseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CourseManagementPanel extends JPanel {
    private CourseService courseService;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton;
    private JComboBox<String> levelFilter;

    public CourseManagementPanel() {
        courseService = new CourseService();
        initializeUI();
        loadCourseData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header
        JLabel headerLabel = new JLabel("Course Management", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(41, 128, 185));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.setBackground(Color.blue);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        searchField = new JTextField(20);
        searchField.setToolTipText("Search courses by title or description...");

        levelFilter = new JComboBox<>(new String[]{"All Levels", "Beginner", "Intermediate", "Advanced", "Expert"});
        
        addButton = createButton("Add Course", new Color(46, 204, 113));
        editButton = createButton("Edit Course", new Color(52, 152, 219));
        deleteButton = createButton("Delete Course", new Color(231, 76, 60));
        refreshButton = createButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(new JLabel("Search:"));
        toolbarPanel.add(searchField);
        toolbarPanel.add(new JLabel("Level:"));
        toolbarPanel.add(levelFilter);
        toolbarPanel.add(addButton);
        toolbarPanel.add(editButton);
        toolbarPanel.add(deleteButton);
        toolbarPanel.add(refreshButton);

        // Table
        String[] columnNames = {"Course ID", "Title", "Description", "Level", "Instructor ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        courseTable.setFont(new Font("Arial", Font.PLAIN, 12));
        courseTable.setRowHeight(25);
        courseTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(courseTable);
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
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return button;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCourseDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditCourseDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCourse();
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
                searchCourses();
            }
        });
        
        // Add mouse listener for double-click to edit
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showEditCourseDialog();
                }
            }
        });
    }

    public void loadCourseData() {
        tableModel.setRowCount(0);
        List<Course> courses = courseService.getAll();
        
        if (courses != null && !courses.isEmpty()) {
            for (Course course : courses) {
                Object[] rowData = {
                    course.getCourseId(),
                    course.getTitle(),
                    course.getDescription(),
                    course.getLevel(),
                    course.getInstructorId() != null ? course.getInstructorId() : "Not Assigned"
                };
                tableModel.addRow(rowData);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No courses found.", "Information", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchCourses() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        String selectedLevel = (String) levelFilter.getSelectedItem();
        
        tableModel.setRowCount(0);
        List<Course> courses = courseService.getAll();
        
        if (courses != null) {
            for (Course course : courses) {
                // Apply level filter
                if (!"All Levels".equals(selectedLevel) && !course.getLevel().equalsIgnoreCase(selectedLevel)) {
                    continue;
                }
                
                // Apply search filter
                if (!searchTerm.isEmpty()) {
                    boolean matches = course.getTitle().toLowerCase().contains(searchTerm) ||
                                     course.getDescription().toLowerCase().contains(searchTerm);
                    if (!matches) continue;
                }
                
                Object[] rowData = {
                    course.getCourseId(),
                    course.getTitle(),
                    course.getDescription(),
                    course.getLevel(),
                    course.getInstructorId() != null ? course.getInstructorId() : "Not Assigned"
                };
                tableModel.addRow(rowData);
            }
        }
    }

    private void showAddCourseDialog() {
        final JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Course", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        final JTextField titleField = new JTextField(20);
        final JTextArea descriptionArea = new JTextArea(3, 20);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        final JComboBox<String> levelCombo = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced", "Expert"});
        final JTextField instructorIdField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Level*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(levelCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Instructor ID (optional):"), gbc);
        gbc.gridx = 1;
        formPanel.add(instructorIdField, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = createButton("Save", new Color(46, 204, 113));
        JButton cancelButton = createButton("Cancel", new Color(149, 165, 166));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText().trim();
                String description = descriptionArea.getText().trim();
                String level = (String) levelCombo.getSelectedItem();
                String instructorIdStr = instructorIdField.getText().trim();

                if (title.isEmpty() || level == null) {
                    JOptionPane.showMessageDialog(dialog, "Title and Level are required!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    Integer instructorId = null;
                    if (!instructorIdStr.isEmpty()) {
                        instructorId = Integer.parseInt(instructorIdStr);
                    }

                    Course newCourse = courseService.createCourse(title, description, level, instructorId);
                    if (newCourse != null) {
                        JOptionPane.showMessageDialog(CourseManagementPanel.this, 
                            "Course created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        refreshData();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to create course!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Instructor ID must be a valid number!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showEditCourseDialog() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        final Course course = courseService.getById(courseId);

        if (course != null) {
            final JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                "Edit Course", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(450, 350);
            dialog.setLocationRelativeTo(this);

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            final JTextField titleField = new JTextField(course.getTitle(), 20);
            final JTextArea descriptionArea = new JTextArea(course.getDescription(), 3, 20);
            JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
            final JComboBox<String> levelCombo = new JComboBox<>(
                new String[]{"Beginner", "Intermediate", "Advanced", "Expert"});
            levelCombo.setSelectedItem(course.getLevel());
            final JTextField instructorIdField = new JTextField(
                course.getInstructorId() != null ? course.getInstructorId().toString() : "", 20);

            gbc.gridx = 0; gbc.gridy = 0;
            formPanel.add(new JLabel("Title*:"), gbc);
            gbc.gridx = 1;
            formPanel.add(titleField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            formPanel.add(new JLabel("Description:"), gbc);
            gbc.gridx = 1;
            formPanel.add(descriptionScroll, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            formPanel.add(new JLabel("Level*:"), gbc);
            gbc.gridx = 1;
            formPanel.add(levelCombo, gbc);

            gbc.gridx = 0; gbc.gridy = 3;
            formPanel.add(new JLabel("Instructor ID (optional):"), gbc);
            gbc.gridx = 1;
            formPanel.add(instructorIdField, gbc);

            JPanel buttonPanel = new JPanel();
            JButton saveButton = createButton("Save Changes", new Color(46, 204, 113));
            JButton cancelButton = createButton("Cancel", new Color(149, 165, 166));

            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String title = titleField.getText().trim();
                    String description = descriptionArea.getText().trim();
                    String level = (String) levelCombo.getSelectedItem();
                    String instructorIdStr = instructorIdField.getText().trim();

                    if (title.isEmpty() || level == null) {
                        JOptionPane.showMessageDialog(dialog, "Title and Level are required!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    try {
                        Integer instructorId = null;
                        if (!instructorIdStr.isEmpty()) {
                            instructorId = Integer.parseInt(instructorIdStr);
                        }

                        // Update the course object
                        course.setTitle(title);
                        course.setDescription(description);
                        course.setLevel(level);
                        course.setInstructorId(instructorId);
                        
                        boolean updated = courseService.updateCourse(course);
                        if (updated) {
                            JOptionPane.showMessageDialog(CourseManagementPanel.this, 
                                "Course updated successfully!", "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                            refreshData();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Failed to update course!", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Instructor ID must be a valid number!", "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);

            dialog.add(formPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        }
    }

    private void deleteSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete!", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int courseId = (int) tableModel.getValueAt(selectedRow, 0);
        String courseTitle = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete course: " + courseTitle + "?\n" +
            "This action cannot be undone.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = courseService.deleteCourse(courseId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Course deleted successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete course!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshData() {
        loadCourseData();
        searchField.setText("");
        levelFilter.setSelectedIndex(0);
    }
}