package com.epam.esm.controller.exception;

import com.epam.esm.exception.JwtFormatException;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class CustomExceptionHandler {

    private final MessageSource messageSource;

    public CustomExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(MethodArgumentNotValidException e, Locale locale) {
        String errorMessage = messageSource.getMessage(e.getBindingResult().getFieldError().getDefaultMessage(),
                new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> handleBindingException(BindException e, Locale locale) {
        String errorMessage = messageSource.getMessage(e.getBindingResult().getFieldError().getDefaultMessage(),
                new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e,
                                                                                  Locale locale) {
        String errorMessage = String.format(messageSource.getMessage(e.getMessage(), new Object[]{},
                locale), e.getResourceId());
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Locale locale) {
        String errorMessage = messageSource.getMessage(
                ResourceBundleErrorMessage.NOT_READABLE_EXCEPTION, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException e, Locale locale) {
        String errorMessage = String.format(messageSource.getMessage(e.getMessage(), new Object[]{}, locale), e.getResourceId());
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(AccessDeniedException e, Locale locale) {
        String errorMessage = messageSource.getMessage(ResourceBundleErrorMessage.ACCESS_FORBIDDEN, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.FORBIDDEN.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(BadCredentialsException e, Locale locale) {
        String errorMessage = messageSource.getMessage(ResourceBundleErrorMessage.BAD_CREDENTIALS, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtFormatException.class)
    public ResponseEntity<ExceptionResponse> handleJwtFormatException(JwtFormatException e, Locale locale) {
        String errorMessage = messageSource.getMessage(ResourceBundleErrorMessage.JWT_FORMAT, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e, Locale locale) {
        String errorMessage = messageSource.getMessage(ResourceBundleErrorMessage.INTERNAL_SERVER_ERROR, new Object[]{},
                locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}