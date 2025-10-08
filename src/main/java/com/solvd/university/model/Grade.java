package com.solvd.university.model;

public record Grade<T>(String subject, T value, String semester) {
    public Grade {
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be null or empty");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (semester == null || semester.trim().isEmpty()) {
            throw new IllegalArgumentException("Semester cannot be null or empty");
        }
    }

    public Grade<T> withValue(T newValue) {
        return new Grade<>(subject, newValue, semester);
    }
}
