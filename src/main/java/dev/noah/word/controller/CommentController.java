package dev.noah.word.controller;

import dev.noah.word.request.CommentRequest;
import dev.noah.word.response.SearchAllCommentResponse;
import dev.noah.word.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/posts/{postId}/comments")
    public List<SearchAllCommentResponse> searchAll(@PathVariable long postId) {
        return commentService.searchAll(postId);
    }

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<Void> create(
            Principal principal,
            @PathVariable long postId,
            @RequestBody CommentRequest commentRequest) {
        commentService.create(getMemberId(principal), postId, commentRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/posts/{postId}/comments/{id}")
    public ResponseEntity<Void> edit(
            Principal principal,
            @PathVariable long id,
            @RequestBody CommentRequest commentRequest) {
        commentService.edit(getMemberId(principal), id, commentRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/posts/{postId}/comments/{id}")
    public ResponseEntity<Void> delete(Principal principal, @PathVariable long id) {
        commentService.delete(getMemberId(principal), id);

        return ResponseEntity.ok().build();
    }

    private long getMemberId(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
