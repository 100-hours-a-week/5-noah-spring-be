package dev.noah.word.controller;

import dev.noah.word.response.SearchAllPostResponse;
import dev.noah.word.response.SearchPostResponse;
import dev.noah.word.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
}
