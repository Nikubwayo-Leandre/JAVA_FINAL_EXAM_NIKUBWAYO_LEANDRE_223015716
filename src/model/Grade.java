package model;

import java.time.LocalDateTime;

public class Grade {
    private int gradeId;
    private String score;
    private String gradeLetter;
    private String comments;
    private LocalDateTime createdAt;
    private int assignmentId;

    public Grade() {}
    
    public Grade(int assignmentId, String score) { 
        this.assignmentId = assignmentId; 
        this.score = score; 
    }

    // Getters and Setters
    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }
    public String getScore() { return score; }
    public void setScore(String score) { this.score = score; }
    public String getGradeLetter() { return gradeLetter; }
    public void setGradeLetter(String gradeLetter) { this.gradeLetter = gradeLetter; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }

    @Override
    public String toString() { 
        return String.format("Grade[ID: %d, Score: %s, Grade: %s]", 
            gradeId, score, gradeLetter); 
    }
}