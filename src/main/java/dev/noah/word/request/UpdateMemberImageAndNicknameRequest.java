package dev.noah.word.request;

import dev.noah.word.config.constraint.annotation.FileExtensions;
import dev.noah.word.config.constraint.annotation.NotEmptyFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UpdateMemberImageAndNicknameRequest(
        @NotEmptyFile @FileExtensions MultipartFile image,
        @NotBlank @Size(min = 1, max = 10) String nickname) {
}
