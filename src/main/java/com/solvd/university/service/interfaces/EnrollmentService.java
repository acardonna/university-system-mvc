package com.solvd.university.service.interfaces;

import com.solvd.university.model.Enrollment;
import com.solvd.university.model.Program;
import com.solvd.university.model.Student;
import com.solvd.university.model.exception.AlreadyEnrolledException;
import com.solvd.university.model.exception.InvalidPaymentException;
import com.solvd.university.model.exception.StudentNotEnrolledException;
import java.util.List;

public interface EnrollmentService {
    Enrollment enrollStudent(Student student, Program program) throws AlreadyEnrolledException;

    Enrollment getEnrollmentByStudent(Student student) throws StudentNotEnrolledException;

    List<Enrollment> getAllEnrollments();

    void updateEnrollment(Enrollment enrollment);

    void deleteEnrollment(Integer enrollmentId);

    void processPayment(Student student, double amount) throws StudentNotEnrolledException, InvalidPaymentException;

    boolean isStudentEnrolled(Student student);
}
