package com.solvd.university.service.impl;

import com.solvd.university.dao.impl.EnrollmentStatusDAOImpl;
import com.solvd.university.dao.interfaces.EnrollmentStatusDAO;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.service.interfaces.EnrollmentStatusService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnrollmentStatusServiceImpl implements EnrollmentStatusService {

    private static final Logger LOGGER = LogManager.getLogger(EnrollmentStatusServiceImpl.class);
    private final EnrollmentStatusDAO enrollmentStatusDAO;

    public EnrollmentStatusServiceImpl() {
        this.enrollmentStatusDAO = new EnrollmentStatusDAOImpl();
    }

    @Override
    public EnrollmentStatus getEnrollmentStatusById(Integer enrollmentStatusId) {
        if (enrollmentStatusId == null) {
            throw new IllegalArgumentException("Enrollment status ID cannot be null");
        }

        return enrollmentStatusDAO
            .findById(enrollmentStatusId)
            .orElseThrow(() -> new RuntimeException("Enrollment status not found with ID: " + enrollmentStatusId));
    }

    @Override
    public EnrollmentStatus getEnrollmentStatusByDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }

        return enrollmentStatusDAO
            .findByDisplayName(displayName)
            .orElseThrow(() -> new RuntimeException("Enrollment status not found with display name: " + displayName));
    }

    @Override
    public List<EnrollmentStatus> getAllEnrollmentStatuses() {
        return enrollmentStatusDAO.findAll();
    }

    @Override
    public void addEnrollmentStatus(EnrollmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Enrollment status cannot be null");
        }
        enrollmentStatusDAO.save(status);
    }

    @Override
    public void updateEnrollmentStatus(EnrollmentStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Enrollment status cannot be null");
        }
        if (status.getEnrollmentStatusId() == null) {
            throw new IllegalArgumentException("Enrollment status ID cannot be null for update");
        }
        enrollmentStatusDAO.update(status);
        LOGGER.info("Updated enrollment status with ID: {}", status.getEnrollmentStatusId());
    }

    @Override
    public void deleteEnrollmentStatus(Integer enrollmentStatusId) {
        if (enrollmentStatusId == null) {
            throw new IllegalArgumentException("Enrollment status ID cannot be null");
        }
        enrollmentStatusDAO.delete(enrollmentStatusId);
        LOGGER.info("Deleted enrollment status with ID: {}", enrollmentStatusId);
    }
}
