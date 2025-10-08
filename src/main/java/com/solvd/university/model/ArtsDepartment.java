package com.solvd.university.model;

public class ArtsDepartment extends Department<String> {

    public ArtsDepartment() {
        super("Arts");
        this.departmentCode = "ART";
    }

    @Override
    public String getDepartmentCode() {
        return departmentCode;
    }
}
