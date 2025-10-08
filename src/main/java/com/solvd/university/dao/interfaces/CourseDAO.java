package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Course;
import java.util.List;
import java.util.Optional;

public interface CourseDAO {
    void save(Course<?, ?> course);

    Optional<Course<?, ?>> findById(String courseCode);

    List<Course<?, ?>> findAll();

    void update(Course<?, ?> course);

    void delete(String courseCode);
}
