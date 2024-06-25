package dev.noah.word.controller;

import dev.noah.word.request.CommentRequest;
import dev.noah.word.response.SearchAllCommentResponse;
import dev.noah.word.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public List<SearchAllCommentResponse> searchAll(@PathVariable long postId) {
        return commentService.searchAll(postId);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> create(
            @AuthenticationPrincipal long memberId,
            @PathVariable long postId,
            @RequestBody CommentRequest commentRequest) {
        commentService.create(memberId, postId, commentRequest.content());

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<Void> edit(
            @AuthenticationPrincipal long memberId,
            @PathVariable long id,
            @RequestBody CommentRequest commentRequest) {
        commentService.edit(memberId, id, commentRequest.content());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal long memberId, @PathVariable long id) {
        commentService.delete(memberId, id);

        return ResponseEntity.ok().build();
    }
}
