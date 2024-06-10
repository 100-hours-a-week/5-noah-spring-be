package dev.noah.word.service;

import dev.noah.word.domain.Member;
import dev.noah.word.dto.CheckAuthenticationDto;
import dev.noah.word.exception.AuthenticationFailedException;
import dev.noah.word.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckAuthenticationService {

    private final MemberRepository memberRepository;

    public CheckAuthenticationDto checkAuthentication(long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(AuthenticationFailedException::new);

        return new CheckAuthenticationDto(foundMember.imageUrl(), foundMember.nickname());
    }
}
