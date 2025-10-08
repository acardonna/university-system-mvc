package com.solvd.university.model;

public class ConcreteStaff extends Staff {

    public ConcreteStaff() {
        super();
    }

    public ConcreteStaff(String firstName, String lastName, String email, Department<?> department, String title) {
        super(firstName, lastName, email, department, title);
    }
}
