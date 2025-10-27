package com.solvd.university.service.interfaces;

import java.util.List;

import com.solvd.university.model.EnrollmentStatus;

public interface MyBatisEnrollmentStatusService {
    void addEnrollmentStatus(EnrollmentStatus status);

    EnrollmentStatus getEnrollmentStatusById(Integer enrollmentStatusId);

    EnrollmentStatus getEnrollmentStatusByDisplayName(String displayName);

    List<EnrollmentStatus> getAllEnrollmentStatuses();

    void updateEnrollmentStatus(EnrollmentStatus status);

    void deleteEnrollmentStatus(Integer enrollmentStatusId);

    void demonstrateCRUD();
}
