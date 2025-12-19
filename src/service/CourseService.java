package service;

import dao.CourseDAO;
import model.Course;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService() {
        this.courseDAO = new CourseDAO();
    }

    // Create a new course
    public Course createCourse(String title, String description, String level, Integer instructorId) {
        try {
            Course course = new Course(title, description, level);
            course.setInstructorId(instructorId);
            
            int courseId = courseDAO.create(course);
            if (courseId > 0) {
                course.setCourseId(courseId);
                System.out.println("✅ Course created successfully: " + course.getTitle() + " (ID: " + courseId + ")");
                return course;
            } else {
                System.out.println("❌ Failed to create course: " + title);
                return null;
            }
        } catch (Exception e) {
            System.err.println("❌ Error creating course: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get course by ID
    public Course getById(int courseId) {
        try {
            Course course = courseDAO.findById(courseId);
            if (course != null) {
                System.out.println("✅ Retrieved course: " + course.getTitle());
            } else {
                System.out.println("❌ Course not found with ID: " + courseId);
            }
            return course;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving course: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get all courses
    public List<Course> getAll() {
        try {
            List<Course> courses = courseDAO.findAll();
            System.out.println("✅ Retrieved " + courses.size() + " courses");
            return courses;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving courses: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Update course
    public boolean updateCourse(Course course) {
        try {
            // Validate course exists
            Course existingCourse = courseDAO.findById(course.getCourseId());
            if (existingCourse == null) {
                System.out.println("❌ Course not found for update: ID " + course.getCourseId());
                return false;
            }

            // Update the course
            boolean success = courseDAO.update(course);
            if (success) {
                System.out.println("✅ Course updated successfully: " + course.getTitle() + " (ID: " + course.getCourseId() + ")");
            } else {
                System.out.println("❌ Failed to update course: " + course.getTitle());
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error updating course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete course
    public boolean deleteCourse(int courseId) {
        try {
            // Validate course exists
            Course existingCourse = courseDAO.findById(courseId);
            if (existingCourse == null) {
                System.out.println("❌ Course not found for deletion: ID " + courseId);
                return false;
            }

            boolean success = courseDAO.delete(courseId);
            if (success) {
                System.out.println("✅ Course deleted successfully: ID " + courseId);
            } else {
                System.out.println("❌ Failed to delete course: ID " + courseId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error deleting course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get courses by instructor
    public List<Course> getCoursesByInstructor(int instructorId) {
        try {
            List<Course> courses = courseDAO.findByInstructorId(instructorId);
            System.out.println("✅ Retrieved " + courses.size() + " courses for instructor ID: " + instructorId);
            return courses;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving courses by instructor: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Get courses by level
    public List<Course> getCoursesByLevel(String level) {
        try {
            List<Course> courses = courseDAO.findByLevel(level);
            System.out.println("✅ Retrieved " + courses.size() + " courses for level: " + level);
            return courses;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving courses by level: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Search courses by title or description
    public List<Course> searchCourses(String keyword) {
        try {
            List<Course> courses = courseDAO.searchByKeyword(keyword);
            System.out.println("✅ Found " + courses.size() + " courses matching: " + keyword);
            return courses;
        } catch (Exception e) {
            System.err.println("❌ Error searching courses: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Check if course title already exists
    public boolean courseTitleExists(String title) {
        try {
            boolean exists = courseDAO.titleExists(title);
            if (exists) {
                System.out.println("ℹ️ Course title already exists: " + title);
            }
            return exists;
        } catch (Exception e) {
            System.err.println("❌ Error checking course title: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get course count
    public int getCourseCount() {
        try {
            int count = courseDAO.getTotalCount();
            System.out.println("ℹ️ Total courses in system: " + count);
            return count;
        } catch (Exception e) {
            System.err.println("❌ Error getting course count: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    // Update course instructor
    public boolean updateCourseInstructor(int courseId, Integer instructorId) {
        try {
            Course course = courseDAO.findById(courseId);
            if (course == null) {
                System.out.println("❌ Course not found: ID " + courseId);
                return false;
            }

            course.setInstructorId(instructorId);
            boolean success = courseDAO.update(course);
            if (success) {
                System.out.println("✅ Course instructor updated successfully for course ID: " + courseId);
            } else {
                System.out.println("❌ Failed to update course instructor for course ID: " + courseId);
            }
            return success;
        } catch (Exception e) {
            System.err.println("❌ Error updating course instructor: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get available course levels
    public List<String> getAvailableLevels() {
        try {
            List<String> levels = courseDAO.getDistinctLevels();
            System.out.println("✅ Retrieved " + levels.size() + " available course levels");
            return levels;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving course levels: " + e.getMessage());
            e.printStackTrace();
            // Fallback - compatible with all Java versions
            List<String> fallbackLevels = new ArrayList<>();
            fallbackLevels.add("Beginner");
            fallbackLevels.add("Intermediate");
            fallbackLevels.add("Advanced");
            fallbackLevels.add("Expert");
            return fallbackLevels;
        }
    }

    // Validate course data
    public boolean validateCourseData(String title, String level) {
        if (title == null || title.trim().isEmpty()) {
            System.out.println("❌ Course title cannot be empty");
            return false;
        }
        if (level == null || level.trim().isEmpty()) {
            System.out.println("❌ Course level cannot be empty");
            return false;
        }
        if (title.length() > 100) {
            System.out.println("❌ Course title too long (max 100 characters)");
            return false;
        }
        return true;
    }
}