package model;

import java.time.LocalDateTime;

public class Student {
    private int studentId;
    private String name;
    private String email;
    private String major;
    private LocalDateTime createdAt;

    public Student() {}

    public Student(String name, String email, String major) {
        this.name = name;
        this.email = email;
        this.major = major;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Student[ID: %d, Name: %s, Email: %s, Major: %s]", 
            studentId, name, email, major);
    }

	public Object getAttribute2() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAttribute1() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getAttribute3() {
		// TODO Auto-generated method stub
		return null;
	}
}