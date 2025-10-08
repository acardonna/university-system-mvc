package com.solvd.university.dao.interfaces;

import com.solvd.university.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentDAO {
    void save(Student student);

    Optional<Student> findById(String id);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByEmailAndStudentNumber(String email, int studentNumber);

    List<Student> findAll();

    void update(Student student);

    void delete(String id);
}
