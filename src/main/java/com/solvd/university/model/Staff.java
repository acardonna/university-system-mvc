package com.solvd.university.model;

public abstract class Staff extends Person {

    protected Integer staffId;
    protected Integer departmentId;
    protected Department<?> department;
    protected String title;

    protected Staff() {
        super();
        this.staffId = null;
        this.departmentId = null;
    }

    protected Staff(String firstName, String lastName, String email, Department<?> department, String title) {
        super(firstName, lastName, email);
        this.staffId = null;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.department = department;
        this.title = title;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Department<?> getDepartment() {
        return department;
    }

    public void setDepartment(Department<?> department) {
        this.department = department;
        this.departmentId = department != null ? department.getDepartmentId() : null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
