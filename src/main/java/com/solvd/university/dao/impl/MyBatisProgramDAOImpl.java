package com.solvd.university.dao.impl;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.interfaces.MyBatisProgramDAO;
import com.solvd.university.model.Department;
import com.solvd.university.model.Program;
import com.solvd.university.util.MyBatisSessionHolder;

public class MyBatisProgramDAOImpl implements MyBatisProgramDAO {

    private static final Logger LOGGER = LogManager.getLogger(MyBatisProgramDAOImpl.class);

    @Override
    public void save(Program program) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            mapper.save(program);
            session.commit();
            LOGGER.info("Saved program: {} (ID: {})", program.getName(), program.getProgramId());
        } catch (Exception e) {
            LOGGER.error("Error saving program", e);
            throw new RuntimeException("Failed to save program", e);
        }
    }

    @Override
    public Optional<Program> findById(Integer programId) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            return mapper.findById(programId);
        } catch (Exception e) {
            LOGGER.error("Error finding program by id: {}", programId, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Program> findByName(String name) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            return mapper.findByName(name);
        } catch (Exception e) {
            LOGGER.error("Error finding program by name: {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Program> findByDepartment(Department<?> department) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            return mapper.findByDepartment(department);
        } catch (Exception e) {
            LOGGER.error("Error finding programs by department", e);
            return List.of();
        }
    }

    @Override
    public List<Program> findAll() {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            return mapper.findAll();
        } catch (Exception e) {
            LOGGER.error("Error finding all programs", e);
            return List.of();
        }
    }

    @Override
    public void update(Program program) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            mapper.update(program);
            session.commit();
            LOGGER.info("Updated program: {} (ID: {})", program.getName(), program.getProgramId());
        } catch (Exception e) {
            LOGGER.error("Error updating program", e);
            throw new RuntimeException("Failed to update program", e);
        }
    }

    @Override
    public void delete(Integer programId) {
        try (SqlSession session = MyBatisSessionHolder.getSqlSession()) {
            MyBatisProgramDAO mapper = session.getMapper(MyBatisProgramDAO.class);
            mapper.delete(programId);
            session.commit();
            LOGGER.info("Deleted program with ID: {}", programId);
        } catch (Exception e) {
            LOGGER.error("Error deleting program: {}", programId, e);
            throw new RuntimeException("Failed to delete program", e);
        }
    }
}
