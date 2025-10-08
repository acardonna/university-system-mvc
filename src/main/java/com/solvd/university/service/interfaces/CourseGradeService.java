package com.solvd.university.service.interfaces;

import com.solvd.university.model.CourseGrade;
import java.time.LocalDateTime;
import java.util.List;

public interface CourseGradeService {
    CourseGrade addGrade(Integer courseId, String subject, Double value, Integer semester, LocalDateTime recordedAt);

    CourseGrade getGradeById(Integer id);

    List<CourseGrade> getAllGrades();

    List<CourseGrade> getGradesByCourse(Integer courseId);

    List<CourseGrade> getGradesByCourseAndSemester(Integer courseId, Integer semester);

    double calculateCourseAverage(Integer courseId);

    void updateGrade(CourseGrade grade);

    void deleteGrade(Integer id);

    boolean gradeExists(Integer id);
}
