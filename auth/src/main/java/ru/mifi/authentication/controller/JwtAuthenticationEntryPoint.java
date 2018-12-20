package ru.mifi.authentication.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.mifi.constructor.utils.exception.AuthException;
import ru.mifi.constructor.utils.exception.ExceptionInfoHandler;
import ru.mifi.constructor.utils.exception.LoggerWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    private static final LoggerWrapper LOG = LoggerWrapper.get(ExceptionInfoHandler.class);


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, LOG.getErrorInfo(request.getRequestURL(), new AuthException("Unauthorized")).toString());
        ;
    }
}