package dev.noah.word.response;

import java.time.LocalDateTime;

public record SearchAllPostResponse(
        long id,
        String authorName,
        String authorImageUrl,
        LocalDateTime createdDate,
        String title,
        int views,
        int likes,
        long comments) {
}
