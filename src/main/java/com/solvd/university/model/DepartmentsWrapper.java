package com.solvd.university.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "departments")
public class DepartmentsWrapper {

    private List<DepartmentXml> departments;

    public DepartmentsWrapper() {
    }

    public DepartmentsWrapper(List<DepartmentXml> departments) {
        this.departments = departments;
    }

    @XmlElement(name = "department")
    public List<DepartmentXml> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentXml> departments) {
        this.departments = departments;
    }
}
