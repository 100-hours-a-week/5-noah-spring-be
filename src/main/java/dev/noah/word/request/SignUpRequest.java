package dev.noah.word.request;

import dev.noah.word.config.constraint.annotation.FileExtensions;
import dev.noah.word.config.constraint.annotation.NotEmptyFile;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

public record SignUpRequest(
        @NotEmptyFile @FileExtensions MultipartFile image,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,20}$") String password,
        @NotBlank @Size(min = 1, max = 10) String nickname) {
}
