package dev.noah.word.controller;

import dev.noah.word.request.PostRequest;
import dev.noah.word.response.SearchAllPostResponse;
import dev.noah.word.response.SearchPostResponse;
import dev.noah.word.service.PostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<SearchAllPostResponse> searchAll() {
        return postService.searchAll();
    }

    @GetMapping("/{id}")
    public SearchPostResponse getPost(@PathVariable long id) {
        return postService.searchById(id);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(
            @AuthenticationPrincipal long memberId,
            @Valid PostRequest postRequest) {
        postService.createPost(
                memberId,
                postRequest.image(),
                postRequest.title(),
                postRequest.content());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> editPost(
            @PathVariable long id,
            @AuthenticationPrincipal long memberId,
            @Valid PostRequest postRequest) {
        postService.editPost(
                id,
                memberId,
                postRequest.image(),
                postRequest.title(),
                postRequest.content());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable long id, @AuthenticationPrincipal long memberId) {
        postService.deletePost(id, memberId);

        return ResponseEntity.ok().build();
    }
}
