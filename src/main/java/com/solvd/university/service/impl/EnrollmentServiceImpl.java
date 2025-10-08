package com.solvd.university.service.impl;

import com.solvd.university.dao.interfaces.EnrollmentDAO;
import com.solvd.university.dao.interfaces.StudentDAO;
import com.solvd.university.model.Course;
import com.solvd.university.model.Enrollment;
import com.solvd.university.model.Grade;
import com.solvd.university.model.Program;
import com.solvd.university.model.Student;
import com.solvd.university.model.exception.AlreadyEnrolledException;
import com.solvd.university.model.exception.InvalidPaymentException;
import com.solvd.university.model.exception.StudentNotEnrolledException;
import com.solvd.university.service.interfaces.CourseGradeService;
import com.solvd.university.service.interfaces.CourseService;
import com.solvd.university.service.interfaces.EnrollmentService;
import com.solvd.university.service.interfaces.StudentGradeService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final CourseService courseService;
    private final StudentGradeService studentGradeService;
    private final CourseGradeService courseGradeService;
    private final Random random = new Random();

    public EnrollmentServiceImpl(
        EnrollmentDAO enrollmentDAO,
        StudentDAO studentDAO,
        CourseService courseService,
        StudentGradeService studentGradeService,
        CourseGradeService courseGradeService
    ) {
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.courseService = courseService;
        this.studentGradeService = studentGradeService;
        this.courseGradeService = courseGradeService;
    }

    @Override
    public Enrollment enrollStudent(Student student, Program program) throws AlreadyEnrolledException {
        if (student.isEnrolled()) {
            throw new AlreadyEnrolledException(
                "Student is already enrolled in '" +
                    student.getEnrolledProgram().getName() +
                    "'. Cannot enroll in multiple programs simultaneously."
            );
        }

        Enrollment enrollment = new Enrollment(student, program);
        enrollmentDAO.save(enrollment);

        student.enroll(program);

        assignRandomCoursesAndGrades(student);

        studentDAO.update(student);

        return enrollment;
    }

    private void assignRandomCoursesAndGrades(Student student) {
        Integer departmentId = student.getEnrolledProgram() != null
            ? student.getEnrolledProgram().getDepartmentId()
            : null;

        List<Course<?, ?>> departmentCourses = departmentId != null
            ? courseService.getCoursesByDepartment(departmentId)
            : courseService.getAllCourses();

        if (departmentCourses.isEmpty()) {
            return;
        }

        List<Course<?, ?>> shuffledCourses = new ArrayList<>(departmentCourses);
        Collections.shuffle(shuffledCourses, random);
        int numCoursesToAssign = Math.min(4, shuffledCourses.size());

        Set<Course<?, ?>> assignedCourses = new HashSet<>();

        for (int i = 0; i < numCoursesToAssign; i++) {
            Course<?, ?> course = shuffledCourses.get(i);

            if (assignedCourses.contains(course)) {
                continue;
            }

            boolean enrolled = student.enrollInCourse(course);

            if (!enrolled) {
                continue;
            }

            assignedCourses.add(course);

            double gradeValue = 70.0 + (random.nextDouble() * 30.0);
            gradeValue = Math.round(gradeValue * 100.0) / 100.0;

            Grade<Double> grade = new Grade<>(course.getCourseName(), gradeValue, "Fall 2024");
            student.addGrade(grade);

            if (student.getStudentId() != null) {
                studentGradeService.addGrade(student.getStudentId(), course.getCourseName(), gradeValue, 1);
            }

            if (course.getCourseId() != null) {
                courseGradeService.addGrade(
                    course.getCourseId(),
                    course.getCourseName(),
                    gradeValue,
                    1,
                    LocalDateTime.now()
                );
            }
        }
    }

    @Override
    public Enrollment getEnrollmentByStudent(Student student) throws StudentNotEnrolledException {
        return enrollmentDAO
            .findByStudent(student)
            .orElseThrow(() ->
                new StudentNotEnrolledException(
                    "Student must be enrolled in a program to view enrollment details. " +
                        "Please enroll in a program first."
                )
            );
    }

    @Override
    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }

    @Override
    public void updateEnrollment(Enrollment enrollment) {
        if (enrollment == null) {
            throw new IllegalArgumentException("Enrollment cannot be null");
        }
        enrollmentDAO.update(enrollment);
    }

    @Override
    public void deleteEnrollment(Integer enrollmentId) {
        if (enrollmentId == null) {
            throw new IllegalArgumentException("Enrollment ID cannot be null");
        }
        enrollmentDAO.delete(enrollmentId);
    }

    @Override
    public void processPayment(Student student, double amount)
        throws StudentNotEnrolledException, InvalidPaymentException {
        if (!student.isEnrolled()) {
            throw new StudentNotEnrolledException(
                "Student must be enrolled in a program to make a payment. " + "Please enroll in a program first."
            );
        }

        if (student.getOutstandingBalance() <= 0.0) {
            throw new InvalidPaymentException("There is no outstanding balance to pay.");
        }

        student.makePayment(amount);

        studentDAO.update(student);
    }

    @Override
    public boolean isStudentEnrolled(Student student) {
        return student.isEnrolled();
    }
}
