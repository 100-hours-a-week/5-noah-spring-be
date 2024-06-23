package dev.noah.word.service.utils;

import dev.noah.word.exception.ImageDeleteFailedException;
import dev.noah.word.exception.ImageSaveFailedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Component
public class ImageUtilityComponent {

    // 이렇게 사용해도 될까? 고민 중
    private static final String SERVER_ADDRESS = "http://localhost:8080";

    public String saveImageAndReturnImageUrl(MultipartFile image, String directoryPath, String relativePath) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String imageName = generateImageName(image);

        Path path = Paths.get(relativePath + imageName);

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            throw new ImageSaveFailedException();
        }

        return SERVER_ADDRESS + directoryPath + imageName;
    }

    private String generateImageName(MultipartFile image) {
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

    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return;
        }

        String relativePath = imageUrl.replace(SERVER_ADDRESS, "");

        Path path = Paths.get(relativePath);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new ImageDeleteFailedException();
        }
    }
}
