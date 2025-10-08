package com.solvd.university.model;

public class BusinessDepartment extends Department<String> {

    public BusinessDepartment() {
        super("Business");
        this.departmentCode = "BUS";
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }
}
