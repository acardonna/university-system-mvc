package com.solvd.university.model.exception;

public class InvalidPaymentException extends Exception {

    public InvalidPaymentException() {
        super("Invalid payment amount. Please enter a valid payment.");
    }

    public InvalidPaymentException(String message) {
        super(message);
    }
}
