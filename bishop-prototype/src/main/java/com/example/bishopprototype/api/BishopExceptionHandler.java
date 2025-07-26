package com.example.bishopprototype.api;

import com.example.bishopprototype.command.exception.UnavailableCommandException;
import com.example.synthetichumancorestarter.errorhandling.SyntheticHumanGlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BishopExceptionHandler extends SyntheticHumanGlobalExceptionHandler {

    @ExceptionHandler(UnavailableCommandException.class)
    public ErrorResponse handleExecutionQueueIsFullException(UnavailableCommandException e) {
        return super.buildErrorResponse(e, HttpStatus.BAD_REQUEST, "COMMAND IS UNAVAILABLE");
    }
}