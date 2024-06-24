package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends BaseException {

    public CommentNotFoundException() {
        super("COMMENT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
