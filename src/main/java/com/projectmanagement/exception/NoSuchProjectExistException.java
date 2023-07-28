package com.projectmanagement.exception;

public class NoSuchProjectExistException extends RuntimeException{
    private String message;
    public NoSuchProjectExistException(){
    }
    public NoSuchProjectExistException(String message){
        super(message);
        this.message=message;
    }


}
