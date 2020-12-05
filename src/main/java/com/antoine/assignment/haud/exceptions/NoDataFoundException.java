package com.antoine.assignment.haud.exceptions;

import lombok.Getter;

@Getter
public class NoDataFoundException extends RuntimeException {
    private final String error;

    public NoDataFoundException(String error) {
        super("error : " + error);
        this.error = error;
    }
}