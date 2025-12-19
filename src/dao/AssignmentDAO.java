package dao;

import model.Assignment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssignmentDAO {

    public int create(Assignment assignment) {
        String sql = "INSERT INTO assignment(reference_id, description, date, status, remarks, enrollment_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, assignment.getReferenceId());
            stmt.setString(2, assignment.getDescription());
            
            if (assignment.getDate() != null) {
                stmt.setDate(3, Date.valueOf(assignment.getDate()));
            } else {
                stmt.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            }
            
            stmt.setString(4, assignment.getStatus() != null ? assignment.getStatus() : "Pending");
            stmt.setString(5, assignment.getRemarks());
            stmt.setInt(6, assignment.getEnrollmentId());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating assignment: " + e.getMessage());
        }
        return -1;
    }

    public List<Assignment> findByEnrollment(int enrollmentId) {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignment WHERE enrollment_id = ? ORDER BY assignment_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentId(rs.getInt("assignment_id"));
                assignment.setReferenceId(rs.getString("reference_id"));
                assignment.setDescription(rs.getString("description"));
                assignment.setDate(rs.getDate("date").toLocalDate());
                assignment.setStatus(rs.getString("status"));
                assignment.setRemarks(rs.getString("remarks"));
                assignment.setEnrollmentId(rs.getInt("enrollment_id"));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            System.err.println("Error finding assignments: " + e.getMessage());
        }
        return assignments;
    }

    public List<Assignment> findAll() {
        List<Assignment> assignments = new ArrayList<>();
        String sql = "SELECT * FROM assignment ORDER BY assignment_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Assignment assignment = new Assignment();
                assignment.setAssignmentId(rs.getInt("assignment_id"));
                assignment.setReferenceId(rs.getString("reference_id"));
                assignment.setDescription(rs.getString("description"));
                assignment.setEnrollmentId(rs.getInt("enrollment_id"));
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving assignments: " + e.getMessage());
        }
        return assignments;
    }
}