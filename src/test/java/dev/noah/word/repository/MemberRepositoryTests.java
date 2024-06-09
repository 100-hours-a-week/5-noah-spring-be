package dev.noah.word.repository;

import dev.noah.word.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@JdbcTest
@Transactional
@Import(MemberRepository.class)
public class MemberRepositoryTests {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void should_pass_save() {
        // given
        String imageUrl = "http://localhost:8080/member-iamges/testImageUrl.png";
        String email = "test@test.com";
        String password = "password";
        String nickname = "testUser";

        // when
        memberRepository.save(imageUrl, email, password, nickname);

        // then
        Optional<Member> foundMemberOptional = memberRepository.findByEmailAndPassword(email, password);

        if (foundMemberOptional.isEmpty()) {
            Assertions.fail();
        }
    }

    @Test
    void should_pass_updateImageUrlAndNickname() {
        // given
        String email = "email1";
        String password = "password1";

        String newImageUrl = "imageUrl10";
        String newNickname = "nickname10";

        // when
        // INFO: id 주의
        memberRepository.updateImageUrlAndNicknameById(1, newImageUrl, newNickname);

        // then
        Optional<Member> foundMemberOptional = memberRepository.findByEmailAndPassword(email, password);

        if (foundMemberOptional.isEmpty()) {
            Assertions.fail();
            return;
        }

        Member foundMember = foundMemberOptional.get();

        Assertions.assertEquals(newImageUrl, foundMember.imageUrl());
        Assertions.assertEquals(newNickname, foundMember.nickname());
    }

    @Test
    void should_pass_updatePassword() {
        // given
        String email = "email2";

        String newPassword = "password20";

        // when
        // INFO: id 주의
        memberRepository.updatePasswordById(2, newPassword);

        // then
        Optional<Member> foundMemberOptional = memberRepository.findByEmailAndPassword(email, newPassword);

        if (foundMemberOptional.isEmpty()) {
            Assertions.fail();
            return;
        }

        Member foundMember = foundMemberOptional.get();

        Assertions.assertEquals(newPassword, foundMember.password());
    }

    @Test
    void should_pass_deleteById() {
        // when, then
        Assertions.assertEquals(5, memberRepository.findAll().size());

        // INFO: id 주의
        memberRepository.deleteById(3);

        Assertions.assertEquals(4, memberRepository.findAll().size());
    }

    @Test
    void should_throwDuplicateKeyException_checkDuplicateEmail() {
        // given
        String imageUrl = "http://localhost:8080/member-iamges/testImageUrl.png";
        String email = "email1";
        String password = "password";
        String nickname = "testUser";

        // when, then
        Assertions.assertThrows(DuplicateKeyException.class, () -> memberRepository.save(imageUrl, email, password, nickname));
    }

    @Test
    void should_pass_checkDuplicateEmail() {
        // given
        String email = "email1";

        // when, then
        Assertions.assertTrue(memberRepository.existsByEmail(email));
    }

    @Test
    void should_throwDuplicateKeyException_checkDuplicateNickname() {
        // given
        String imageUrl = "http://localhost:8080/member-iamges/testImageUrl.png";
        String email = "test@test.com";
        String password = "password";
        String nickname = "nickname1";

        // when, then
        Assertions.assertThrows(DuplicateKeyException.class, () -> memberRepository.save(imageUrl, email, password, nickname));
    }

    @Test
    void should_pass_checkDuplicateNickname() {
        // given
        String nickname = "nickname1";

        // when, then
        Assertions.assertTrue(memberRepository.existsByNickname(nickname));
    }
}
