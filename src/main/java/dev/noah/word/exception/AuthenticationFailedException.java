package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends BaseException {

    public AuthenticationFailedException() {
        super("AUTHENTICATION_FAILED", HttpStatus.UNAUTHORIZED);
    }
}
