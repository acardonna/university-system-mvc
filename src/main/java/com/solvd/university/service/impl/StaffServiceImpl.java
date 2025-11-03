package com.solvd.university.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.dao.factory.DAOFactory;
import com.solvd.university.dao.interfaces.StaffDAO;
import com.solvd.university.model.ConcreteStaff;
import com.solvd.university.model.Department;
import com.solvd.university.model.Staff;
import com.solvd.university.service.interfaces.StaffService;

public class StaffServiceImpl implements StaffService {

    private static final Logger LOGGER = LogManager.getLogger(StaffServiceImpl.class);
    private final StaffDAO staffDAO;

    public StaffServiceImpl() {
        this.staffDAO = DAOFactory.create(StaffDAO.class);
    }

    @Override
    public Staff addStaff(String firstName, String lastName, String email, Department<?> department, String title) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        Staff staff = new ConcreteStaff(firstName, lastName, email, department, title);
        staffDAO.save(staff);
        LOGGER.info("Staff member '{}' added successfully with title '{}'", staff.getFullName(), title);
        return staff;
    }

    @Override
    public Staff getStaffById(Integer staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID cannot be null");
        }

        return staffDAO
            .findById(staffId)
            .orElseThrow(() -> new RuntimeException("Staff member not found with ID: " + staffId));
    }

    @Override
    public Staff getStaffByPersonId(Integer personId) {
        if (personId == null) {
            throw new IllegalArgumentException("Person ID cannot be null");
        }

        return staffDAO
            .findByPersonId(personId)
            .orElseThrow(() -> new RuntimeException("Staff member not found with person ID: " + personId));
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffDAO.findAll();
    }

    @Override
    public List<Staff> getStaffByDepartment(Integer departmentId) {
        if (departmentId == null) {
            throw new IllegalArgumentException("Department ID cannot be null");
        }

        return staffDAO.findByDepartment(departmentId);
    }

    @Override
    public void updateStaff(Staff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null");
        }
        if (staff.getStaffId() == null) {
            throw new IllegalArgumentException("Staff ID cannot be null for update");
        }
        if (staff.getPersonId() == null) {
            throw new IllegalArgumentException("Person ID cannot be null for update");
        }
        if (staff.getTitle() == null || staff.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        staffDAO.update(staff);
        LOGGER.info("Staff member '{}' updated successfully", staff.getFullName());
    }

    @Override
    public void deleteStaff(Integer staffId) {
        if (staffId == null) {
            throw new IllegalArgumentException("Staff ID cannot be null");
        }

        staffDAO.delete(staffId);
        LOGGER.info("Staff member with ID {} deleted successfully", staffId);
    }
}
