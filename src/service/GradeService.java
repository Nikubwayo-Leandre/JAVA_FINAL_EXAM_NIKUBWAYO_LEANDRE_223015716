package service;

import dao.GradeDAO;
import model.Grade;

public class GradeService {
    private final GradeDAO dao = new GradeDAO();

    public Grade recordGrade(int assignmentId, String score, String gradeLetter, String comments) {
        Grade grade = new Grade(assignmentId, score);
        grade.setGradeLetter(gradeLetter);
        grade.setComments(comments);
        int id = dao.create(grade);
        if (id > 0) {
            grade.setGradeId(id);
            System.out.println("✅ Grade recorded successfully: " + grade);
            return grade;
        } else {
            System.out.println("❌ Failed to record grade");
            return null;
        }
    }

    public Grade getGradeByAssignment(int assignmentId) {
        Grade grade = dao.findByAssignmentId(assignmentId);
        if (grade == null) {
            System.out.println("❌ No grade found for assignment ID: " + assignmentId);
        }
        return grade;
    }
}
