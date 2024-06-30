package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class DuplicateNicknameException extends BaseException {

    public DuplicateNicknameException() {
        super("DUPLICATE_NICKNAME", HttpStatus.CONFLICT);
    }
}
