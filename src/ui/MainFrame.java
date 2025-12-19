package ui;

import model.Admin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Admin currentAdmin;
    private JPanel mainPanel;

    public MainFrame(Admin admin) {
        this.currentAdmin = admin;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        createMenuBar();
        createMainPanel();
        setupEventHandlers();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmExit();
            }
        });
        fileMenu.add(exitItem);
        
        // Admin Menu
        JMenu adminMenu = new JMenu("Admin");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        JMenuItem profileItem = new JMenuItem("My Profile");
        profileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showProfile();
            }
        });
        adminMenu.add(profileItem);
        adminMenu.addSeparator();
        adminMenu.add(logoutItem);
        
        // Management Menu
        JMenu managementMenu = new JMenu("Management");
        JMenuItem studentManagementItem = new JMenuItem("Student Management");
        studentManagementItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStudentManagement();
            }
        });
        managementMenu.add(studentManagementItem);
        
        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(managementMenu);
        menuBar.add(adminMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        
        // Content Panel
        JPanel contentPanel = createContentPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        // Welcome message
        String welcomeText = "Welcome, " + getAdminDisplayName();
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);

        // User info
        JLabel userInfoLabel = new JLabel("Role: " + (currentAdmin.getRole() != null ? currentAdmin.getRole() : "Administrator"));
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfoLabel.setForeground(Color.WHITE);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(41, 128, 185));
        userPanel.add(userInfoLabel);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // Create tabbed pane for different management sections
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Student Management Tab
        StudentManagementPanel studentPanel = new StudentManagementPanel();
        tabbedPane.addTab("Student Management", studentPanel);
        
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        return contentPanel;
    }

    private void setupEventHandlers() {
        // Window listener for closing confirmation
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                confirmExit();
            }
        });
    }

    private String getAdminDisplayName() {
        if (currentAdmin.getFullName() != null && !currentAdmin.getFullName().trim().isEmpty()) {
            return currentAdmin.getFullName();
        } else if (currentAdmin.getUsername() != null) {
            return currentAdmin.getUsername();
        } else {
            return "Administrator";
        }
    }

    private void showStudentManagement() {
        JOptionPane.showMessageDialog(this, "Student Management is already active", 
                                    "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showProfile() {
        String profileInfo = String.format(
            "User Profile:\n\n" +
            "Username: %s\n" +
            "Full Name: %s\n" +
            "Email: %s\n" +
            "Role: %s",
            currentAdmin.getUsername(),
            currentAdmin.getFullName() != null ? currentAdmin.getFullName() : "Not set",
            currentAdmin.getEmail() != null ? currentAdmin.getEmail() : "Not set",
            currentAdmin.getRole() != null ? currentAdmin.getRole() : "Administrator"
        );
        
        JOptionPane.showMessageDialog(this, profileInfo, "My Profile", JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            // Return to login screen
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new LoginFrame().setVisible(true);
                }
            });
        }
    }

    private void confirmExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showAbout() {
        String aboutMessage = 
            "Student Management System\n\n" +
            "Version: 1.0\n" +
            "Developed for educational institutions\n\n" +
            "Features:\n" +
            "• Student Information Management\n" +
            "• User Authentication\n" +
            "• Database Integration\n\n" +
            "© 2024 All Rights Reserved";
            
        JOptionPane.showMessageDialog(this, aboutMessage, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            setLocationRelativeTo(null);
        }
    }
}