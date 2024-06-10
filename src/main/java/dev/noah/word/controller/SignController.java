package dev.noah.word.controller;

import dev.noah.word.exception.*;
import dev.noah.word.request.SignInRequest;
import dev.noah.word.response.ErrorResponse;
import dev.noah.word.service.SignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class SignController {

    private final SignService signService;

    @PostMapping("/api/sign-in")
    public ResponseEntity<Void> signIn(HttpSession httpSession, @RequestBody SignInRequest request) {
        if (httpSession.getAttribute("id") != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        httpSession.setAttribute("id", signService.signIn(request.getEmail(), request.getPassword()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/api/sign-out")
    public ResponseEntity<Void> signOut(HttpSession httpSession) {
        httpSession.invalidate();

        return ResponseEntity.noContent().build();
    }

    // serverUrl을 받기 위해 HttpServletRequest 사용
    @PostMapping("/api/sign-up")
    public ResponseEntity<Void> signUp(HttpServletRequest httpServletRequestRequest, @RequestPart("image") MultipartFile image, @RequestPart("email") String email, @RequestPart("password") String password, @RequestPart("nickname") String nickname) {
        HttpSession httpSession = httpServletRequestRequest.getSession();

        if (httpSession.getAttribute("id") != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        signService.signUp(getServerUrl(httpServletRequestRequest), image, email, password, nickname);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private String getServerUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
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

    @ExceptionHandler(ImageSaveFailedException.class)
    public ResponseEntity<ErrorResponse> imageSaveFailedExceptionHandler(ImageSaveFailedException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
