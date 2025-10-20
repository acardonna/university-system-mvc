package com.solvd.university.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solvd.university.model.EnrollmentStatus;
import com.solvd.university.service.interfaces.EnrollmentStatusJsonService;

public class EnrollmentStatusJsonServiceImpl implements EnrollmentStatusJsonService {

    private static final Logger LOGGER = LogManager.getLogger(EnrollmentStatusJsonServiceImpl.class);
    private static final String JSON_FILE_PATH = "src/main/resources/json/enrollment_statuses.json";
    private static final String RESOURCE_PATH = "json/enrollment_statuses.json";
    private final ObjectMapper objectMapper;

    public EnrollmentStatusJsonServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<EnrollmentStatus> read() {
        LOGGER.info("Reading enrollment statuses from JSON file: {}", RESOURCE_PATH);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                LOGGER.error("Could not find JSON file: {}", RESOURCE_PATH);
                return new ArrayList<>();
            }

            List<EnrollmentStatus> statuses = objectMapper.readValue(inputStream, new TypeReference<List<EnrollmentStatus>>() {});
            LOGGER.info("Successfully read {} enrollment statuses from JSON", statuses.size());
            return statuses;

        } catch (IOException e) {
            LOGGER.error("Error reading JSON file: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public void write(List<EnrollmentStatus> statuses) {
        LOGGER.info("Writing {} enrollment statuses to JSON file: {}", statuses.size(), JSON_FILE_PATH);
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(JSON_FILE_PATH))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, statuses);
            LOGGER.info("Successfully wrote enrollment statuses to JSON file");

        } catch (IOException e) {
            LOGGER.error("Error writing JSON file: {}", e.getMessage(), e);
        }
    }

    @Override
    public void displayStatuses() {
        List<EnrollmentStatus> statuses = read();

        if (statuses.isEmpty()) {
            LOGGER.warn("No enrollment statuses found in JSON file");
            return;
        }

        LOGGER.info("================ ENROLLMENT STATUSES FROM JSON ================");
        for (EnrollmentStatus status : statuses) {
            LOGGER.info("  - ID: {}, Name: {}, Description: {}",
                status.getEnrollmentStatusId(),
                status.getDisplayName(),
                status.getDescription());
        }
        LOGGER.info("================================================================");
    }

    @Override
    public void demonstrateReadWrite() {
        LOGGER.info("--- Reading Enrollment Statuses from JSON ---");
        List<EnrollmentStatus> statuses = read();

        if (!statuses.isEmpty()) {
            displayStatuses();

            LOGGER.info("\n--- Writing half of the enrollment statuses back to JSON ---");
            int halfSize = statuses.size() / 2;
            List<EnrollmentStatus> halfStatuses = statuses.subList(0, Math.max(1, halfSize));
            LOGGER.info("Writing {} out of {} enrollment statuses", halfStatuses.size(), statuses.size());
            write(halfStatuses);
        }
    }
}
