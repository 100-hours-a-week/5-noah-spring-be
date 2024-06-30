package dev.noah.word.repository;

import dev.noah.word.entity.PostEntity;
import dev.noah.word.response.SearchAllPostResponse;
import dev.noah.word.response.SearchPostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    @Query("SELECT new dev.noah.word.response.SearchAllPostResponse(p.id, m.nickname, m.imageUrl, p.createdDate, p.title, p.views, p.likes, COUNT(c.id)) " +
            "FROM PostEntity p " +
            "JOIN p.memberEntity m " +
            "LEFT JOIN p.commentEntities c " +
            "GROUP BY p.id, m.nickname, m.imageUrl, p.createdDate, p.title, p.views, p.likes")
    List<SearchAllPostResponse> findAllWithAuthorAndComments();

    @Query("SELECT NEW dev.noah.word.response.SearchPostResponse(p.id, m.nickname, m.imageUrl, p.createdDate, p.imageUrl, p.title, p.content, p.views, COUNT(c.id)) " +
            "FROM PostEntity p " +
            "JOIN p.memberEntity m " +
            "LEFT JOIN p.commentEntities c " +
            "WHERE p.id = :id " +
            "GROUP BY p.id, m.nickname, m.imageUrl, p.createdDate, p.title, p.content, p.views")
    Optional<SearchPostResponse> findWithAuthorAndCommentsById(@Param("id") long id);

    @Modifying
    @Query("DELETE FROM PostEntity p WHERE p.memberEntity.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") long memberId);
}
