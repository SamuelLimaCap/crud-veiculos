package com.support.compracarros.dto.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.support.compracarros.models.FieldErrors;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Result<T> {

    final String errorStatus = "error";
    final String successStatus = "success";
    final String defaultError = "default";
    final String fieldError = "field";

    static <T> SuccessResponse<T> with(String message) {
        return new SuccessResponse<>(successStatus, message, null);
    }

    static <T> SuccessResponse<T> with(String message, T data) {
        return new SuccessResponse<>(successStatus, message, data);
    }

    static <T> PageSuccessResponse<List<T>> withPage(String message, List<T> data, PageSuccessResponse.PageDetails page) {
        return new PageSuccessResponse<List<T>>(successStatus, message, Collections.singletonList(data), page);
    }

    static <T> ErrorResponse err(String message) {
        return new ErrorResponse(errorStatus, Result.defaultError, message);
    }

    static <T> ValidationErrorResponse errByFields(FieldErrors fieldErrors) {
        return new ValidationErrorResponse(Result.errorStatus, Result.fieldError, fieldErrors.toMap());
    }
}
