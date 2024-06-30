package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends BaseException {

    public MemberNotFoundException() {
        super("MEMBER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
