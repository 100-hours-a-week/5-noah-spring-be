package dev.noah.word.response;

import java.time.LocalDateTime;

public record SearchAllCommentResponse(
        long id,
        String authorName,
        String authorImageUrl,
        LocalDateTime createdDate,
        String content) {
}
