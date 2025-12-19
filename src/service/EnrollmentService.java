package service;

import dao.EnrollmentDAO;
import model.Enrollment;
import java.util.List;

public class EnrollmentService {
    private final EnrollmentDAO dao = new EnrollmentDAO();

    public Enrollment enrollStudent(int studentId, int courseId) {
        Enrollment enrollment = new Enrollment(studentId, courseId);
        enrollment.setReferenceId("ENR-" + studentId + "-" + courseId);
        enrollment.setDescription("Enrollment of student " + studentId + " in course " + courseId);
        enrollment.setStatus("Enrolled");
        int id = dao.create(enrollment);
        if (id > 0) {
            enrollment.setEnrollmentId(id);
            System.out.println("✅ Student enrolled successfully: " + enrollment);
            return enrollment;
        } else {
            System.out.println("❌ Failed to enroll student");
            return null;
        }
    }

    public Enrollment getById(int id) { 
        Enrollment enrollment = dao.findById(id);
        if (enrollment == null) {
            System.out.println("❌ Enrollment not found with ID: " + id);
        }
        return enrollment; 
    }
    
    public List<Enrollment> getByStudent(int studentId) { 
        List<Enrollment> enrollments = dao.findByStudentId(studentId);
        System.out.println("📋 Found " + enrollments.size() + " enrollments for student ID: " + studentId);
        return enrollments; 
    }

    public List<Enrollment> getAll() {
        List<Enrollment> enrollments = dao.findAll();
        System.out.println("📋 Found " + enrollments.size() + " enrollments");
        return enrollments;
    }
}