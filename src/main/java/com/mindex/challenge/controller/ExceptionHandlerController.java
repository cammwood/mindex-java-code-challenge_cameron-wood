package com.mindex.challenge.controller;

import com.mindex.challenge.exceptions.EmptyObjectException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

    /**
     * Return 500 http code with generic message body for any RunTime exception
     * @param ex
     * @return String
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody String handleGenericException(final Exception ex) {
        LOG.error(ex.getMessage(), ex);
        return "Error processing request!";
    }

    /**
     * Return 204 http code with empty body when no record found for request
     * @param ex
     */
    @ExceptionHandler(EmptyObjectException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handleObjectNotFoundException(final EmptyObjectException ex) {
        //log warning incase a record was expected to be found but wasn't
        LOG.warn(ex.getMessage(), ex);
    }

    /**
     * Return 400 http code when illegal argument was passed in request
     * @param ex
     */
    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody String handleBadRequestExceptions(final Exception ex) {
        LOG.warn(ex.getMessage(), ex);
        if (ex instanceof  MethodArgumentNotValidException) {
            return processFieldError(((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors());
        } else if (ex instanceof HttpMessageNotReadableException) {
            return ((HttpMessageNotReadableException) ex).getMostSpecificCause().getMessage();
        }
        return ex.getMessage();
    }

    /**
     * Concatenate field error default messages into a single string
     * @param fieldErrors
     * @return String
     */
    private String processFieldError(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fieldError -> sb.append(fieldError.getDefaultMessage()).append(StringUtils.LF));
        return sb.toString();
    }
}