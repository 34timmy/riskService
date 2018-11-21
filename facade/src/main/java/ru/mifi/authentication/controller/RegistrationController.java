package ru.mifi.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.authentication.model.User;
import ru.mifi.authentication.service.UserServiceImpl;
import ru.mifi.authentication.utils.EmailExistException;
import ru.mifi.service.risk.controllers.ResponseHelper;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping()
public class RegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user) throws SQLException, EmailExistException {
        userService.create(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/login")
    public Principal loginUser(
                               Principal user) throws SQLException, EmailExistException {
//        User user = userService.getByEmail(username);
        return  user;
    }
}
