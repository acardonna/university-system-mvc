package com.solvd.university.model;

@FunctionalInterface
public interface CourseFormatter {
    String format(Course<?, ?> course);
}
