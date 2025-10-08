package com.solvd.university.model.exception;

public class StudentNotEnrolledException extends Exception {

    public StudentNotEnrolledException() {
        super("Student is not enrolled in any program. Please enroll in a program first.");
    }

    public StudentNotEnrolledException(String message) {
        super(message);
    }
}
