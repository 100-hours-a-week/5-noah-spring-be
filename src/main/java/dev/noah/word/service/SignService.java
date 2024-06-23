package dev.noah.word.service;

import dev.noah.word.exception.*;
import dev.noah.word.common.JwtProvider;
import dev.noah.word.repository.MemberJdbcTemplateRepository;
import dev.noah.word.service.utils.ImageUtilityComponent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class SignService {

    private static final String MEMBER_IMAGE_SAVE_DIRECTORY_PATH = "/member-images/";
    private static final String MEMBER_IMAGE_SAVE_RELATIVE_PATH = "src/main/resources/public" + MEMBER_IMAGE_SAVE_DIRECTORY_PATH;

    private final MemberJdbcTemplateRepository memberJdbcTemplateRepository;
    private final JwtProvider jwtProvider;
    private final ImageUtilityComponent imageUtilityComponent;

    public String signIn(String email, String password) {
        long foundMemberId = memberJdbcTemplateRepository.findByEmailAndPassword(email, password)
                .orElseThrow(AuthenticationFailedException::new)
                .id();

        return jwtProvider.generateJwt(foundMemberId);
    }

    @Transactional
    public void signUp(MultipartFile image, String email, String password, String nickname) {
        if (memberJdbcTemplateRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        if (memberJdbcTemplateRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }

        String imageUrl = imageUtilityComponent
                .saveImageAndReturnImageUrl(image, MEMBER_IMAGE_SAVE_DIRECTORY_PATH, MEMBER_IMAGE_SAVE_RELATIVE_PATH);

        memberJdbcTemplateRepository.save(imageUrl, email, password, nickname);
    }
}
