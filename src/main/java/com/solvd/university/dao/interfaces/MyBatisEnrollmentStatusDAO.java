package com.solvd.university.dao.interfaces;

import java.util.List;
import java.util.Optional;

import com.solvd.university.model.EnrollmentStatus;

public interface MyBatisEnrollmentStatusDAO {
    void save(EnrollmentStatus status);

    Optional<EnrollmentStatus> findById(Integer enrollmentStatusId);

    Optional<EnrollmentStatus> findByDisplayName(String displayName);

    List<EnrollmentStatus> findAll();

    void update(EnrollmentStatus status);

    void delete(Integer enrollmentStatusId);
}
