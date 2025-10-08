package com.solvd.university.model;

import com.solvd.university.model.annotation.RequiredExperience;
import com.solvd.university.model.exception.InvalidPaymentException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Student extends Person implements Identifiable, Enrollable, Payable {

    private Integer studentId;
    private int age;
    private final int studentNumber;
    private final String id;
    private Program enrolledProgram;
    private EnrollmentStatus enrollmentStatus;
    private GradeLevel gradeLevel;
    private boolean isRegistered;
    private double balance;
    private List<Grade<Double>> grades;
    private HashSet<Course<?, ?>> enrolledCourses;
    private static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.of("en", "US"));

    public Student(String firstName, String lastName, int age, String email) {
        super(firstName, lastName, email);
        this.studentId = null;
        this.age = age;
        enrolledProgram = null;
        enrollmentStatus = EnrollmentStatus.APPLIED;
        gradeLevel = GradeLevel.FRESHMAN;
        isRegistered = false;
        studentNumber = generateRandomId();
        id = "STU-" + studentNumber;
        balance = 0.0;
        this.grades = new ArrayList<>();
        this.enrolledCourses = new HashSet<>();
    }

    private static int generateRandomId() {
        return Math.abs(UUID.randomUUID().hashCode() % 900000) + 100000;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public Program getEnrolledProgram() {
        return enrolledProgram;
    }

    public void setEnrolledProgram(Program enrolledProgram) {
        this.enrolledProgram = enrolledProgram;
    }

    public EnrollmentStatus getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public GradeLevel getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(GradeLevel gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    @RequiredExperience(level = 2)
    public void advanceGradeLevel() {
        if (gradeLevel.getYear() == 1) {
            gradeLevel = GradeLevel.SOPHOMORE;
        } else if (gradeLevel.getYear() == 2) {
            gradeLevel = GradeLevel.JUNIOR;
        } else if (gradeLevel.getYear() == 3) {
            gradeLevel = GradeLevel.SENIOR;
        } else if (gradeLevel.getYear() == 4) {
            gradeLevel = GradeLevel.GRADUATE;
        }
    }

    public boolean isEnrolled() {
        return enrollmentStatus == EnrollmentStatus.ENROLLED && enrolledProgram != null;
    }

    public void setEnrolled(boolean enrolled) {
        this.enrollmentStatus = enrolled ? EnrollmentStatus.ENROLLED : EnrollmentStatus.APPLIED;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public void addGrade(Grade<Double> grade) {
        grades.add(grade);
    }

    public void addGrade(Grade<Double> grade, GradeValidator validator) {
        if (validator.isValid(grade.value())) {
            grades.add(grade);
        } else {
            throw new IllegalArgumentException("Invalid grade value: " + grade.value());
        }
    }

    public List<Grade<Double>> getGrades() {
        return new ArrayList<>(grades);
    }

    public List<Grade<Double>> getGradesForSemester(String semester) {
        return grades
            .stream()
            .filter(grade -> grade.semester().equals(semester))
            .toList();
    }

    public double calculateAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        return grades.stream().mapToDouble(Grade::value).average().orElse(0.0);
    }

    public double calculateSemesterAverage(String semester) {
        List<Grade<Double>> semesterGrades = getGradesForSemester(semester);
        if (semesterGrades.isEmpty()) {
            return 0.0;
        }
        return semesterGrades.stream().mapToDouble(Grade::value).average().orElse(0.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return studentNumber == student.studentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }

    @Override
    public String toString() {
        return (
            "Student{" +
            "name='" +
            getFullName() +
            '\'' +
            ", age=" +
            age +
            ", email='" +
            email +
            '\'' +
            ", studentNumber=" +
            studentNumber +
            ", gradeLevel=" +
            gradeLevel.getDisplayName() +
            " (Year " +
            gradeLevel.getYear() +
            ")" +
            ", enrolledProgram=" +
            enrolledProgram +
            ", enrollmentStatus=" +
            enrollmentStatus +
            ", isRegistered=" +
            isRegistered +
            ", balance=" +
            balance +
            ", averageGrade=" +
            String.format("%.2f", calculateAverageGrade()) +
            ", totalGrades=" +
            grades.size() +
            '}'
        );
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void enroll(Program program) {
        this.enrolledProgram = program;
        this.enrollmentStatus = EnrollmentStatus.ENROLLED;
        this.balance += program.getRawPrice();
    }

    @Override
    public double getOutstandingBalance() {
        return balance;
    }

    public void setOutstandingBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public void makePayment(double amount) throws InvalidPaymentException {
        if (amount <= 0) {
            throw new InvalidPaymentException("Payment must be greater than zero.");
        }
        if (amount > balance) {
            throw new InvalidPaymentException(
                "Payment amount ($" +
                    String.format("%.2f", amount) +
                    ") exceeds outstanding balance ($" +
                    String.format("%.2f", balance) +
                    "). Please enter a valid payment amount."
            );
        }
        balance = balance - amount;
    }

    public String getOutstandingBalanceFormatted() {
        return numberFormat.format(balance);
    }

    public boolean enrollInCourse(Course<?, ?> course) {
        return enrolledCourses.add(course);
    }

    public boolean isEnrolledInCourse(Course<?, ?> course) {
        return enrolledCourses.contains(course);
    }

    public void dropCourse(Course<?, ?> course) {
        enrolledCourses.remove(course);
    }

    public Set<Course<?, ?>> getEnrolledCourses() {
        return new HashSet<>(enrolledCourses);
    }

    public int getEnrolledCoursesCount() {
        return enrolledCourses.size();
    }
}
