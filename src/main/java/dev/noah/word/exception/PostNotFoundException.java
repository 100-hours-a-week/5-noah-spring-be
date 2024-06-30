package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException() {
        super("POST_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
