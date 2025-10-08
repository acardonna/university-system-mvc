package com.solvd.university.model;

@FunctionalInterface
public interface GradeValidator {
    boolean isValid(double gradeValue);
}
