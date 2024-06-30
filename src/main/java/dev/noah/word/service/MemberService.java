package dev.noah.word.service;

import dev.noah.word.domain.Member;
import dev.noah.word.exception.DuplicateNicknameException;
import dev.noah.word.exception.MemberNotFoundException;
import dev.noah.word.repository.CommentJpaRepository;
import dev.noah.word.repository.MemberJdbcTemplateRepository;
import dev.noah.word.repository.PostJpaRepository;
import dev.noah.word.response.SearchMemberResponse;
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
    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;
    private final ImageUtilityComponent imageUtilityComponent;
    private final PasswordEncoder passwordEncoder;

    /* query: 1
     * 1. JDBC, 사용자 조회
     */
    @Transactional(readOnly = true)
    public SearchMemberResponse searchMember(long id) {
        Member foundMember = memberJdbcTemplateRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        return new SearchMemberResponse(
                foundMember.id(),
                foundMember.imageUrl(),
                foundMember.email(),
                foundMember.nickname()
        );
    }

    /* query: 3
     * 1. JDBC, 사용자 조회
     * 2. JDBC, 사용자 닉네임 중복 확인
     * 3. JDBC, 사용자 이미지, 닉네임 갱신
     */
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

    /* query: 2
     * 1. JDBC, 사용자 존재 유무 확인
     * 2. JDBC, 사용자 비밀번호 갱신
     */
    public void updatePassword(long id, String password) {
        if (!memberJdbcTemplateRepository.existsById(id)) {
            throw new MemberNotFoundException();
        }

        memberJdbcTemplateRepository.updatePasswordById(id, passwordEncoder.encode(password));
    }

    /* query: 4 (영속성 전이 삭제 방식에서 JPQL로 최적화)
     * 1. JDBC, 사용자 조회
     * 2. JPA, 댓글 삭제
     * 3. JPA, 게시글 삭제
     * 4. JDBC, 회원 삭제
     */
    public void deleteAccount(long id) {
        Member foundMember = memberJdbcTemplateRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);

        commentJpaRepository.deleteAllByMemberId(id);
        postJpaRepository.deleteAllByMemberId(id);
        memberJdbcTemplateRepository.deleteById(id);

        // INFO: 사실 이미지를 삭제하면 복구할 방법이 없다. 소프르 삭제를 수행해야 한다.
        imageUtilityComponent.deleteImage(foundMember.imageUrl());
    }
}
