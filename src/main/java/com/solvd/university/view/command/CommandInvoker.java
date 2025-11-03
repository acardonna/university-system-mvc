package com.solvd.university.view.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandInvoker {
    private static final Logger LOGGER = LogManager.getLogger(CommandInvoker.class);

    private final Map<Integer, Command> commands = new HashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    public void registerCommand(int id, Command command) {
        commands.put(id, command);
    }

    public boolean executeCommand(int id) {
        Command command = commands.get(id);
        if (command != null) {
            commandHistory.add(command.getDescription());
            command.execute();
            return true;
        }
        return false;
    }

    public String getCommandDescription(int id) {
        Command command = commands.get(id);
        return command != null ? command.getDescription() : null;
    }

    public void displayAvailableCommands() {
        commands.forEach((id, command) -> {
            LOGGER.info(id + ". " + command.getDescription());
        });
    }

    public void displayMenu(String headerMessage) {
        if (headerMessage != null && !headerMessage.isEmpty()) {
            LOGGER.info(headerMessage);
            LOGGER.info("");
        }
        displayAvailableCommands();
    }

    public boolean hasCommand(int id) {
        return commands.containsKey(id);
    }

    public List<String> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }

    public void clearHistory() {
        commandHistory.clear();
    }

    public int getCommandCount() {
        return commands.size();
    }
}
