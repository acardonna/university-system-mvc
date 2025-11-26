package com.solvd.university;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.model.GradeLevel;
import com.solvd.university.model.Student;
import com.solvd.university.model.exception.InvalidPaymentException;

public class StudentTest {

    @Test
    public void verifyStudentConstructorTest() {
        Student student = new Student("John", "Doe", 20, "john.doe@example.com");
        Assert.assertEquals(student.getFirstName(), "John");
        Assert.assertEquals(student.getLastName(), "Doe");
        Assert.assertEquals(student.getAge(), 20);
        Assert.assertEquals(student.getEmail(), "john.doe@example.com");
    }

    @Test
    public void verifyStudentGradeLevelAdvanceTest() {
        Student student = new Student("Jane", "Smith", 19, "jane@example.com");
        // Default is FRESHMAN (Year 1)
        Assert.assertEquals(student.getGradeLevel(), GradeLevel.FRESHMAN);

        student.advanceGradeLevel();
        Assert.assertEquals(student.getGradeLevel(), GradeLevel.SOPHOMORE, "Student should advance to Sophomore");
    }

    @Test
    public void verifyStudentPaymentTest() throws InvalidPaymentException {
        Student student = new Student("Bob", "Brown", 22, "bob@example.com");
        student.setOutstandingBalance(1000.0);
        student.makePayment(500.0);
        Assert.assertEquals(student.getOutstandingBalance(), 500.0, 0.01, "Balance should decrease by payment amount");
    }

    @Test(expectedExceptions = InvalidPaymentException.class)
    public void verifyInvalidPaymentExceptionTest() throws InvalidPaymentException {
        Student student = new Student("Alice", "White", 21, "alice@example.com");
        student.setOutstandingBalance(100.0);
        // Attempt to pay more than balance
        student.makePayment(200.0);
    }

    @Test
    public void verifyStudentEnrollmentStatusTest() {
        Student student = new Student("Charlie", "Green", 23, "charlie@example.com");
        student.setEnrolled(true);
        Assert.assertEquals(student.getEnrollmentStatus(), EnrollmentStatus.ENROLLED, "Student should be enrolled");

        student.setEnrolled(false);
        Assert.assertEquals(student.getEnrollmentStatus(), EnrollmentStatus.APPLIED, "Student should revert to APPLIED status");
    }
}
