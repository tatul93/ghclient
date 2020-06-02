package com.webfontaine.ghclient.controller;

import com.webfontaine.ghclient.dto.api.ErrorDto;
import com.webfontaine.ghclient.exception.DocumentAlreadyExistException;
import com.webfontaine.ghclient.exception.DocumentNotFoundException;
import com.webfontaine.ghclient.exception.WrongParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception handler class
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDto> handleAllExceptions(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto("Something went wrong");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RestClientException.class)
    public final ResponseEntity<ErrorDto> handleRestClientException(RestClientException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto("Failed to connect Github API");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public final ResponseEntity<ErrorDto> handleDocumentNotFound(DocumentNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto("Document not found");
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentAlreadyExistException.class)
    public final ResponseEntity<ErrorDto> handleDocumentAlreadyExist(DocumentAlreadyExistException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(WrongParameterException.class)
    public final ResponseEntity<ErrorDto> handleWrongParameter(WrongParameterException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ErrorDto errorDto = new ErrorDto(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
}
