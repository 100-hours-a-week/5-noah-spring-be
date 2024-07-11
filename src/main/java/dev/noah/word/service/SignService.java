package dev.noah.word.service;

import dev.noah.word.domain.Member;
import dev.noah.word.exception.*;
import dev.noah.word.common.JwtProvider;
import dev.noah.word.repository.MemberJdbcTemplateRepository;
import dev.noah.word.service.utils.ImageUtilityComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {

    @Value("${spring.cloud.aws.s3.bucket-url}")
    private String bucketUrl;

    private final MemberJdbcTemplateRepository memberJdbcTemplateRepository;
    private final JwtProvider jwtProvider;
    private final ImageUtilityComponent imageUtilityComponent;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    /* query: 1
     * 1. JDBC, 사용자 조회
     */
    public String signIn(String email, String password) {
        Member foundMember = memberJdbcTemplateRepository.findByEmail(email)
                .orElseThrow(AuthenticationFailedException::new);

        if (!passwordEncoder.matches(password, foundMember.password())) {
            throw new AuthenticationFailedException();
        }

        return jwtProvider.generateJwt(foundMember.id());
    }

    /* query: 3
     * 1. JDBC, 사용자 이메일 중복 확인
     * 2. JDBC, 사용자 닉네임 중복 확인
     * 3. 사용자 저장
     */
    @Transactional
    public void signUp(MultipartFile image, String email, String password, String nickname) {
        if (memberJdbcTemplateRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }

        if (memberJdbcTemplateRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }

        String imageName = imageUtilityComponent.generateImageName(image);

        memberJdbcTemplateRepository.save(bucketUrl + imageName, email, passwordEncoder.encode(password), nickname);

        s3Service.uploadFile(image, imageName);
    }
}
