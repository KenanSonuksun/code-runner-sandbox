package com.project.onlinecoderunner.controller;

import com.project.onlinecoderunner.model.CodeRunResponse;
import com.project.onlinecoderunner.model.ExecutionStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExecptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CodeRunResponse handleIllegalArgument(IllegalArgumentException ex) {
        CodeRunResponse response = new CodeRunResponse();
        response.setStatus(ExecutionStatus.INVALID_REQUEST);
        response.setStdout("");
        response.setStderr(ex.getMessage());
        response.setExitCode(-1);
        return response;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CodeRunResponse handleGeneric(Exception ex) {
        CodeRunResponse response = new CodeRunResponse();
        response.setStatus(ExecutionStatus.SYSTEM_ERROR);
        response.setStdout("");
        response.setStderr("Unexpected server error.");
        response.setExitCode(-1);
        return response;
    }
}
