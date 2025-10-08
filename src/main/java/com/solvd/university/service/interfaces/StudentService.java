package com.solvd.university.service.interfaces;

import com.solvd.university.model.Student;
import com.solvd.university.model.StudentFilter;
import com.solvd.university.model.exception.DuplicateRegistrationException;
import com.solvd.university.model.exception.StudentNotFoundException;
import java.util.List;

public interface StudentService {
    Student registerStudent(String firstName, String lastName, int age, String email)
        throws DuplicateRegistrationException;

    Student authenticateStudent(String email, int studentId) throws StudentNotFoundException;

    Student getStudentById(String id) throws StudentNotFoundException;

    List<Student> getStudents(StudentFilter filter);

    List<Student> getAllStudents();

    void updateStudent(Student student);

    void deleteStudent(String id);
}
