package dev.noah.word.response;

import java.time.LocalDateTime;

public record SearchPostResponse(
        long id,
        String authorName,
        String authorImageUrl,
        LocalDateTime createdDate,
        String imageUrl,
        String title,
        String content,
        int views,
        long comments) {
}