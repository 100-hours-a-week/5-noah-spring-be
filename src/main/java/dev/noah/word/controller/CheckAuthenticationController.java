package dev.noah.word.controller;

import dev.noah.word.dto.CheckAuthenticationDto;
import dev.noah.word.exception.AuthenticationFailedException;
import dev.noah.word.response.CheckAuthenticationResponse;
import dev.noah.word.response.ErrorResponse;
import dev.noah.word.service.CheckAuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CheckAuthenticationController {

    private final CheckAuthenticationService checkAuthenticationService;

    @GetMapping("/api/check-auth")
    public ResponseEntity<CheckAuthenticationResponse> checkAuthentication(HttpSession httpSession) {
        Object memberIdObject = httpSession.getAttribute("id");

        if (memberIdObject == null) {
            throw new AuthenticationFailedException();
        }

        CheckAuthenticationDto checkAuthenticationDto = checkAuthenticationService.checkAuthentication(getMemberId(memberIdObject));

        return ResponseEntity.status(HttpStatus.OK).body(new CheckAuthenticationResponse(checkAuthenticationDto.imageUrl(), checkAuthenticationDto.nickname()));
    }

    private long getMemberId(Object object) {
        try {
            return Long.parseLong(object.toString());
        } catch (NumberFormatException e) {
            throw new AuthenticationFailedException();
        }
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailed(AuthenticationFailedException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
