package com.solvd.university.util;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.solvd.university.model.DepartmentsWrapper;

public class JAXBParser {

    private static final Logger LOGGER = LogManager.getLogger(JAXBParser.class);

    public DepartmentsWrapper parseAndValidate(String xmlFilePath, String xsdFilePath) {
        try {
            InputStream xmlStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath);
            InputStream xsdStream = getClass().getClassLoader().getResourceAsStream(xsdFilePath);

            if (xmlStream == null) {
                LOGGER.error("Could not find XML file: {}", xmlFilePath);
                return null;
            }

            if (xsdStream == null) {
                LOGGER.error("Could not find XSD file: {}", xsdFilePath);
                return null;
            }

            LOGGER.info("Validating XML file {} against schema {}", xmlFilePath, xsdFilePath);

            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new javax.xml.transform.stream.StreamSource(xsdStream));

            JAXBContext jaxbContext = JAXBContext.newInstance(DepartmentsWrapper.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(schema);

            DepartmentsWrapper departments = (DepartmentsWrapper) unmarshaller.unmarshal(xmlStream);

            LOGGER.info("Successfully parsed and validated {} departments from {}",
                    departments.getDepartments().size(), xmlFilePath);

            return departments;

        } catch (JAXBException e) {
            LOGGER.error("Error parsing XML with JAXB: {}", e.getMessage(), e);
            return null;
        } catch (SAXException e) {
            LOGGER.error("Error loading XSD schema: {}", e.getMessage(), e);
            return null;
        }
    }
}
