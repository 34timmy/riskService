package ru.mifi.authentication.utils;

import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailExistException extends Exception {
    public EmailExistException(String message) {
        super(message);
    }
}
