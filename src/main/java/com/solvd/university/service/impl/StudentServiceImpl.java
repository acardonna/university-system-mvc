package com.solvd.university.service.impl;

import com.solvd.university.dao.interfaces.StudentDAO;
import com.solvd.university.model.Grade;
import com.solvd.university.model.Student;
import com.solvd.university.model.StudentFilter;
import com.solvd.university.model.StudentGrade;
import com.solvd.university.model.exception.DuplicateRegistrationException;
import com.solvd.university.model.exception.StudentNotFoundException;
import com.solvd.university.service.interfaces.StudentGradeService;
import com.solvd.university.service.interfaces.StudentService;
import java.util.List;
import java.util.stream.Collectors;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;
    private final StudentGradeService studentGradeService;

    public StudentServiceImpl(StudentDAO studentDAO, StudentGradeService studentGradeService) {
        this.studentDAO = studentDAO;
        this.studentGradeService = studentGradeService;
    }

    @Override
    public Student registerStudent(String firstName, String lastName, int age, String email)
        throws DuplicateRegistrationException {
        if (studentDAO.findByEmail(email).isPresent()) {
            throw new DuplicateRegistrationException(
                "Student with email '" + email + "' is already registered. Please log in instead."
            );
        }

        Student student = new Student(firstName, lastName, age, email);
        student.setRegistered(true);
        studentDAO.save(student);

        return student;
    }

    @Override
    public Student authenticateStudent(String email, int studentId) throws StudentNotFoundException {
        Student student = studentDAO
            .findByEmailAndStudentNumber(email, studentId)
            .orElseThrow(() ->
                new StudentNotFoundException(
                    "No student found with email '" +
                        email +
                        "' and Student ID '" +
                        studentId +
                        "'. Please check your credentials."
                )
            );

        if (student.getStudentId() != null) {
            List<StudentGrade> dbGrades = studentGradeService.getGradesByStudent(student.getStudentId());

            for (StudentGrade dbGrade : dbGrades) {
                String semesterName = mapSemesterNumberToName(dbGrade.getSemester());
                Grade<Double> grade = new Grade<>(dbGrade.getSubject(), dbGrade.getValue(), semesterName);
                student.addGrade(grade);
            }
        }

        return student;
    }

    private String mapSemesterNumberToName(Integer semesterNumber) {
        if (semesterNumber == null) {
            return "Fall 2024";
        }

        int year = 2024 + (semesterNumber - 1) / 2;
        String season = (semesterNumber % 2 == 1) ? "Fall" : "Spring";
        return season + " " + year;
    }

    @Override
    public Student getStudentById(String id) throws StudentNotFoundException {
        return studentDAO
            .findById(id)
            .orElseThrow(() -> new StudentNotFoundException("Student with ID '" + id + "' not found."));
    }

    @Override
    public List<Student> getStudents(StudentFilter filter) {
        return studentDAO.findAll().stream().filter(filter::matches).collect(Collectors.toList());
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(String id) {
        studentDAO.delete(id);
    }
}
