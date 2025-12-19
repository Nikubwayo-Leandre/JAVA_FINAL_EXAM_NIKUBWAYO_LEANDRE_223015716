package service;

import dao.AssignmentDAO;
import model.Assignment;
import java.util.List;

public class AssignmentService {
    private final AssignmentDAO dao = new AssignmentDAO();

    public Assignment createAssignment(int enrollmentId, String reference, String description) {
        Assignment assignment = new Assignment(enrollmentId, reference);
        assignment.setDescription(description);
        assignment.setStatus("Assigned");
        int id = dao.create(assignment);
        if (id > 0) {
            assignment.setAssignmentId(id);
            System.out.println("✅ Assignment created successfully: " + assignment);
            return assignment;
        } else {
            System.out.println("❌ Failed to create assignment");
            return null;
        }
    }

    public List<Assignment> getByEnrollment(int enrollmentId) { 
        List<Assignment> assignments = dao.findByEnrollment(enrollmentId);
        System.out.println("📋 Found " + assignments.size() + " assignments for enrollment ID: " + enrollmentId);
        return assignments; 
    }

    public List<Assignment> getAll() {
        List<Assignment> assignments = dao.findAll();
        System.out.println("📋 Found " + assignments.size() + " assignments");
        return assignments;
    }
}