package dev.noah.word.service;

import dev.noah.word.entity.CommentEntity;
import dev.noah.word.entity.MemberEntity;
import dev.noah.word.entity.PostEntity;
import dev.noah.word.exception.NotAuthorizedException;
import dev.noah.word.exception.CommentNotFoundException;
import dev.noah.word.exception.MemberNotFoundException;
import dev.noah.word.exception.PostNotFoundException;
import dev.noah.word.repository.CommentJpaRepository;
import dev.noah.word.repository.MemberJpaRepository;
import dev.noah.word.repository.PostJpaRepository;
import dev.noah.word.response.SearchAllCommentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final PostJpaRepository postJpaRepository;

    public List<SearchAllCommentResponse> searchAll(long postId) {
        if (!postJpaRepository.existsById(postId)) {
            throw new PostNotFoundException();
        }

        return commentJpaRepository.findAllByPostEntity_Id(postId)
                .stream()
                .map(commentEntity -> new SearchAllCommentResponse(
                        commentEntity.getId(),
                        commentEntity.getMemberEntity().getId(),
                        commentEntity.getCreatedDate(),
                        commentEntity.getContent()
                ))
                .toList();
    }

    @Transactional
    public void create(long memberId, long postId, String content) {
        MemberEntity foundMemberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        PostEntity foundPostEntity = postJpaRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        commentJpaRepository.save(new CommentEntity(foundMemberEntity, foundPostEntity, content));
    }

    @Transactional
    public void edit(long memberId, long id, String content) {
        if (!memberJpaRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        CommentEntity foundCommentEntity = commentJpaRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);

        if (foundCommentEntity.getMemberEntity().getId() != memberId) {
            throw new NotAuthorizedException();
        }

        foundCommentEntity.edit(content);
    }

    @Transactional
    public void delete(long memberId, long id) {
        if (!memberJpaRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        CommentEntity foundCommentEntity = commentJpaRepository.findById(id)
                .orElseThrow(CommentNotFoundException::new);

        if (foundCommentEntity.getMemberEntity().getId() != memberId) {
            throw new NotAuthorizedException();
        }

        commentJpaRepository.deleteById(id);
    }
}
