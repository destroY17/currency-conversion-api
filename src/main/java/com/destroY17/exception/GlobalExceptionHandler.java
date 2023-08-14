package com.destroY17.exception;

import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExchangeException.class)
    public ResponseEntity<ErrorMessage> handleExchangeException(ExchangeException e) {
        return new ResponseEntity<>(new ErrorMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorMessage> handleDataAccessException(DataAccessException e) {
        return new ResponseEntity<>(new ErrorMessage("Database error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations()
                .stream()
                .map(violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ValidationErrorResponse(violations), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> new Violation(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ValidationErrorResponse(violations), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateKeyException(DuplicateKeyException e) {
        return new ResponseEntity<>(new ErrorMessage("These data already exists"), HttpStatus.CONFLICT);
    }
}
