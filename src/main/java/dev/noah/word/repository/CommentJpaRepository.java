package dev.noah.word.repository;

import dev.noah.word.entity.CommentEntity;
import dev.noah.word.response.SearchAllCommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT new dev.noah.word.response.SearchAllCommentResponse(c.id, m.nickname, m.imageUrl, c.createdDate, c.content) " +
            "FROM CommentEntity c " +
            "JOIN c.memberEntity m " +
            "WHERE c.postEntity.id = :postId")
    List<SearchAllCommentResponse> findAllByPostIdWithAuthor(@Param("postId") long postId);

    @Modifying
    @Query("DELETE FROM CommentEntity c WHERE c.memberEntity.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") long memberId);

    @Modifying
    @Query("DELETE FROM CommentEntity c WHERE c.postEntity.id = :postId")
    void deleteAllByPostId(@Param("postId") long postId);
}
