package ru.mifi.service.risk.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mifi.authentication.model.JwtUser;
import ru.mifi.service.risk.exception.RestException;

@Controller
public class ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

    protected static String getCurrentUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principalObj = auth.getPrincipal();
        if (principalObj instanceof String) {
            return (String) principalObj;
        }
        JwtUser principal = (JwtUser) principalObj;
        return String.format("%s %s (%s)", principal.getLastname(), principal.getFirstname(), principal.getEmail());//TODO надо как-то иначе?
    }

    @ExceptionHandler(RestException.class)
    public @ResponseBody
    String handleException(RestException e) {
        LOG.error("Ошибка: " + e.getMessage(), e);
        return "Ошибка: " + e.getMessage();
    }
}