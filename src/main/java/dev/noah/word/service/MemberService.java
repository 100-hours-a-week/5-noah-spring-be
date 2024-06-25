package dev.noah.word.service;

import dev.noah.word.domain.Member;
import dev.noah.word.exception.DuplicateNicknameException;
import dev.noah.word.exception.MemberNotFoundException;
import dev.noah.word.repository.MemberJdbcTemplateRepository;
import dev.noah.word.service.utils.ImageUtilityComponent;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@AllArgsConstructor
public class MemberService {

    private static final String MEMBER_IMAGE_SAVE_DIRECTORY_PATH = "/member-images/";
    private static final String MEMBER_IMAGE_SAVE_RELATIVE_PATH = "src/main/resources/public" + MEMBER_IMAGE_SAVE_DIRECTORY_PATH;

    private final MemberJdbcTemplateRepository memberJdbcTemplateRepository;
    private final ImageUtilityComponent imageUtilityComponent;
    private final PasswordEncoder passwordEncoder;

    public void updateImageAndNickname(long id, MultipartFile image, String nickname) {
        Member foundMember = memberJdbcTemplateRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        if (memberJdbcTemplateRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }

        String imageUrl = imageUtilityComponent
                .saveImageAndReturnImageUrl(image, MEMBER_IMAGE_SAVE_DIRECTORY_PATH, MEMBER_IMAGE_SAVE_RELATIVE_PATH);

        memberJdbcTemplateRepository.updateImageUrlAndNicknameById(id, imageUrl, nickname);

        // INFO: 사실 이미지를 삭제하면 복구할 방법이 없다. 소프르 삭제를 수행해야 한다.
        imageUtilityComponent.deleteImage(foundMember.imageUrl());
    }

    public void updatePassword(long id, String password) {
        if (memberJdbcTemplateRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }

        memberJdbcTemplateRepository.updatePasswordById(id, passwordEncoder.encode(password));
    }

    public void deleteAccount(long id) {
        Member foundMember = memberJdbcTemplateRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        memberJdbcTemplateRepository.deleteById(id);

        // INFO: 사실 이미지를 삭제하면 복구할 방법이 없다. 소프르 삭제를 수행해야 한다.
        imageUtilityComponent.deleteImage(foundMember.imageUrl());
    }
}
