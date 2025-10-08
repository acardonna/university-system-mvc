package com.solvd.university.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class CourseGrade {

    private Integer courseGradeId;
    private Integer courseId;
    private String subject;
    private Double value;
    private Integer semester;
    private LocalDateTime recordedAt;

    private Course<?, ?> course;

    public CourseGrade() {
        this.courseGradeId = null;
        this.courseId = null;
    }

    public CourseGrade(Integer courseId, String subject, Double value, Integer semester, LocalDateTime recordedAt) {
        this.courseGradeId = null;
        this.courseId = courseId;
        this.subject = subject;
        this.value = value;
        this.semester = semester;
        this.recordedAt = recordedAt != null ? recordedAt : LocalDateTime.now();
        this.course = null;
    }

    public CourseGrade(Course<?, ?> course, String subject, Double value, Integer semester, LocalDateTime recordedAt) {
        this.courseGradeId = null;
        this.courseId = course != null ? course.getCourseId() : null;
        this.subject = subject;
        this.value = value;
        this.semester = semester;
        this.recordedAt = recordedAt != null ? recordedAt : LocalDateTime.now();
        this.course = course;
    }

    public Integer getCourseGradeId() {
        return courseGradeId;
    }

    public void setCourseGradeId(Integer courseGradeId) {
        this.courseGradeId = courseGradeId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public Course<?, ?> getCourse() {
        return course;
    }

    public void setCourse(Course<?, ?> course) {
        this.course = course;
        this.courseId = course != null ? course.getCourseId() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseGrade that = (CourseGrade) o;
        return (
            Objects.equals(courseGradeId, that.courseGradeId) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(value, that.value) &&
            Objects.equals(semester, that.semester) &&
            Objects.equals(recordedAt, that.recordedAt)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseGradeId, courseId, subject, value, semester, recordedAt);
    }

    @Override
    public String toString() {
        String courseInfo = course != null ? course.getCourseCode() : "Course ID: " + courseId;
        return String.format(
            "CourseGrade{id=%d, course=%s, subject='%s', grade=%.2f, semester=%d, recorded=%s}",
            courseGradeId,
            courseInfo,
            subject,
            value,
            semester,
            recordedAt
        );
    }
}
