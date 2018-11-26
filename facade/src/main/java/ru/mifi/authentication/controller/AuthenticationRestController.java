package ru.mifi.authentication.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.mifi.authentication.model.JwtAuthenticationRequest;
import ru.mifi.authentication.utils.JwtTokenUtil;
import ru.mifi.authentication.model.JwtUser;
import ru.mifi.authentication.model.User;
import ru.mifi.authentication.model.JwtAuthenticationResponse;
import ru.mifi.authentication.service.UserServiceImpl;
import ru.mifi.authentication.utils.EmailExistException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Collections;

@RestController
@Api(value = "Контроллер для аутентификации")
public class AuthenticationRestController {

    @Value("Authorization")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/auth")
    @ApiOperation(value = "Аутентификация")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword(),
                        Collections.emptyList()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping(value = "/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping(value = "/register")
    public ResponseEntity registerUser(@Valid @RequestBody User user) throws SQLException, EmailExistException {
        userService.create(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/profile")
    public ResponseEntity getUserProfile(@RequestParam("id") String id) throws SQLException, EmailExistException {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        User user = userService.get(id);
        if (user != null && user.getEmail().equals(email)) {
            user.setPassword("");
            user.setRoles(Collections.emptySet());
            user.setRegistered(null);
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/profile")
    public ResponseEntity updateUserProfile(@Valid @RequestBody User user) throws SQLException, EmailExistException {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
        if (user.getEmail().equals(email)) {
            userService.save(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
