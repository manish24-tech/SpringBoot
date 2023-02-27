package com.org.epaperintegration.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Collection<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();

        ErrorResponse invalidMethodArgument = ErrorResponse.builder()
                .reason("Invalid method argument")
                .errors(errors)
                .code(status.toString())
                .reason(ex.getLocalizedMessage())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(invalidMethodArgument, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getPropertyName().concat(" should be of type ").concat(ex.getRequiredType().getName());

        ErrorResponse invalidTypeMismatch = new ErrorResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), System.currentTimeMillis(), error);

        return new ResponseEntity<>(invalidTypeMismatch, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String error = ex.getRequestPartName().concat(" parameter is missing");

        ErrorResponse invalidParameter = new ErrorResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), System.currentTimeMillis(), error);

        return new ResponseEntity<>(invalidParameter, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringJoiner error = new StringJoiner(" ");
        error.add(ex.getMethod());
        error.add("method is not supported for this request. supported method are");

        for (String method : Objects.requireNonNull(ex.getSupportedMethods())) {
            error.add(method);
        }

        ErrorResponse invalidParameter = new ErrorResponse(ex.getLocalizedMessage(), HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), System.currentTimeMillis(), error.toString());

        return new ResponseEntity<>(invalidParameter, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        Collection<String> errors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName()
                    .concat(String.valueOf(violation.getPropertyPath()))
                    .concat(": ").concat(violation.getMessage()));
        }

        ErrorResponse invalidConstraintOnMethodArgument = ErrorResponse.builder()
                .reason("Invalid constraint on method argument")
                .errors(errors)
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .reason(ex.getLocalizedMessage())
                .timestamp(System.currentTimeMillis())
                .build();

        return new ResponseEntity<>(invalidConstraintOnMethodArgument, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(final ResourceNotFoundException ex) {
        ErrorResponse resourceNotFound =
                new ErrorResponse(ex.getErrorDescription(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        System.currentTimeMillis(),
                        ex.getError());

        return new ResponseEntity<>(resourceNotFound, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Object> handleResourceExistsException(final ResourceExistsException ex) {
        ErrorResponse resourceAlreadyPresent =
                new ErrorResponse(ex.getErrorDescription(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        System.currentTimeMillis(),
                        ex.getError());

        return new ResponseEntity<>(resourceAlreadyPresent, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceInvalidException.class)
    public ResponseEntity<Object> handleResourceInvalidException(final ResourceInvalidException ex) {
        ErrorResponse resourceAlreadyPresent =
                new ErrorResponse(ex.getErrorDescription(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        System.currentTimeMillis(),
                        ex.getError());

        return new ResponseEntity<>(resourceAlreadyPresent, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
