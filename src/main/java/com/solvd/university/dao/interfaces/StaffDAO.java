package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Staff;
import java.util.List;
import java.util.Optional;

public interface StaffDAO {
    void save(Staff staff);

    Optional<Staff> findById(Integer staffId);

    Optional<Staff> findByPersonId(Integer personId);

    List<Staff> findAll();

    List<Staff> findByDepartment(Integer departmentId);

    void update(Staff staff);

    void delete(Integer staffId);
}
