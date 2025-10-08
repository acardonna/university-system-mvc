package com.solvd.university.model;

public class EngineeringDepartment extends Department<String> {

    public EngineeringDepartment() {
        super("Engineering");
        this.departmentCode = "ENG";
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }
}
