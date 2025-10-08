package com.solvd.university.model;

public class MessagePrinter {

    private static final MessagePrinter INSTANCE = new MessagePrinter();

    private MessagePrinter() {}

    public static MessagePrinter getInstance() {
        return INSTANCE;
    }

    public synchronized void printMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.println("[" + threadName + "] " + message);
    }
}
