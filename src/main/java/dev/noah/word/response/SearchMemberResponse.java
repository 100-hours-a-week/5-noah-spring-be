package dev.noah.word.response;

public record SearchMemberResponse(
        long id,
        String imageUrl,
        String email,
        String nickname) {
}
