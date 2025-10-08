package com.solvd.university.model;

public class MathematicsDepartment extends Department<String> {

    public MathematicsDepartment() {
        super("Mathematics");
        this.departmentCode = "MATH";
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }
}
