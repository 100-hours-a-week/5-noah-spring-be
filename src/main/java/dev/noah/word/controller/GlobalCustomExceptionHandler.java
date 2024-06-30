package dev.noah.word.controller;

import dev.noah.word.exception.*;
import dev.noah.word.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hong.kim (김지홍)
 */
@Slf4j
@RestControllerAdvice
public class GlobalCustomExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> baseExceptionHandler(BaseException exception) {
        log.warn(exception.getMessage());
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
