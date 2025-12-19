package app;

import ui.LoginFrame;
import dao.DatabaseConnection;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set modern look and feel with proper error handling
        setLookAndFeel();
        
        // Initialize application
        initializeApplication();
    }
    
    private static void setLookAndFeel() {
        try {
            // CORRECTED: Use getSystemLookAndFeelClassName() instead of getSystemLookAndFeel()
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            System.err.println("Look and feel class not found: " + e.getMessage());
        } catch (InstantiationException e) {
            System.err.println("Look and feel instantiation failed: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Illegal access to look and feel: " + e.getMessage());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Unsupported look and feel: " + e.getMessage());
            // Try cross-platform as fallback
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.err.println("Cross-platform look and feel also failed: " + ex.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }
    }
    
    private static void initializeApplication() {
        try {
            // Test database connection
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            
            if (dbConnection.testConnection()) {
                System.out.println("Database connection successful!");
                launchLoginFrame();
            } else {
                showConnectionError("Database connection test failed");
            }
        } catch (Exception e) {
            showConnectionError("Database connection error: " + e.getMessage());
        }
    }
    
    private static void showConnectionError(String message) {
        System.err.println(message);
        JOptionPane.showMessageDialog(null, 
            message + "\nApplication will now exit.", 
            "Database Connection Error", 
            JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
    
    private static void launchLoginFrame() {
        // Use SwingUtilities to ensure thread safety with proper Runnable implementation
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        });
    }
}