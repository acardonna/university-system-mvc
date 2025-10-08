package com.solvd.university.dao.interfaces;

import com.solvd.university.model.StudentGrade;
import java.util.List;

public interface StudentGradeDAO {
    void save(StudentGrade grade);

    StudentGrade findById(Integer id);

    List<StudentGrade> findAll();

    List<StudentGrade> findByStudentId(Integer studentId);

    List<StudentGrade> findByStudentIdAndSemester(Integer studentId, Integer semester);

    void update(StudentGrade grade);

    void delete(Integer id);
}
