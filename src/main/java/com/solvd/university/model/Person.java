package com.solvd.university.model;

import java.util.Objects;

public abstract class Person {

    protected Integer personId;
    protected String firstName;
    protected String lastName;
    protected String email;

    protected Person() {
        this.personId = null;
    }

    protected Person(String firstName, String lastName, String email) {
        this.personId = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return (
            Objects.equals(firstName, person.firstName) &&
            Objects.equals(lastName, person.lastName) &&
            Objects.equals(email, person.email)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }
}
