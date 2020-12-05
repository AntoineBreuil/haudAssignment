package com.antoine.assignment.haud.exceptions;

import lombok.Getter;

@Getter
public class AssignmentException extends RuntimeException {
    private final String error;

    public AssignmentException(String error, String description) {
        super(description);
        this.error = error;
    }
}
