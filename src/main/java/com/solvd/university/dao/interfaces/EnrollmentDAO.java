package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Enrollment;
import com.solvd.university.model.Student;
import java.util.List;
import java.util.Optional;

public interface EnrollmentDAO {
    void save(Enrollment enrollment);

    Optional<Enrollment> findById(Integer enrollmentId);

    Optional<Enrollment> findByStudent(Student student);

    List<Enrollment> findAll();

    void update(Enrollment enrollment);

    void delete(Integer enrollmentId);
}
