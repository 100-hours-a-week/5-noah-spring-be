package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends BaseException {

    public NotAuthorizedException() {
        super("NOT_AUTHORIZED", HttpStatus.FORBIDDEN);
    }
}
