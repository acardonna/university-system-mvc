package com.solvd.university.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.impl.MyBatisEnrollmentStatusDAOImpl;
import com.solvd.university.dao.interfaces.MyBatisEnrollmentStatusDAO;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.service.interfaces.MyBatisEnrollmentStatusService;

public class MyBatisEnrollmentStatusServiceImpl implements MyBatisEnrollmentStatusService {

    private static final Logger LOGGER = LogManager.getLogger(MyBatisEnrollmentStatusServiceImpl.class);
    private final MyBatisEnrollmentStatusDAO enrollmentStatusDAO;

    public MyBatisEnrollmentStatusServiceImpl() {
        this.enrollmentStatusDAO = new MyBatisEnrollmentStatusDAOImpl();
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

    @Override
    public void demonstrateCRUD() {
        EnrollmentStatus testStatus1 = new EnrollmentStatus();
        testStatus1.setDisplayName("MyBatis Status Alpha");
        testStatus1.setDescription("First MyBatis test status");

        addEnrollmentStatus(testStatus1);
        LOGGER.info(
            "Created first enrollment status: {} with ID: {}",
            testStatus1.getDisplayName(),
            testStatus1.getEnrollmentStatusId()
        );

        EnrollmentStatus testStatus2 = new EnrollmentStatus();
        testStatus2.setDisplayName("MyBatis Status Beta");
        testStatus2.setDescription("Second MyBatis test status");

        addEnrollmentStatus(testStatus2);
        LOGGER.info(
            "Created second enrollment status: {} with ID: {}",
            testStatus2.getDisplayName(),
            testStatus2.getEnrollmentStatusId()
        );

        EnrollmentStatus retrievedStatus = getEnrollmentStatusByDisplayName("MyBatis Status Alpha");
        if (retrievedStatus != null) {
            LOGGER.info("Retrieved enrollment status: {}", retrievedStatus.getDisplayName());

            retrievedStatus.setDescription("Updated MyBatis test status description");
            updateEnrollmentStatus(retrievedStatus);
            LOGGER.info("Updated enrollment status description");
        }

        if (testStatus2.getEnrollmentStatusId() != null) {
            deleteEnrollmentStatus(testStatus2.getEnrollmentStatusId());
            LOGGER.info(
                "Deleted enrollment status: {} with ID: {}",
                testStatus2.getDisplayName(),
                testStatus2.getEnrollmentStatusId()
            );
        }

        LOGGER.info(
            "Enrollment status '{}' with ID: {} remains in database",
            testStatus1.getDisplayName(),
            testStatus1.getEnrollmentStatusId()
        );
    }
}
