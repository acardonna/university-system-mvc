package com.solvd.university.service.interfaces;

import com.solvd.university.model.EnrollmentStatus;
import java.util.List;

public interface EnrollmentStatusService {
    void addEnrollmentStatus(EnrollmentStatus status);

    EnrollmentStatus getEnrollmentStatusById(Integer enrollmentStatusId);

    EnrollmentStatus getEnrollmentStatusByDisplayName(String displayName);

    List<EnrollmentStatus> getAllEnrollmentStatuses();

    void updateEnrollmentStatus(EnrollmentStatus status);

    void deleteEnrollmentStatus(Integer enrollmentStatusId);
}
