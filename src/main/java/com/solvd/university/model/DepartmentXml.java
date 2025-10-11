package com.solvd.university.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"id", "name", "code", "universityId"})
public class DepartmentXml {

    private Long id;
    private String name;
    private String code;
    private Long universityId;

    public DepartmentXml() {
    }

    public DepartmentXml(Long id, String name, String code, Long universityId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.universityId = universityId;
    }

    @XmlElement(name = "department_id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlElement(name = "university_id")
    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    @Override
    public String toString() {
        return String.format("Department [ID: %d, Name: %s, Code: %s, University ID: %d]",
                id, name, code, universityId);
    }
}
