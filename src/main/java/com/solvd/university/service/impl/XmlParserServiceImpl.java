package com.solvd.university.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.solvd.university.model.DepartmentXml;
import com.solvd.university.model.DepartmentsWrapper;
import com.solvd.university.service.interfaces.XmlParserService;
import com.solvd.university.util.JAXBParser;
import com.solvd.university.util.StAXParser;

public class XmlParserServiceImpl implements XmlParserService {

    private static final Logger LOGGER = LogManager.getLogger(XmlParserServiceImpl.class);
    private static final String DEPARTMENTS_XML = "xml/departments.xml";
    private static final String DEPARTMENTS_XSD = "xml/departments.xsd";
    private static final String BUILDINGS_XML = "xml/buildings.xml";
    private final StAXParser staxParser;
    private final JAXBParser jaxbParser;

    public XmlParserServiceImpl() {
        this.staxParser = new StAXParser();
        this.jaxbParser = new JAXBParser();
    }

    @Override
    public List<Map<String, String>> parseDepartments() {
        LOGGER.info("Parsing departments from XML file");
        return staxParser.parse(DEPARTMENTS_XML);
    }

    @Override
    public List<Map<String, String>> parseBuildings() {
        LOGGER.info("Parsing buildings from XML file");
        return staxParser.parse(BUILDINGS_XML);
    }

    @Override
    public void displayDepartments() {
        List<Map<String, String>> departments = parseDepartments();

        if (departments.isEmpty()) {
            LOGGER.warn("No departments found in XML file");
            return;
        }

        LOGGER.info("==================== DEPARTMENTS FROM XML ====================");
        for (Map<String, String> dept : departments) {
            LOGGER.info(
                "Department [ID: {}, Name: {}, Code: {}, University ID: {}]",
                dept.get("department_id"),
                dept.get("name"),
                dept.get("code"),
                dept.get("university_id")
            );
        }
        LOGGER.info("===============================================================");
    }

    @Override
    public void displayBuildings() {
        List<Map<String, String>> buildings = parseBuildings();

        if (buildings.isEmpty()) {
            LOGGER.warn("No buildings found in XML file");
            return;
        }

        LOGGER.info("===================== BUILDINGS FROM XML =====================");
        for (Map<String, String> building : buildings) {
            LOGGER.info(
                "Building [ID: {}, Name: {}, University ID: {}]",
                building.get("building_id"),
                building.get("name"),
                building.get("university_id")
            );
        }
        LOGGER.info("===============================================================");
    }

    @Override
    public DepartmentsWrapper parseDepartmentsWithJAXB() {
        LOGGER.info("Parsing departments with JAXB and XSD validation");
        return jaxbParser.parseAndValidate(DEPARTMENTS_XML, DEPARTMENTS_XSD);
    }

    @Override
    public void displayDepartmentsWithJAXB() {
        DepartmentsWrapper wrapper = parseDepartmentsWithJAXB();

        if (wrapper == null || wrapper.getDepartments() == null || wrapper.getDepartments().isEmpty()) {
            LOGGER.warn("No departments found using JAXB parsing");
            return;
        }

        LOGGER.info("=============== DEPARTMENTS FROM XML (JAXB) ================");
        for (DepartmentXml dept : wrapper.getDepartments()) {
            LOGGER.info(dept.toString());
        }
        LOGGER.info("==============================================================");
    }
}
