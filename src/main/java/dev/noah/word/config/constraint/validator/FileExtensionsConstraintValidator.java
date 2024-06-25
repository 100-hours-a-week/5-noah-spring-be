package dev.noah.word.config.constraint.validator;

import dev.noah.word.config.constraint.annotation.FileExtensions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileExtensionsConstraintValidator implements ConstraintValidator<FileExtensions, MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(FileExtensions constraintAnnotation) {
        allowedExtensions = Arrays.stream(constraintAnnotation.value())
                .map(String::toLowerCase)
                .toArray(String[]::new);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return true;
        }

        String fileName = multipartFile.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            return false;
        }

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        for (String allowedExtension : allowedExtensions) {
            if (extension.equals(allowedExtension)) {
                return true;
            }
        }

        return false;
    }
}
