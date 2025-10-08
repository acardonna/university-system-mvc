package com.solvd.university.model;

import com.solvd.university.model.annotation.RequiredExperience;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredExperience(level = 5)
public class Professor extends Staff implements Teachable {

    private Integer professorId;
    private final List<Course<?, ?>> assignedCourses = new ArrayList<>();

    public Professor(String firstName, String lastName, Department<?> department, String title) {
        super(
            firstName,
            lastName,
            firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.edu",
            department,
            title
        );
        this.professorId = null;
    }

    public Professor() {
        super();
        this.professorId = null;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    @RequiredExperience(level = 4)
    @Override
    public void assignCourse(Course<?, ?> course) {
        if (course == null) {
            return;
        }

        if (course.getProfessor() != this) {
            course.setProfessor(this);
        }

        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public List<Course<?, ?>> getAssignedCourses() {
        return assignedCourses;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Professor professor = (Professor) o;
        return (
            Objects.equals(firstName, professor.firstName) &&
            Objects.equals(lastName, professor.lastName) &&
            Objects.equals(department, professor.department) &&
            Objects.equals(title, professor.title)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, department, title);
    }

    @Override
    public String toString() {
        String deptName = department != null ? department.getName() : "Unassigned";
        return String.format("%s %s | %s Department", title, getFullName(), deptName);
    }
}
