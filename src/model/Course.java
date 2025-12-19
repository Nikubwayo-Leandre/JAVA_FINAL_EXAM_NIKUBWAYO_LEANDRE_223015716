package model;

import java.time.LocalDateTime;

public class Course {
    private int courseId;
    private String title;
    private String description;
    private String level;
    private LocalDateTime createdAt;
    private Integer instructorId;

    public Course() {}
    
    public Course(String title, String description, String level) {
        this.title = title; 
        this.description = description; 
        this.level = level;
    }

    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Integer getInstructorId() { return instructorId; }
    public void setInstructorId(Integer instructorId) { this.instructorId = instructorId; }

    @Override
    public String toString() { 
        return String.format("Course[ID: %d, Title: %s, Level: %s]", 
            courseId, title, level); 
    }
}