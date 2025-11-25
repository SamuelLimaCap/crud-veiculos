package com.support.compracarros.advices;

import com.support.compracarros.dto.handlers.Result;
import com.support.compracarros.exceptions.APIAuthException;
import com.support.compracarros.exceptions.BadCredentialsException;
import com.support.compracarros.models.FieldError;
import com.support.compracarros.models.FieldErrors;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
public class Advice {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result<Void> handleBadRequestException(BadCredentialsException ex) {
        return Result.errByFields(ex.getFieldErrors());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result<Void> handleConstraintViolationException(ConstraintViolationException ex) {
        var fieldErrors = ex.getConstraintViolations().stream().map(violation -> {
            return new FieldError(violation.getPropertyPath().toString(), violation.getMessage());
        }).collect(Collectors.toList());

        return Result.errByFields(new FieldErrors(fieldErrors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result<Void> handleValidationException(MethodArgumentNotValidException ex) {
        var fieldErrors = ex.getBindingResult().getAllErrors().stream().map(error -> {
            return new FieldError(((org.springframework.validation.FieldError) error).getField(), error.getDefaultMessage());
        }).collect(Collectors.toList());

        return Result.errByFields(new FieldErrors(fieldErrors));
    }

    @ExceptionHandler(APIAuthException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Result<Void> handleAPIAuthException(APIAuthException ex) {
        return Result.err(ex.getMessage());
    }
}
