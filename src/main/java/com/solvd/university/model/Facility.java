package com.solvd.university.model;

import java.util.Objects;

public abstract class Facility {

    protected String name;

    protected Facility(String name) {
        this.name = name;
    }

    protected Facility() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facility facility = (Facility) o;
        return Objects.equals(name, facility.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
