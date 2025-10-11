package com.solvd.university.service.interfaces;

import java.util.List;
import java.util.Map;

import com.solvd.university.model.DepartmentsWrapper;

public interface XmlParserService {
    List<Map<String, String>> parseDepartments();

    List<Map<String, String>> parseBuildings();

    void displayDepartments();

    void displayBuildings();

    DepartmentsWrapper parseDepartmentsWithJAXB();

    void displayDepartmentsWithJAXB();
}
