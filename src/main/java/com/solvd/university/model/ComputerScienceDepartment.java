package com.solvd.university.model;

public class ComputerScienceDepartment extends Department<String> {

    public ComputerScienceDepartment() {
        super("Computer Science");
        this.departmentCode = "CS";
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }
}
