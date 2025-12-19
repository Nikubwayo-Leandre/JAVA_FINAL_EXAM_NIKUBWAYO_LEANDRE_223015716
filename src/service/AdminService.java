package service;

import model.Admin;
import dao.DatabaseConnection;
import java.sql.*;

public class AdminService {
    
    public Admin authenticate(String username, String password) {
        System.out.println("Authenticating user: " + username);
        
        try {
            // CORRECTED: Using 'admins' table instead of 'admin'
            Admin admin = authenticateFromAdminsTable(username, password);
            if (admin != null) {
                return admin;
            }
            
            // If not found in admins table, try user table as fallback
            admin = authenticateFromUserTable(username, password);
            if (admin != null) {
                return admin;
            }
            
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    private Admin authenticateFromAdminsTable(String username, String password) {
        // CORRECTED: Changed to 'admins' table
        String sql = "SELECT * FROM admins WHERE username = ? AND status = 'active'";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password_hash");
                System.out.println("Found in admins table. Stored password: " + storedPassword);
                
                if (password != null && password.equals(storedPassword)) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("admin_id"));
                    admin.setUsername(rs.getString("username"));
                    admin.setFullName(rs.getString("full_name"));
                    admin.setEmail(rs.getString("email"));
                    admin.setRole(rs.getString("role"));
                    return admin;
                }
            } else {
                System.out.println("No user found in admins table with username: " + username);
            }
        } catch (SQLException e) {
            System.err.println("Error reading from admins table: " + e.getMessage());
        }
        
        return null;
    }
    
    private Admin authenticateFromUserTable(String username, String password) {
        String sql = "SELECT * FROM user WHERE Username = ? AND UserType = 'ADMIN'";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("Password");
                System.out.println("Found in user table. Stored password: " + storedPassword);
                
                if (password != null && password.equals(storedPassword)) {
                    Admin admin = new Admin();
                    admin.setId(rs.getInt("UserID"));
                    admin.setUsername(rs.getString("Username"));
                    admin.setFullName(rs.getString("Username"));
                    admin.setEmail(rs.getString("Email"));
                    admin.setRole("admin");
                    return admin;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error reading from user table: " + e.getMessage());
        }
        
        return null;
    }
}