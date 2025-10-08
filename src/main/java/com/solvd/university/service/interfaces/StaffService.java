package com.solvd.university.service.interfaces;

import com.solvd.university.model.Department;
import com.solvd.university.model.Staff;
import java.util.List;

public interface StaffService {
    Staff addStaff(String firstName, String lastName, String email, Department<?> department, String title);

    Staff getStaffById(Integer staffId);

    Staff getStaffByPersonId(Integer personId);

    List<Staff> getAllStaff();

    List<Staff> getStaffByDepartment(Integer departmentId);

    void updateStaff(Staff staff);

    void deleteStaff(Integer staffId);
}
