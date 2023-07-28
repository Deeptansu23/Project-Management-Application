package com.projectmanagement.exception;

public class NoSuchDepartmentExistException extends RuntimeException{
    private String message;
    public NoSuchDepartmentExistException(){
    }
    public NoSuchDepartmentExistException(String message){
        super(message);
        this.message=message;
    }
}
