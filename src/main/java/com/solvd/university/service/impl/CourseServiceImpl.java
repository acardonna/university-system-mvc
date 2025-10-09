package com.solvd.university.service.impl;

import com.solvd.university.dao.impl.*;
import com.solvd.university.dao.interfaces.CourseDAO;
import com.solvd.university.model.Course;
import com.solvd.university.model.CourseFormatter;
import com.solvd.university.service.interfaces.CourseService;
import java.util.List;
import java.util.stream.Collectors;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO;

    public CourseServiceImpl() {
        this.courseDAO = new CourseDAOImpl(
            new DepartmentDAOImpl(),
            new ProfessorDAOImpl(new DepartmentDAOImpl()),
            new ClassroomDAOImpl(new BuildingDAOImpl())
        );
    }

    @Override
    public void addCourse(Course<?, ?> course) {
        courseDAO.save(course);
    }

    @Override
    public Course<?, ?> getCourseByCode(String courseCode) {
        return courseDAO.findById(courseCode).orElse(null);
    }

    @Override
    public List<Course<?, ?>> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Course<?, ?>> getCoursesByDepartment(Integer departmentId) {
        if (departmentId == null) {
            return getAllCourses();
        }
        return courseDAO
            .findAll()
            .stream()
            .filter(course -> departmentId.equals(course.getDepartmentId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<String> formatCourses(CourseFormatter formatter) {
        return courseDAO.findAll().stream().map(formatter::format).collect(Collectors.toList());
    }

    @Override
    public void updateCourse(Course<?, ?> course) {
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(String courseCode) {
        courseDAO.delete(courseCode);
    }
}
