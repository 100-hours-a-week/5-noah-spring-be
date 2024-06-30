package dev.noah.word.request;

import dev.noah.word.config.constraint.annotation.FileExtensions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostRequest(
        @FileExtensions MultipartFile image,
        @NotBlank @Size(min = 1, max = 32) String title,
        // Q. content, Integer.MAX_VALUE 를 넘어가는경우에는?
        @NotBlank @Size(min = 1) String content) {
}
