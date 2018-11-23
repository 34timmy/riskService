package ru.mifi.authentication.utils;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}
