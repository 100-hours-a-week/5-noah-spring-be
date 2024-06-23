package dev.noah.word.response;

import java.time.LocalDateTime;

public record SearchAllPostResponse(long id, String authorName, String authorImageUrl, LocalDateTime localDateTime,
                                    String title, int views, int likes, int comments) {

}
