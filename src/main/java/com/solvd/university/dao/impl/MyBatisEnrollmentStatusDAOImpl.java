package com.solvd.university.dao.impl;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.interfaces.MyBatisEnrollmentStatusDAO;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.util.MyBatisSessionHolder;

public class MyBatisEnrollmentStatusDAOImpl implements MyBatisEnrollmentStatusDAO {

    private static final Logger LOGGER = LogManager.getLogger(MyBatisEnrollmentStatusDAOImpl.class);

    @Override
    public void save(EnrollmentStatus status) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisEnrollmentStatusDAO mapper = session.getMapper(MyBatisEnrollmentStatusDAO.class);
            mapper.save(status);
            session.commit();
            LOGGER.info("Enrollment status saved successfully with ID: {}", status.getEnrollmentStatusId());
        } catch (Exception e) {
            LOGGER.error("Error saving enrollment status: {}", status.getDisplayName(), e);
            throw new RuntimeException("Failed to save enrollment status", e);
        }
    }

    @Override
    public Optional<EnrollmentStatus> findById(Integer enrollmentStatusId) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisEnrollmentStatusDAO mapper = session.getMapper(MyBatisEnrollmentStatusDAO.class);
            return mapper.findById(enrollmentStatusId);
        } catch (Exception e) {
            LOGGER.error("Error finding enrollment status by ID: {}", enrollmentStatusId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<EnrollmentStatus> findByDisplayName(String displayName) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisEnrollmentStatusDAO mapper = session.getMapper(MyBatisEnrollmentStatusDAO.class);
            return mapper.findByDisplayName(displayName);
        } catch (Exception e) {
            LOGGER.error("Error finding enrollment status by display name: {}", displayName, e);
            return Optional.empty();
        }
    }

    @Override
    public List<EnrollmentStatus> findAll() {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisEnrollmentStatusDAO mapper = session.getMapper(MyBatisEnrollmentStatusDAO.class);
            return mapper.findAll();
        } catch (Exception e) {
            LOGGER.error("Error retrieving all enrollment statuses", e);
            return List.of();
        }
    }

    @Override
    public void update(EnrollmentStatus status) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisEnrollmentStatusDAO mapper = session.getMapper(MyBatisEnrollmentStatusDAO.class);
            mapper.update(status);
            session.commit();
            LOGGER.info("Enrollment status updated successfully with ID: {}", status.getEnrollmentStatusId());
        } catch (Exception e) {
            LOGGER.error("Error updating enrollment status: {}", status.getEnrollmentStatusId(), e);
            throw new RuntimeException("Failed to update enrollment status", e);
        }
    }

    @Override
    public void delete(Integer enrollmentStatusId) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisEnrollmentStatusDAO mapper = session.getMapper(MyBatisEnrollmentStatusDAO.class);
            mapper.delete(enrollmentStatusId);
            session.commit();
            LOGGER.info("Enrollment status deleted successfully with ID: {}", enrollmentStatusId);
        } catch (Exception e) {
            LOGGER.error("Error deleting enrollment status: {}", enrollmentStatusId, e);
            throw new RuntimeException("Failed to delete enrollment status", e);
        }
    }
}
