package com.example.UserManagementService.exception;

public class EmailExistsException extends  Exception{
    public EmailExistsException(String message) {
        super(message);
    }
}
