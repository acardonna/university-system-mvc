package com.solvd.university.model;

@FunctionalInterface
public interface StudentFilter {
    boolean matches(Student student);
}
