package model;

import java.time.LocalDate;

public class Assignment {
    private int assignmentId;
    private String referenceId;
    private String description;
    private LocalDate date;
    private String status;
    private String remarks;
    private int enrollmentId;

    public Assignment() {}
    
    public Assignment(int enrollmentId, String referenceId) { 
        this.enrollmentId = enrollmentId; 
        this.referenceId = referenceId; 
    }

    // Getters and Setters
    public int getAssignmentId() { return assignmentId; }
    public void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }
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
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    @Override
    public String toString() { 
        return String.format("Assignment[ID: %d, Reference: %s, Enrollment: %d]", 
            assignmentId, referenceId, enrollmentId); 
    }
}