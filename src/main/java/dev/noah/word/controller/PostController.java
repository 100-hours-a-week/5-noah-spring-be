package dev.noah.word.controller;

import dev.noah.word.exception.AuthorizationException;
import dev.noah.word.exception.MemberNotFoundException;
import dev.noah.word.exception.PostNotFoundException;
import dev.noah.word.response.ErrorResponse;
import dev.noah.word.response.SearchAllPostResponse;
import dev.noah.word.response.SearchPostResponse;
import dev.noah.word.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/api/posts")
    public List<SearchAllPostResponse> searchAll() {
        return postService.searchAll();
    }

    @GetMapping("/api/posts/{id}")
    public SearchPostResponse getPost(@PathVariable long id) {
        return postService.searchById(id);
    }

    @PostMapping("/api/posts")
    public ResponseEntity<Void> createPost(
            Principal principal,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("title") String title,
            @RequestPart("content") String content) {
        postService.createPost(getMemberId(principal), image, title, content);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/posts/{id}")
    public ResponseEntity<Void> editPost(
            @PathVariable long id,
            Principal principal,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart("title") String title,
            @RequestPart("content") String content) {
        postService.editPost(id, getMemberId(principal), image, title, content);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id, Principal principal) {
        postService.deletePost(id, getMemberId(principal));

        return ResponseEntity.ok().build();
    }

    private long getMemberId(Principal principal) {
        return Long.parseLong(principal.getName());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> MemberNotFoundExceptionHandler(MemberNotFoundException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    // "/api/posts/{id}" 변환할 수 없는 id가 들어온 경우
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> MethodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        return ResponseEntity.status(400).body(new ErrorResponse("BAD_REQUEST"));
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> PostNotFoundExceptionHandler(PostNotFoundException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> AuthorizationExceptionHandler(AuthorizationException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorResponse(exception.getMessage()));
    }
}
