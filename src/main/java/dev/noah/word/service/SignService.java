package dev.noah.word.service;

import dev.noah.word.exception.*;
import dev.noah.word.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SignService {

    private static final String MEMBER_IMAGE_SAVE_DIRECTORY = "member-images/";
    private static final String MEMBER_IMAGE_SAVE_PATH = "src/main/resources/public/" + MEMBER_IMAGE_SAVE_DIRECTORY;

    private final MemberRepository memberRepository;

    public long signIn(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password).orElseThrow(AuthenticationFailedException::new).id();
    }

    public void signUp(String serverUrl, MultipartFile image, String email, String password, String nickname) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }

        String imageName = generateImageName(image);

        Path path = Paths.get(MEMBER_IMAGE_SAVE_PATH + imageName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, image.getBytes());
        } catch (IOException e) {
            throw new ImageSaveFailedException();
        }

        memberRepository.save(serverUrl + "/" + MEMBER_IMAGE_SAVE_DIRECTORY + imageName, email, password, nickname);
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
}
