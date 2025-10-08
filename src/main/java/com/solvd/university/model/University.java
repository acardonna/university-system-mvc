package com.solvd.university.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class University {

    private Integer universityId;
    private String name;
    private List<Program> programCatalog;
    private List<Student> studentRegistry;
    private HashMap<String, Student> studentLookup;
    private List<Enrollment> enrollments;
    private LinkedList<Student> enrollmentQueue;
    private List<Professor> professorRegistry;
    private List<Course<?, ?>> courseCatalog;
    private List<Classroom> classrooms;

    public University(String name) {
        this.universityId = null;
        this.name = name;
        programCatalog = new ArrayList<>();
        studentRegistry = new ArrayList<>();
        studentLookup = new HashMap<>();
        enrollments = new ArrayList<>();
        enrollmentQueue = new LinkedList<>();
        professorRegistry = new ArrayList<>();
        courseCatalog = new ArrayList<>();
        classrooms = new ArrayList<>();
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void registerStudent(Student student) {
        studentRegistry.add(student);
        studentLookup.put(student.getId(), student);
    }

    public Student findStudentById(String id) {
        return studentLookup.get(id);
    }

    public void addToEnrollmentQueue(Student student) {
        enrollmentQueue.offer(student);
    }

    public Student processNextEnrollment() {
        return enrollmentQueue.poll();
    }

    public boolean hasEnrollmentQueue() {
        return !enrollmentQueue.isEmpty();
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void addProfessor(Professor professor) {
        professorRegistry.add(professor);
    }

    public void addCourse(Course<?, ?> course) {
        courseCatalog.add(course);
    }

    public void addClassroom(Classroom classroom) {
        classrooms.add(classroom);
    }

    public List<Program> getProgramCatalog() {
        return programCatalog;
    }

    public void setProgramCatalog(List<Program> programCatalog) {
        this.programCatalog = programCatalog;
    }

    public List<Student> getStudentRegistry() {
        return studentRegistry;
    }

    public void setStudentRegistry(List<Student> studentRegistry) {
        this.studentRegistry = studentRegistry;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Professor> getProfessorRegistry() {
        return professorRegistry;
    }

    public void setProfessorRegistry(List<Professor> professorRegistry) {
        this.professorRegistry = professorRegistry;
    }

    public List<Course<?, ?>> getCourseCatalog() {
        return courseCatalog;
    }

    public void setCourseCatalog(List<Course<?, ?>> courseCatalog) {
        this.courseCatalog = courseCatalog;
    }

    public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public HashMap<String, Student> getStudentLookup() {
        return studentLookup;
    }

    public void setStudentLookup(HashMap<String, Student> studentLookup) {
        this.studentLookup = studentLookup;
    }

    public LinkedList<Student> getEnrollmentQueue() {
        return enrollmentQueue;
    }

    public void setEnrollmentQueue(LinkedList<Student> enrollmentQueue) {
        this.enrollmentQueue = enrollmentQueue;
    }

    public List<Student> findStudents(StudentFilter filter) {
        return studentRegistry.stream().filter(filter::matches).collect(Collectors.toList());
    }

    public List<String> formatCourses(CourseFormatter formatter) {
        return courseCatalog.stream().map(formatter::format).collect(Collectors.toList());
    }

    public void addGradeToAllStudentsInCourse(Course<?, ?> course, Grade<Double> grade, GradeValidator validator) {
        studentRegistry
            .stream()
            .filter(student -> student.isEnrolledInCourse(course))
            .forEach(student -> {
                try {
                    student.addGrade(grade, validator);
                } catch (IllegalArgumentException e) {
                    System.out.println(
                        "Failed to add grade for student " + student.getFullName() + ": " + e.getMessage()
                    );
                }
            });
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return (
            Objects.equals(name, that.name) &&
            Objects.equals(programCatalog, that.programCatalog) &&
            Objects.equals(studentRegistry, that.studentRegistry) &&
            Objects.equals(enrollments, that.enrollments) &&
            Objects.equals(professorRegistry, that.professorRegistry) &&
            Objects.equals(courseCatalog, that.courseCatalog) &&
            Objects.equals(classrooms, that.classrooms)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            name,
            programCatalog,
            studentRegistry,
            enrollments,
            professorRegistry,
            courseCatalog,
            classrooms
        );
    }

    @Override
    public String toString() {
        return (
            "University{" +
            "name='" +
            name +
            '\'' +
            ", programCatalog=" +
            programCatalog +
            ", studentRegistry=" +
            studentRegistry +
            ", enrollments=" +
            enrollments +
            ", professorRegistry=" +
            professorRegistry +
            ", courseCatalog=" +
            courseCatalog +
            ", classrooms=" +
            classrooms +
            '}'
        );
    }
}
