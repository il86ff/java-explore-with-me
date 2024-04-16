package ru.practicum.exceptions;

public class SQLConstraintViolationException extends RuntimeException {
    public SQLConstraintViolationException(String message) {
        super(message);
    }
}