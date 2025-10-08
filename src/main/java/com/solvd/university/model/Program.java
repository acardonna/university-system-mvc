package com.solvd.university.model;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class Program implements Identifiable {

    private Integer programId;
    private Integer departmentId;
    private Integer universityId;
    private String name;
    private int duration;
    double price;
    Department<?> department;
    private static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.of("en", "US"));
    private final String id;

    public Program(String name, int duration, double price, Department<?> department) {
        this.programId = null;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.universityId = department != null ? department.getUniversityId() : null;
        this.name = name;
        this.duration = duration;
        this.price = price;
        this.department = department;
        this.id =
            (department != null ? department.getDepartmentCode() : "GEN") +
            "-PRG-" +
            (Math.abs(Objects.hash(name, duration, price)) % 100000);
    }

    public Program() {
        this.programId = null;
        this.departmentId = null;
        this.universityId = null;
        this.id = "GEN-PRG-0";
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return numberFormat.format(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Department<?> getDepartment() {
        return department;
    }

    public void setDepartment(Department<?> department) {
        this.department = department;
        this.departmentId = department != null ? department.getDepartmentId() : null;
        this.universityId = department != null ? department.getUniversityId() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Program program = (Program) o;
        return (
            duration == program.duration &&
            Double.compare(price, program.price) == 0 &&
            Objects.equals(name, program.name) &&
            Objects.equals(department, program.department)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, duration, price, department);
    }

    @Override
    public String toString() {
        return String.format("%s | %d years | %s", name, duration, numberFormat.format(price));
    }

    public double getRawPrice() {
        return price;
    }

    @Override
    public String getId() {
        return id;
    }
}
