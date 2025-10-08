package com.solvd.university.model;

import java.util.Objects;

public abstract class Department<T> {

    protected Integer departmentId;
    protected Integer universityId;
    protected String name;
    protected T departmentCode;

    public Department(String name) {
        this.departmentId = null;
        this.universityId = null;
        this.name = name;
    }

    public Department() {}

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public abstract T getDepartmentCode();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Department<?> that = (Department<?>) o;
        return Objects.equals(name, that.name) && Objects.equals(departmentCode, that.departmentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, departmentCode);
    }

    @Override
    public String toString() {
        return "Department{" + "name='" + name + '\'' + ", code='" + getDepartmentCode() + '\'' + '}';
    }
}
