package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class ImageDeleteFailedException extends BaseException {

    public ImageDeleteFailedException() {
        super("IMAGE_DELETE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
