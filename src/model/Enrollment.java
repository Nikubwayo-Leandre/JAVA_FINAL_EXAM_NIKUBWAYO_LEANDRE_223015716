package model;

import java.time.LocalDate;

public class Enrollment {
    private int enrollmentId;
    private String referenceId;
    private String description;
    private LocalDate date;
    private String status;
    private String remarks;
    private int studentId;
    private int courseId;

    public Enrollment() {}
    
    public Enrollment(int studentId, int courseId) { 
        this.studentId = studentId; 
        this.courseId = courseId; 
    }

    // Getters and Setters
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    @Override
    public String toString() { 
        return String.format("Enrollment[ID: %d, Student: %d, Course: %d, Status: %s]", 
            enrollmentId, studentId, courseId, status); 
    }
}