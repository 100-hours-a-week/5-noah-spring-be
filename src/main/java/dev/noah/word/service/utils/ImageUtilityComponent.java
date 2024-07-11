package dev.noah.word.service.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Component
public class ImageUtilityComponent {

    public String generateImageName(MultipartFile image) {
        return UUID.randomUUID() + getImageExtension(StringUtils.cleanPath(Objects.requireNonNull(image.getOriginalFilename())));
    }

    private String getImageExtension(String imageName) {
        String extension = "";

        int index = imageName.lastIndexOf('.');

        if (index > 0) {
            extension = imageName.substring(index);
        }

        return extension;
    }
}
