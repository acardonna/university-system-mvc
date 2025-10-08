package com.solvd.university.model.exception;

public class DuplicateRegistrationException extends Exception {

    public DuplicateRegistrationException() {
        super("Student with this email is already registered. Please log in instead.");
    }

    public DuplicateRegistrationException(String message) {
        super(message);
    }
}
