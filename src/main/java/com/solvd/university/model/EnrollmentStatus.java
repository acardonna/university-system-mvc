package com.solvd.university.model;

import java.util.Objects;

public class EnrollmentStatus {

    private Integer enrollmentStatusId;
    private String displayName;
    private String description;

    public static final EnrollmentStatus APPLIED = new EnrollmentStatus(1, "Applied", "Application submitted");
    public static final EnrollmentStatus ENROLLED = new EnrollmentStatus(4, "Enrolled", "Actively enrolled");
    public static final EnrollmentStatus WITHDRAWN = new EnrollmentStatus(8, "Withdrawn", "Student withdrew");

    public EnrollmentStatus() {}

    public EnrollmentStatus(Integer enrollmentStatusId, String displayName, String description) {
        this.enrollmentStatusId = enrollmentStatusId;
        this.displayName = displayName;
        this.description = description;
    }

    public EnrollmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public Integer getEnrollmentStatusId() {
        return enrollmentStatusId;
    }

    public void setEnrollmentStatusId(Integer enrollmentStatusId) {
        this.enrollmentStatusId = enrollmentStatusId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnrollmentStatus that = (EnrollmentStatus) o;

        if (enrollmentStatusId == null || that.enrollmentStatusId == null) {
            return Objects.equals(displayName, that.displayName);
        }
        return Objects.equals(enrollmentStatusId, that.enrollmentStatusId);
    }

    @Override
    public int hashCode() {
        return enrollmentStatusId != null ? Objects.hash(enrollmentStatusId) : Objects.hash(displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
