package com.solvd.university.model;

import java.util.Objects;

public class StudentGrade {

    private Integer studentGradeId;
    private Integer studentId;
    private String subject;
    private Double value;
    private Integer semester;

    private Student student;

    public StudentGrade() {
        this.studentGradeId = null;
        this.studentId = null;
    }

    public StudentGrade(Integer studentId, String subject, Double value, Integer semester) {
        this.studentGradeId = null;
        this.studentId = studentId;
        this.subject = subject;
        this.value = value;
        this.semester = semester;
        this.student = null;
    }

    public StudentGrade(Student student, String subject, Double value, Integer semester) {
        this.studentGradeId = null;
        this.studentId = student != null ? student.getStudentId() : null;
        this.subject = subject;
        this.value = value;
        this.semester = semester;
        this.student = student;
    }

    public Integer getStudentGradeId() {
        return studentGradeId;
    }

    public void setStudentGradeId(Integer studentGradeId) {
        this.studentGradeId = studentGradeId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
        this.studentId = student != null ? student.getStudentId() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGrade that = (StudentGrade) o;
        return (
            Objects.equals(studentGradeId, that.studentGradeId) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(value, that.value) &&
            Objects.equals(semester, that.semester)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentGradeId, studentId, subject, value, semester);
    }

    @Override
    public String toString() {
        String studentInfo = student != null ? student.getFullName() : "Student ID: " + studentId;
        return String.format(
            "StudentGrade{id=%d, student=%s, subject='%s', grade=%.2f, semester=%d}",
            studentGradeId,
            studentInfo,
            subject,
            value,
            semester
        );
    }
}
