package dao;

import model.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    public int create(Enrollment enrollment) {
        String sql = "INSERT INTO enrollment(reference_id, description, date, status, remarks, student_id, course_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, enrollment.getReferenceId());
            stmt.setString(2, enrollment.getDescription());
            
            if (enrollment.getDate() != null) {
                stmt.setDate(3, Date.valueOf(enrollment.getDate()));
            } else {
                stmt.setDate(3, Date.valueOf(java.time.LocalDate.now()));
            }
            
            stmt.setString(4, enrollment.getStatus() != null ? enrollment.getStatus() : "Enrolled");
            stmt.setString(5, enrollment.getRemarks());
            stmt.setInt(6, enrollment.getStudentId());
            stmt.setInt(7, enrollment.getCourseId());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating enrollment: " + e.getMessage());
        }
        return -1;
    }

    public Enrollment findById(int id) {
        String sql = "SELECT * FROM enrollment WHERE enrollment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setReferenceId(rs.getString("reference_id"));
                enrollment.setDescription(rs.getString("description"));
                enrollment.setDate(rs.getDate("date").toLocalDate());
                enrollment.setStatus(rs.getString("status"));
                enrollment.setRemarks(rs.getString("remarks"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                return enrollment;
            }
        } catch (SQLException e) {
            System.err.println("Error finding enrollment: " + e.getMessage());
        }
        return null;
    }

    public List<Enrollment> findByStudentId(int studentId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollment WHERE student_id = ? ORDER BY enrollment_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setReferenceId(rs.getString("reference_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setStatus(rs.getString("status"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Error finding enrollments by student: " + e.getMessage());
        }
        return enrollments;
    }

    public List<Enrollment> findAll() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollment ORDER BY enrollment_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Enrollment enrollment = new Enrollment();
                enrollment.setEnrollmentId(rs.getInt("enrollment_id"));
                enrollment.setReferenceId(rs.getString("reference_id"));
                enrollment.setStudentId(rs.getInt("student_id"));
                enrollment.setCourseId(rs.getInt("course_id"));
                enrollment.setStatus(rs.getString("status"));
                enrollments.add(enrollment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving enrollments: " + e.getMessage());
        }
        return enrollments;
    }
}