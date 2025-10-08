package com.solvd.university.service.interfaces;

import com.solvd.university.model.StudentGrade;
import java.util.List;

public interface StudentGradeService {
    StudentGrade addGrade(Integer studentId, String subject, Double value, Integer semester);

    StudentGrade getGradeById(Integer id);

    List<StudentGrade> getAllGrades();

    List<StudentGrade> getGradesByStudent(Integer studentId);

    List<StudentGrade> getGradesByStudentAndSemester(Integer studentId, Integer semester);

    double calculateStudentAverage(Integer studentId);

    double calculateSemesterAverage(Integer studentId, Integer semester);

    void updateGrade(StudentGrade grade);

    void deleteGrade(Integer id);

    boolean gradeExists(Integer id);
}
