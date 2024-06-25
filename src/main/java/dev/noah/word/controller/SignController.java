package dev.noah.word.controller;

import dev.noah.word.exception.*;
import dev.noah.word.request.SignInRequest;
import dev.noah.word.request.SignUpRequest;
import dev.noah.word.response.ErrorResponse;
import dev.noah.word.service.SignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignController {

    @Value("${jwt.expiration-in-ms}")
    private int expirationInMs;

    private final SignService signService;

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@Valid @RequestBody SignInRequest request) {
        String jwt = signService.signIn(request.email(), request.password());

        ResponseCookie responseCookie = ResponseCookie
                .from("accessToken", jwt)
                .path("/")
                .httpOnly(true)
                .maxAge(expirationInMs)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> signOut() {
        // TODO 고민 후 나중에 구현
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@Valid SignUpRequest signUpRequest) {
        signService.signUp(
                signUpRequest.image(),
                signUpRequest.email(),
                signUpRequest.password(),
                signUpRequest.nickname());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailed(AuthenticationFailedException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> duplicateEmailExceptionHandler(DuplicateEmailException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> duplicateNicknameExceptionHandler(DuplicateNicknameException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
