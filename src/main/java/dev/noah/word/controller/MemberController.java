package dev.noah.word.controller;

import dev.noah.word.request.UpdateMemberImageAndNicknameRequest;
import dev.noah.word.request.UpdateMemberPasswordRequest;
import dev.noah.word.response.SearchMemberResponse;
import dev.noah.word.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public SearchMemberResponse getMember(@AuthenticationPrincipal long id) {
        return memberService.searchMember(id);
    }

    @PatchMapping("/update/image-and-nickname")
    public ResponseEntity<Void> updateImageAndNickname(
            @AuthenticationPrincipal long id,
            @Valid UpdateMemberImageAndNicknameRequest request) {
        memberService.updateImageAndNickname(id, request.image(), request.nickname());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/password")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal long id,
            @Valid @RequestBody UpdateMemberPasswordRequest request) {
        memberService.updatePassword(id, request.password());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal long id) {
        memberService.deleteAccount(id);

        return ResponseEntity.ok().build();
    }
}
