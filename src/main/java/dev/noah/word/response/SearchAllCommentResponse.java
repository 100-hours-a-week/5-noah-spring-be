package dev.noah.word.response;

import java.time.LocalDateTime;

public record SearchAllCommentResponse(long id, long userId, LocalDateTime createdDate, String content) {

}
