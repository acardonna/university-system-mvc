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
import com.solvd.university.model.Program;
import com.solvd.university.service.interfaces.ProgramJsonService;

public class ProgramJsonServiceImpl implements ProgramJsonService {

    private static final Logger LOGGER = LogManager.getLogger(ProgramJsonServiceImpl.class);
    private static final String JSON_FILE_PATH = "src/main/resources/json/programs.json";
    private static final String RESOURCE_PATH = "json/programs.json";
    private final ObjectMapper objectMapper;

    public ProgramJsonServiceImpl() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<Program> read() {
        LOGGER.info("Reading programs from JSON file: {}", RESOURCE_PATH);
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(RESOURCE_PATH)) {
            if (inputStream == null) {
                LOGGER.error("Could not find JSON file: {}", RESOURCE_PATH);
                return new ArrayList<>();
            }

            List<Program> programs = objectMapper.readValue(inputStream, new TypeReference<List<Program>>() {});
            LOGGER.info("Successfully read {} programs from JSON", programs.size());
            return programs;

        } catch (IOException e) {
            LOGGER.error("Error reading JSON file: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public void write(List<Program> programs) {
        LOGGER.info("Writing {} programs to JSON file: {}", programs.size(), JSON_FILE_PATH);
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(JSON_FILE_PATH))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, programs);
            LOGGER.info("Successfully wrote programs to JSON file");

        } catch (IOException e) {
            LOGGER.error("Error writing JSON file: {}", e.getMessage(), e);
        }
    }

    @Override
    public void displayPrograms() {
        List<Program> programs = read();

        if (programs.isEmpty()) {
            LOGGER.warn("No programs found in JSON file");
            return;
        }

        LOGGER.info("==================== PROGRAMS FROM JSON ====================");
        for (Program program : programs) {
            LOGGER.info("  - ID: {}, Name: {}, Duration: {} years, Price: ${}",
                program.getProgramId(),
                program.getName(),
                program.getDuration(),
                program.getRawPrice());
        }
        LOGGER.info("=============================================================");
    }

    @Override
    public void demonstrateReadWrite() {
        LOGGER.info("--- Reading Programs from JSON ---");
        List<Program> programs = read();

        if (!programs.isEmpty()) {
            displayPrograms();

            LOGGER.info("\n--- Writing half of the programs back to JSON ---");
            int halfSize = programs.size() / 2;
            List<Program> halfPrograms = programs.subList(0, Math.max(1, halfSize));
            LOGGER.info("Writing {} out of {} programs", halfPrograms.size(), programs.size());
            write(halfPrograms);
        }
    }
}
