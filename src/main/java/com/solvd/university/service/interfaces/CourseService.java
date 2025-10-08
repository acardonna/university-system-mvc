package com.solvd.university.service.interfaces;

import com.solvd.university.model.Course;
import com.solvd.university.model.CourseFormatter;
import java.util.List;

public interface CourseService {
    void addCourse(Course<?, ?> course);

    Course<?, ?> getCourseByCode(String courseCode);

    List<Course<?, ?>> getAllCourses();

    List<Course<?, ?>> getCoursesByDepartment(Integer departmentId);

    List<String> formatCourses(CourseFormatter formatter);

    void updateCourse(Course<?, ?> course);

    void deleteCourse(String courseCode);
}
