package com.solvd.university.model;

import java.time.LocalDate;

public record Enrollment(
    Integer enrollmentId,
    Integer studentId,
    Integer programId,
    Integer enrollmentStatusId,
    Student student,
    Program program,
    LocalDate enrollmentDate,
    EnrollmentStatus status
) {
    public Enrollment(Student student, Program program) {
        this(
            null,
            student != null ? student.getStudentId() : null,
            program != null ? program.getProgramId() : null,
            EnrollmentStatus.ENROLLED.getEnrollmentStatusId(),
            student,
            program,
            LocalDate.now(),
            EnrollmentStatus.ENROLLED
        );
    }

    public Enrollment {
        if (studentId == null && student == null) {
            throw new IllegalArgumentException("Student or studentId must be provided");
        }
        if (programId == null && program == null) {
            throw new IllegalArgumentException("Program or programId must be provided");
        }
        if (enrollmentDate == null) {
            throw new IllegalArgumentException("Enrollment date cannot be null");
        }
        if (enrollmentStatusId == null && status == null) {
            throw new IllegalArgumentException("Status or enrollmentStatusId must be provided");
        }
    }

    public Enrollment withStatus(EnrollmentStatus newStatus) {
        return new Enrollment(
            enrollmentId,
            studentId,
            programId,
            newStatus != null ? newStatus.getEnrollmentStatusId() : enrollmentStatusId,
            student,
            program,
            enrollmentDate,
            newStatus
        );
    }
}
