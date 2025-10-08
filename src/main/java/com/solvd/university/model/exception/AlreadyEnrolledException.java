package com.solvd.university.model.exception;

public class AlreadyEnrolledException extends Exception {

    public AlreadyEnrolledException() {
        super("Student is already enrolled in a program. Cannot enroll in multiple programs simultaneously.");
    }

    public AlreadyEnrolledException(String message) {
        super(message);
    }
}
