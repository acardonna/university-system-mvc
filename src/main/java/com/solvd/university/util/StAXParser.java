package com.solvd.university.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StAXParser {

    private static final Logger LOGGER = LogManager.getLogger(StAXParser.class);
    private final XMLInputFactory factory;

    public StAXParser() {
        this.factory = XMLInputFactory.newInstance();
    }

    public List<Map<String, String>> parse(String xmlFilePath) {
        List<Map<String, String>> records = new LinkedList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(xmlFilePath)) {
            if (inputStream == null) {
                LOGGER.error("Could not find XML file: {}", xmlFilePath);
                return records;
            }

            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
            try {
                Map<String, String> currentRecord = null;
                String currentElement = null;

                while (reader.hasNext()) {
                    int event = reader.next();

                    switch (event) {
                        case XMLStreamConstants.START_ELEMENT:
                            String elementName = reader.getLocalName();

                            if (isDepartmentOrBuilding(elementName)) {
                                currentRecord = new HashMap<>();
                            } else if (currentRecord != null) {
                                currentElement = elementName;
                            }
                            break;

                        case XMLStreamConstants.CHARACTERS:
                            if (currentElement != null && currentRecord != null) {
                                String text = reader.getText().trim();
                                if (!text.isEmpty()) {
                                    currentRecord.put(currentElement, text);
                                }
                            }
                            break;

                        case XMLStreamConstants.END_ELEMENT:
                            String endElementName = reader.getLocalName();

                            if (isDepartmentOrBuilding(endElementName) && currentRecord != null) {
                                records.add(currentRecord);
                                currentRecord = null;
                            } else if (currentElement != null && currentElement.equals(endElementName)) {
                                currentElement = null;
                            }
                            break;
                    }
                }

                LOGGER.info("Successfully parsed {} records from {}", records.size(), xmlFilePath);
            } finally {
                reader.close();
            }

        } catch (XMLStreamException e) {
            LOGGER.error("Error parsing XML file: {}", xmlFilePath, e);
        } catch (Exception e) {
            LOGGER.error("Unexpected error while parsing XML file: {}", xmlFilePath, e);
        }

        return records;
    }

    private boolean isDepartmentOrBuilding(String elementName) {
        return "department".equals(elementName) || "building".equals(elementName);
    }
}
