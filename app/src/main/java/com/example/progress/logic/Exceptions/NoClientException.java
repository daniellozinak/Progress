package com.example.progress.logic.Exceptions;

public class NoClientException extends Exception {
    private final static String MESSAGE = "Client has not been chosen";

    public NoClientException()
    {
        super(MESSAGE);
    }
}
