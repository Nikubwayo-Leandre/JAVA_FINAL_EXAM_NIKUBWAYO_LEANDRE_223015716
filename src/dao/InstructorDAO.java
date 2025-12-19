package dao;

import model.Instructor;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InstructorDAO {

    // Create a new instructor
    public int create(Instructor instructor) {
        String sql = "INSERT INTO instructor(name, identifier, status, location, contact, assigned_since) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, instructor.getName());
            stmt.setString(2, instructor.getIdentifier());
            stmt.setString(3, instructor.getStatus() != null ? instructor.getStatus() : "Active");
            stmt.setString(4, instructor.getLocation());
            stmt.setString(5, instructor.getContact());
            
            if (instructor.getAssignedSince() != null) {
                stmt.setDate(6, Date.valueOf(instructor.getAssignedSince()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating instructor: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // Find instructor by ID
    public Instructor findById(int id) {
        String sql = "SELECT * FROM instructor WHERE instructor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                return instructor;
            }
        } catch (SQLException e) {
            System.err.println("Error finding instructor: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get all instructors
    public List<Instructor> findAll() {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructor ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving instructors: " + e.getMessage());
            e.printStackTrace();
        }
        return instructors;
    }

    // Update instructor - COMPLETE IMPLEMENTATION
    public boolean update(Instructor instructor) {
        String sql = "UPDATE instructor SET name = ?, identifier = ?, status = ?, location = ?, contact = ?, assigned_since = ? WHERE instructor_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, instructor.getName());
            stmt.setString(2, instructor.getIdentifier());
            stmt.setString(3, instructor.getStatus());
            stmt.setString(4, instructor.getLocation());
            stmt.setString(5, instructor.getContact());
            
            if (instructor.getAssignedSince() != null) {
                stmt.setDate(6, Date.valueOf(instructor.getAssignedSince()));
            } else {
                stmt.setNull(6, Types.DATE);
            }
            
            stmt.setInt(7, instructor.getInstructorId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete instructor
    public boolean delete(int instructorId) {
        String sql = "DELETE FROM instructor WHERE instructor_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, instructorId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Find instructors by status
    public List<Instructor> findByStatus(String status) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructor WHERE status = ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println("Error finding instructors by status: " + e.getMessage());
            e.printStackTrace();
        }
        return instructors;
    }

    // Find instructors by location
    public List<Instructor> findByLocation(String location) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructor WHERE location = ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println("Error finding instructors by location: " + e.getMessage());
            e.printStackTrace();
        }
        return instructors;
    }

    // Search instructors by keyword in name, identifier, or location
    public List<Instructor> searchByKeyword(String keyword) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructor WHERE name LIKE ? OR identifier LIKE ? OR location LIKE ? ORDER BY name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println("Error searching instructors: " + e.getMessage());
            e.printStackTrace();
        }
        return instructors;
    }

    // Check if instructor identifier already exists
    public boolean identifierExists(String identifier) {
        String sql = "SELECT COUNT(*) FROM instructor WHERE identifier = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, identifier);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking instructor identifier: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get total instructor count
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM instructor";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting instructors: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Get count by status
    public int getCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM instructor WHERE status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting instructors by status: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Get count by location
    public int getCountByLocation(String location) {
        String sql = "SELECT COUNT(*) FROM instructor WHERE location = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting instructors by location: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Get distinct instructor statuses
    public List<String> getDistinctStatuses() {
        List<String> statuses = new ArrayList<>();
        String sql = "SELECT DISTINCT status FROM instructor ORDER BY status";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                statuses.add(rs.getString("status"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving instructor statuses: " + e.getMessage());
            e.printStackTrace();
        }
        return statuses;
    }

    // Get distinct instructor locations
    public List<String> getDistinctLocations() {
        List<String> locations = new ArrayList<>();
        String sql = "SELECT DISTINCT location FROM instructor WHERE location IS NOT NULL ORDER BY location";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving instructor locations: " + e.getMessage());
            e.printStackTrace();
        }
        return locations;
    }

    // Find instructors assigned since a specific date
    public List<Instructor> findByAssignedSince(LocalDate date) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructor WHERE assigned_since >= ? ORDER BY assigned_since DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            System.err.println("Error finding instructors by assigned date: " + e.getMessage());
            e.printStackTrace();
        }
        return instructors;
    }

    // Update only the instructor status
    public boolean updateStatus(int instructorId, String status) {
        String sql = "UPDATE instructor SET status = ? WHERE instructor_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, instructorId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating instructor status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Find instructor by identifier
    public Instructor findByIdentifier(String identifier) {
        String sql = "SELECT * FROM instructor WHERE identifier = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, identifier);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Instructor instructor = new Instructor();
                instructor.setInstructorId(rs.getInt("instructor_id"));
                instructor.setName(rs.getString("name"));
                instructor.setIdentifier(rs.getString("identifier"));
                instructor.setStatus(rs.getString("status"));
                instructor.setLocation(rs.getString("location"));
                instructor.setContact(rs.getString("contact"));
                
                Date assignedSince = rs.getDate("assigned_since");
                if (assignedSince != null) {
                    instructor.setAssignedSince(assignedSince.toLocalDate());
                }
                return instructor;
            }
        } catch (SQLException e) {
            System.err.println("Error finding instructor by identifier: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}