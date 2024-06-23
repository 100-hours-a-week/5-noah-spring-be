package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends BaseException {

    public AuthorizationException() {
        super("NOT_AUTHORIZED", HttpStatus.FORBIDDEN);
    }
}
