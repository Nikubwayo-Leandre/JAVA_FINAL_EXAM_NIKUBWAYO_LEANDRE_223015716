package service;

import model.Student;
import dao.StudentDAO;
import java.util.List;

public class StudentService {
    private StudentDAO studentDAO;
    
    public StudentService() {
        studentDAO = new StudentDAO();
    }
    
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }
    
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }
    
    public Student createStudent(String name, String email, String major) {
        Student student = new Student(name, email, major);
        int id = studentDAO.create(student);
        if (id != -1) {
            student.setStudentId(id);
            return student;
        }
        return null;
    }
    
    public boolean updateStudent(int studentId, String name, String email, String major) {
        Student student = new Student(name, email, major);
        student.setStudentId(studentId);
        return studentDAO.update(student);
    }
    
    public boolean deleteStudent(int studentId) {
        return studentDAO.delete(studentId);
    }
}