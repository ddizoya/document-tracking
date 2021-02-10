package com.demo.documenttracking.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RestExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Error> resrouceNotFoundHandler(RuntimeException ex, HttpServletRequest request) {

        Error errorMessage = Error.builder()
                .status(HttpStatus.CONFLICT)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorMessage);
    }

    @ExceptionHandler(value = {DocumentStoringException.class})
    protected ResponseEntity<Error> storingDocumentHandler(RuntimeException ex, HttpServletRequest request) {

        Error errorMessage = Error.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorMessage);
    }

    @Getter
    static class Error {

        private String message;
        private HttpStatus status;

        @Builder
        public Error(HttpStatus status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}