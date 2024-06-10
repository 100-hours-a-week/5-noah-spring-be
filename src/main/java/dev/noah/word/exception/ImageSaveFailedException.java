package dev.noah.word.exception;

import org.springframework.http.HttpStatus;

public class ImageSaveFailedException extends BaseException {

    public ImageSaveFailedException() {
        super("IMAGE_SAVE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
