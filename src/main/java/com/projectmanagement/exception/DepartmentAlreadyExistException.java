package com.projectmanagement.exception;

public class DepartmentAlreadyExistException extends RuntimeException{

    private String message;

    public DepartmentAlreadyExistException() {
    }

    public DepartmentAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
