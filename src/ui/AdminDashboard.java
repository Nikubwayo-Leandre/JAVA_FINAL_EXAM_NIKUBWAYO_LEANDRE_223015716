package ui;

import model.Admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdminDashboard extends JFrame {
    private JTabbedPane tabbedPane;
    private StudentManagementPanel studentPanel;
    private InstructorManagementPanel instructorPanel;
    private CourseManagementPanel coursePanel;
    private Admin currentAdmin;
    
    // Professional Color Scheme
    private static final Color PRIMARY_DARK = new Color(34, 49, 63);    // Dark Blue
    private static final Color PRIMARY_COLOR = new Color(52, 73, 94);   // Blue Gray
    private static final Color SECONDARY_COLOR = new Color(44, 130, 201); // Bright Blue
    private static final Color ACCENT_COLOR = new Color(26, 188, 156);  // Teal
    private static final Color BACKGROUND_COLOR = new Color(247, 249, 252); // Light Gray-Blue
    private static final Color CARD_BACKGROUND = Color.white;           // White for cards
    private static final Color HOVER_COLOR = new Color(245, 248, 250);  // Light Blue Hover
    private static final Color BORDER_COLOR = new Color(230, 236, 240); // Light Border
    private static final Color TEXT_PRIMARY = new Color(44, 62, 80);    // Dark Blue Text
    private static final Color TEXT_SECONDARY = new Color(127, 140, 141); // Gray Text
    private static final Color TEXT_LIGHT = new Color(236, 240, 241);   // Light Text for dark backgrounds

    public AdminDashboard(Admin admin) {
        this.currentAdmin = admin;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Education Automation Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(BACKGROUND_COLOR);

        createMenuBar();
        createTabbedPane();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(PRIMARY_COLOR);
        menuBar.setBorder(BorderFactory.createLineBorder(PRIMARY_DARK));

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setForeground(Color.black);
        fileMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JMenuItem refreshItem = createMenuItem("Refresh All", "🔄");
        JMenuItem logoutItem = createMenuItem("Logout", "🚪");
        JMenuItem exitItem = createMenuItem("Exit", "🚫");

        refreshItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAllData();
            }
        });

        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        viewMenu.setForeground(Color.WHITE);
        viewMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JMenuItem dashboardItem = createMenuItem("Dashboard", "📊");
        JMenuItem studentsItem = createMenuItem("Students", "👨🎓");
        JMenuItem instructorsItem = createMenuItem("Instructors", "👨🏫");
        JMenuItem coursesItem = createMenuItem("Courses", "📚");

        dashboardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(0);
            }
        });

        studentsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabbedPane.setSelectedIndex(1);
            }
        });

        instructorsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabbedPane.getTabCount() > 2) {
                    tabbedPane.setSelectedIndex(2);
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Instructors panel not available yet",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        coursesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabbedPane.getTabCount() > 3) {
                    tabbedPane.setSelectedIndex(3);
                } else {
                    JOptionPane.showMessageDialog(AdminDashboard.this,
                        "Courses panel not available yet",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        viewMenu.add(dashboardItem);
        viewMenu.add(studentsItem);
        viewMenu.add(instructorsItem);
        viewMenu.add(coursesItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setForeground(Color.black);
        helpMenu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        JMenuItem aboutItem = createMenuItem("About", "ℹ️");

        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
    
    private JMenuItem createMenuItem(String text, String icon) {
        JMenuItem item = new JMenuItem(icon + " " + text);
        item.setForeground(TEXT_PRIMARY);
        item.setBackground(Color.WHITE);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return item;
    }

    private void createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setForeground(TEXT_PRIMARY);

        // Initialize panels
        studentPanel = new StudentManagementPanel();
        instructorPanel = new InstructorManagementPanel();
        coursePanel = new CourseManagementPanel();

        // Add tabs
        tabbedPane.addTab("📊 Dashboard", createDashboardPanel());
        tabbedPane.addTab("👨🎓 Students", studentPanel);
        tabbedPane.addTab("👨🏫 Instructors", instructorPanel);
        tabbedPane.addTab("📚 Courses", coursePanel);

        // Setup tab colors for better visibility
        setupTabColors();

        add(tabbedPane);
    }

    private void setupTabColors() {
        // Set initial tab colors
        updateTabColors();
        
        // Add listener to update colors when tabs change
        tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                updateTabColors();
            }
        });
    }

    private void updateTabColors() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        int tabCount = tabbedPane.getTabCount();
        
        // Colors for each tab - using the professional color scheme
        Color[] tabColors = {
            SECONDARY_COLOR,    // Dashboard - Bright Blue
            new Color(46, 204, 113),    // Students - Green
            new Color(241, 196, 15),    // Instructors - Yellow/Gold
            new Color(155, 89, 182)     // Courses - Purple
        };
        
        // Set colors for all tabs
        for (int i = 0; i < tabCount; i++) {
            if (i == selectedIndex) {
                // Selected tab - bright color
                tabbedPane.setBackgroundAt(i, tabColors[i % tabColors.length]);
                tabbedPane.setForegroundAt(i, Color.black);
            } else {
                // Unselected tabs - light background, dark text
                tabbedPane.setBackgroundAt(i, new Color(240, 245, 250));
                tabbedPane.setForegroundAt(i, TEXT_SECONDARY);
            }
        }
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(BACKGROUND_COLOR);

        // Header Section
        JPanel headerPanel = createHeaderPanel();
        
        // Stats Section
        JPanel statsPanel = createStatsPanel();
        
        // Quick Actions Section
        JPanel quickActionsPanel = createQuickActionsPanel();

        dashboardPanel.add(headerPanel, BorderLayout.NORTH);
        dashboardPanel.add(statsPanel, BorderLayout.CENTER);
        dashboardPanel.add(quickActionsPanel, BorderLayout.SOUTH);

        return dashboardPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_DARK); // Dark blue gradient effect
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel titleLabel = new JLabel("Education Automation Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleLabel.setForeground(TEXT_LIGHT); // Changed to light text color

        JLabel welcomeLabel = new JLabel("Welcome, " + currentAdmin.getFullName() + "! (" + currentAdmin.getRole() + ")", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        welcomeLabel.setForeground(new Color(200, 210, 220)); // Light gray-blue

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        infoPanel.setOpaque(false);
        
        JLabel dateLabel = new JLabel("📅 Today: " + new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        dateLabel.setForeground(new Color(180, 190, 200));
        infoPanel.add(dateLabel);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(welcomeLabel, BorderLayout.SOUTH);
        headerPanel.add(infoPanel, BorderLayout.NORTH);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 30, 30));
        statsPanel.setBackground(BACKGROUND_COLOR);

        statsPanel.add(createStatCard("Total Students", "150", SECONDARY_COLOR)); // Bright Blue
        statsPanel.add(createStatCard("Total Instructors", "25", ACCENT_COLOR)); // Teal
        statsPanel.add(createStatCard("Active Courses", "45", new Color(155, 89, 182))); // Purple
        statsPanel.add(createStatCard("Enrollments", "1,234", new Color(241, 196, 15))); // Yellow
        statsPanel.add(createStatCard("Assignments", "567", new Color(230, 126, 34))); // Orange
        statsPanel.add(createStatCard("Avg Grade", "85%", new Color(231, 76, 60))); // Red

        return statsPanel;
    }

    private JPanel createQuickActionsPanel() {
        JPanel quickActionsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        quickActionsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 50, 30));
        quickActionsPanel.setBackground(BACKGROUND_COLOR);

        // Only create actions for tabs that actually exist
        quickActionsPanel.add(createQuickActionCard("Add Student", "👨🎓", 1, SECONDARY_COLOR)); // Students tab
        quickActionsPanel.add(createQuickActionCard("Manage Instructors", "👨🏫", 2, ACCENT_COLOR)); // Instructors tab
        quickActionsPanel.add(createQuickActionCard("Manage Courses", "📚", 3, new Color(155, 89, 182))); // Courses tab
        quickActionsPanel.add(createQuickActionCard("System Info", "⚙️", -1, PRIMARY_COLOR)); // No tab

        return quickActionsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 120), 2),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        card.setPreferredSize(new Dimension(220, 140));
        
        // Add subtle shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 30), 1),
            card.getBorder()
        ));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setForeground(Color.black);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 38));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createQuickActionCard(final String actionTitle, String icon, final int tabIndex, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Add subtle shadow
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1),
            card.getBorder()
        ));

        JPanel iconPanel = new JPanel(new BorderLayout());
        iconPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        iconLabel.setForeground(iconColor);
        iconPanel.add(iconLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel(actionTitle, JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(TEXT_PRIMARY);

        card.add(iconPanel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        // Use a final reference for use in the anonymous inner class
        final JPanel actionCard = card;
        
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                actionCard.setBackground(new Color(240, 245, 250)); // Click effect
                Timer timer = new Timer(150, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        actionCard.setBackground(CARD_BACKGROUND);
                        if (tabIndex >= 0) {
                            // Check if the tab index exists before switching
                            if (tabIndex < tabbedPane.getTabCount()) {
                                tabbedPane.setSelectedIndex(tabIndex);
                            } else {
                                JOptionPane.showMessageDialog(AdminDashboard.this, 
                                    "This feature is not available yet.", 
                                    "Coming Soon", 
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            if (actionTitle.equals("System Info")) {
                                showSystemInfo();
                            }
                        }
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                actionCard.setBackground(HOVER_COLOR);
                actionCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(30, 20, 30, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                actionCard.setBackground(CARD_BACKGROUND);
                actionCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(30, 20, 30, 20)
                ));
            }
        });

        return card;
    }

    private void refreshAllData() {
        if (studentPanel != null) {
            studentPanel.refreshData();
        }
        if (instructorPanel != null) {
            instructorPanel.refreshData();
        }
        if (coursePanel != null) {
            coursePanel.refreshData();
        }
        
        // Show notification
        JOptionPane.showMessageDialog(this, 
            "All data refreshed successfully!", 
            "Refresh Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            // CORRECTED LINE: Use anonymous inner class instead of lambda
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginFrame().setVisible(true);
                }
            });
        }
    }

    private void showAboutDialog() {
        String aboutText = "<html>" +
            "<div style='text-align: center; padding: 20px; background-color: #f8f9fa;'>" +
            "<h2 style='color: #2C3E50; font-family: Segoe UI;'>Education Automation Management System</h2>" +
            "<div style='background-color: white; padding: 15px; border-radius: 5px; margin: 10px 0;'>" +
            "<p><b style='color: #34495E;'>Version:</b> 2.0.0</p>" +
            "<p><b style='color: #34495E;'>Current User:</b> " + currentAdmin.getFullName() + "</p>" +
            "<p><b style='color: #34495E;'>Role:</b> " + currentAdmin.getRole() + "</p>" +
            "<p><b style='color: #34495E;'>Last Login:</b> " + 
                (currentAdmin.getLastLogin() != null ? 
                 currentAdmin.getLastLogin().toLocalDate().toString() : "First login") + "</p>" +
            "<p><b style='color: #34495E;'>Developed by:</b> EAMS Development Team</p>" +
            "</div>" +
            "<hr style='border: none; border-top: 1px solid #ddd;'>" +
            "<p style='color: #7f8c8d; font-size: 12px;'>© 2024 EAMS. All rights reserved.</p>" +
            "</div>" +
            "</html>";
        
        JOptionPane optionPane = new JOptionPane(aboutText, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "About EAMS");
        dialog.setVisible(true);
    }

    private void showSystemInfo() {
        String systemInfo = "<html>" +
            "<div style='text-align: center; padding: 15px; background-color: #f8f9fa;'>" +
            "<h3 style='color: #2C3E50; font-family: Segoe UI;'>System Information</h3>" +
            "<div style='background-color: white; padding: 15px; border-radius: 5px; margin: 10px 0;'>" +
            "<table style='margin: 0 auto; border-spacing: 8px; text-align: left;'>" +
            "<tr><td align='right'><b style='color: #34495E;'>Java Version:</b></td><td>" + System.getProperty("java.version") + "</td></tr>" +
            "<tr><td align='right'><b style='color: #34495E;'>OS:</b></td><td>" + System.getProperty("os.name") + "</td></tr>" +
            "<tr><td align='right'><b style='color: #34495E;'>Architecture:</b></td><td>" + System.getProperty("os.arch") + "</td></tr>" +
            "<tr><td align='right'><b style='color: #34495E;'>Processors:</b></td><td>" + Runtime.getRuntime().availableProcessors() + "</td></tr>" +
            "<tr><td align='right'><b style='color: #34495E;'>Total Memory:</b></td><td>" + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " MB</td></tr>" +
            "<tr><td align='right'><b style='color: #34495E;'>Free Memory:</b></td><td>" + Runtime.getRuntime().freeMemory() / (1024 * 1024) + " MB</td></tr>" +
            "<tr><td align='right'><b style='color: #34495E;'>Database:</b></td><td>MySQL - eams_db</td></tr>" +
            "</table>" +
            "</div>" +
            "</div>" +
            "</html>";
        
        JOptionPane optionPane = new JOptionPane(systemInfo, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "System Information");
        dialog.setVisible(true);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            // Center the window
            setLocationRelativeTo(null);
        }
    }
}