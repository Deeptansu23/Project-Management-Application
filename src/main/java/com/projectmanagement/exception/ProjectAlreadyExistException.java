package com.projectmanagement.exception;

public class ProjectAlreadyExistException extends RuntimeException{
    private String message;

    public ProjectAlreadyExistException() {
    }

    public ProjectAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
