package com.solvd.university.model;

import com.solvd.university.model.exception.InvalidPaymentException;

public interface Payable {
    double getOutstandingBalance();

    void makePayment(double amount) throws InvalidPaymentException;
}
