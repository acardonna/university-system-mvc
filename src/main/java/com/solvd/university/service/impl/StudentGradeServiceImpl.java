package com.solvd.university.service.impl;

import com.solvd.university.dao.impl.StudentGradeDAOImpl;
import com.solvd.university.dao.interfaces.StudentGradeDAO;
import com.solvd.university.model.StudentGrade;
import com.solvd.university.service.interfaces.StudentGradeService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StudentGradeServiceImpl implements StudentGradeService {

    private static final Logger LOGGER = LogManager.getLogger();
    private final StudentGradeDAO studentGradeDAO;

    public StudentGradeServiceImpl() {
        this.studentGradeDAO = new StudentGradeDAOImpl();
    }

    @Override
    public StudentGrade addGrade(Integer studentId, String subject, Double value, Integer semester) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (value == null || value < 0 || value > 100) {
            throw new IllegalArgumentException("Grade value must be between 0 and 100");
        }
        if (semester == null || semester < 1) {
            throw new IllegalArgumentException("Semester must be a positive number");
        }

        StudentGrade grade = new StudentGrade(studentId, subject, value, semester);
        studentGradeDAO.save(grade);

        LOGGER.info("Added grade for student {}: {} = {} (semester {})", studentId, subject, value, semester);

        return grade;
    }

    @Override
    public StudentGrade getGradeById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Grade ID cannot be null");
        }
        return studentGradeDAO.findById(id);
    }

    @Override
    public List<StudentGrade> getAllGrades() {
        return studentGradeDAO.findAll();
    }

    @Override
    public List<StudentGrade> getGradesByStudent(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        return studentGradeDAO.findByStudentId(studentId);
    }

    @Override
    public List<StudentGrade> getGradesByStudentAndSemester(Integer studentId, Integer semester) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        }
        return studentGradeDAO.findByStudentIdAndSemester(studentId, semester);
    }

    @Override
    public double calculateStudentAverage(Integer studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }

        List<StudentGrade> grades = studentGradeDAO.findByStudentId(studentId);

        if (grades.isEmpty()) {
            return 0.0;
        }

        double sum = grades.stream().mapToDouble(StudentGrade::getValue).sum();

        return sum / grades.size();
    }

    @Override
    public double calculateSemesterAverage(Integer studentId, Integer semester) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        if (semester == null) {
            throw new IllegalArgumentException("Semester cannot be null");
        }

        List<StudentGrade> grades = studentGradeDAO.findByStudentIdAndSemester(studentId, semester);

        if (grades.isEmpty()) {
            return 0.0;
        }

        double sum = grades.stream().mapToDouble(StudentGrade::getValue).sum();

        return sum / grades.size();
    }

    @Override
    public void updateGrade(StudentGrade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Grade cannot be null");
        }
        if (grade.getStudentGradeId() == null) {
            throw new IllegalArgumentException("Grade ID cannot be null for update");
        }

        studentGradeDAO.update(grade);
        LOGGER.info("Updated student grade: ID {}", grade.getStudentGradeId());
    }

    @Override
    public void deleteGrade(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Grade ID cannot be null");
        }

        studentGradeDAO.delete(id);
        LOGGER.info("Deleted student grade: ID {}", id);
    }

    @Override
    public boolean gradeExists(Integer id) {
        if (id == null) {
            return false;
        }
        return studentGradeDAO.findById(id) != null;
    }
}
