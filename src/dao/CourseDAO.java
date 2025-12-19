package dao;

import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    
    // Create a new course
    public int create(Course course) {
        String sql = "INSERT INTO course(title, description, level, instructor_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, course.getTitle());
            stmt.setString(2, course.getDescription());
            stmt.setString(3, course.getLevel());
            
            if (course.getInstructorId() != null) {
                stmt.setInt(4, course.getInstructorId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error creating course: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // Find course by ID
    public Course findById(int id) {
        String sql = "SELECT * FROM course WHERE course_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setLevel(rs.getString("level"));
                
                int instructorId = rs.getInt("instructor_id");
                if (!rs.wasNull()) {
                    course.setInstructorId(instructorId);
                }
                return course;
            }
        } catch (SQLException e) {
            System.err.println("Error finding course: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Get all courses
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY course_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setLevel(rs.getString("level"));
                
                int instructorId = rs.getInt("instructor_id");
                if (!rs.wasNull()) {
                    course.setInstructorId(instructorId);
                }
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    // Update course - COMPLETE IMPLEMENTATION
    public boolean update(Course course) {
        String sql = "UPDATE course SET title = ?, description = ?, level = ?, instructor_id = ? WHERE course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getTitle());
            stmt.setString(2, course.getDescription());
            stmt.setString(3, course.getLevel());
            
            if (course.getInstructorId() != null) {
                stmt.setInt(4, course.getInstructorId());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setInt(5, course.getCourseId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete course
    public boolean delete(int courseId) {
        String sql = "DELETE FROM course WHERE course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Find courses by instructor ID
    public List<Course> findByInstructorId(int instructorId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE instructor_id = ? ORDER BY title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setLevel(rs.getString("level"));
                course.setInstructorId(rs.getInt("instructor_id"));
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error finding courses by instructor: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    // Find courses by level
    public List<Course> findByLevel(String level) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE level = ? ORDER BY title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setLevel(rs.getString("level"));
                
                int instructorId = rs.getInt("instructor_id");
                if (!rs.wasNull()) {
                    course.setInstructorId(instructorId);
                }
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error finding courses by level: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    // Search courses by keyword in title or description
    public List<Course> searchByKeyword(String keyword) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE title LIKE ? OR description LIKE ? ORDER BY title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setLevel(rs.getString("level"));
                
                int instructorId = rs.getInt("instructor_id");
                if (!rs.wasNull()) {
                    course.setInstructorId(instructorId);
                }
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error searching courses: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }

    // Check if course title already exists
    public boolean titleExists(String title) {
        String sql = "SELECT COUNT(*) FROM course WHERE title = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking course title: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get total course count
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM course";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting courses: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Get count by level
    public int getCountByLevel(String level) {
        String sql = "SELECT COUNT(*) FROM course WHERE level = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting courses by level: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    // Get distinct course levels
    public List<String> getDistinctLevels() {
        List<String> levels = new ArrayList<>();
        String sql = "SELECT DISTINCT level FROM course ORDER BY level";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                levels.add(rs.getString("level"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving course levels: " + e.getMessage());
            e.printStackTrace();
        }
        return levels;
    }

    // Update only the course instructor
    public boolean updateInstructor(int courseId, Integer instructorId) {
        String sql = "UPDATE course SET instructor_id = ? WHERE course_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (instructorId != null) {
                stmt.setInt(1, instructorId);
            } else {
                stmt.setNull(1, Types.INTEGER);
            }
            
            stmt.setInt(2, courseId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating course instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get courses with no instructor assigned
    public List<Course> findCoursesWithoutInstructor() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course WHERE instructor_id IS NULL ORDER BY title";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setLevel(rs.getString("level"));
                course.setInstructorId(null);
                courses.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses without instructor: " + e.getMessage());
            e.printStackTrace();
        }
        return courses;
    }
}