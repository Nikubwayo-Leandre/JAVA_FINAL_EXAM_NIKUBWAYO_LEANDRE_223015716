package model;

import java.time.LocalDate;

public class Instructor {
    private int instructorId;
    private String name;
    private String identifier;
    private String status;
    private String location;
    private String contact;
    private LocalDate assignedSince;

    public Instructor() {}
    
    public Instructor(String name, String identifier) { 
        this.name = name; 
        this.identifier = identifier; 
    }

    // Getters and Setters
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public LocalDate getAssignedSince() { return assignedSince; }
    public void setAssignedSince(LocalDate assignedSince) { this.assignedSince = assignedSince; }

    @Override
    public String toString() { 
        return String.format("Instructor[ID: %d, Name: %s, Identifier: %s]", 
            instructorId, name, identifier); 
    }
}