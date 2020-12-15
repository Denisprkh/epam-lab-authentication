package com.epam.esm.security.filter;

import com.epam.esm.controller.exception.ExceptionResponse;
import com.epam.esm.exception.JwtFormatException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private static final String ENCODING = "UTF-8";

    public JwtExceptionHandlerFilter(MessageSource messageSource, ObjectMapper objectMapper) {
        this.messageSource = messageSource;
        this.objectMapper = objectMapper;
    }

    private void sendJsonResponse(ExceptionResponse exceptionResponse, HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(ENCODING);
        response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtFormatException e) {
            Locale locale = request.getLocale();
            String errorMessage = messageSource.getMessage(ResourceBundleErrorMessage.JWT_FORMAT, new Object[]{}, locale);
            ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
            sendJsonResponse(exceptionResponse, response, HttpStatus.BAD_REQUEST.value());
        }

    }
}
