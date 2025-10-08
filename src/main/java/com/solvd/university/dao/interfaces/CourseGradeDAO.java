package com.solvd.university.dao.interfaces;

import com.solvd.university.model.CourseGrade;
import java.util.List;

public interface CourseGradeDAO {
    void save(CourseGrade grade);

    CourseGrade findById(Integer id);

    List<CourseGrade> findAll();

    List<CourseGrade> findByCourseId(Integer courseId);

    List<CourseGrade> findByCourseIdAndSemester(Integer courseId, Integer semester);

    void update(CourseGrade grade);

    void delete(Integer id);
}
