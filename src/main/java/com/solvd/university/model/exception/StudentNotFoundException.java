package com.solvd.university.model.exception;

public class StudentNotFoundException extends Exception {

    public StudentNotFoundException() {
        super("Student not found. Please check your credentials.");
    }

    public StudentNotFoundException(String message) {
        super(message);
    }
}
