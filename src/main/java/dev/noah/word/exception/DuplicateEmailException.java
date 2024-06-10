package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends BaseException {

    public DuplicateEmailException() {
        super("DUPLICATE_EMAIL", HttpStatus.CONFLICT);
    }
}
