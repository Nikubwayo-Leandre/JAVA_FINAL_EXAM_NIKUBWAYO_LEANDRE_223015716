package dao;

import model.Grade;
import java.sql.*;

public class GradeDAO {

    public int create(Grade grade) {
        String sql = "INSERT INTO grade(score, grade_letter, comments, assignment_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, grade.getScore());
            stmt.setString(2, grade.getGradeLetter());
            stmt.setString(3, grade.getComments());
            stmt.setInt(4, grade.getAssignmentId());
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating grade: " + e.getMessage());
        }
        return -1;
    }

    public Grade findByAssignmentId(int assignmentId) {
        String sql = "SELECT * FROM grade WHERE assignment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, assignmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Grade grade = new Grade();
                grade.setGradeId(rs.getInt("grade_id"));
                grade.setScore(rs.getString("score"));
                grade.setGradeLetter(rs.getString("grade_letter"));
                grade.setComments(rs.getString("comments"));
                grade.setAssignmentId(rs.getInt("assignment_id"));
                return grade;
            }
        } catch (SQLException e) {
            System.err.println("Error finding grade: " + e.getMessage());
        }
        return null;
    }
}