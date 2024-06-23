package dev.noah.word.controller;

import dev.noah.word.exception.ImageDeleteFailedException;
import dev.noah.word.exception.ImageSaveFailedException;
import dev.noah.word.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageSaveFailedException.class)
    public ResponseEntity<ErrorResponse> imageSaveFailedExceptionHandler(ImageSaveFailedException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(ImageDeleteFailedException.class)
    public ResponseEntity<ErrorResponse> imageDeleteFailedExceptionHandler(ImageDeleteFailedException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
