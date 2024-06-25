package dev.noah.word.repository;

import dev.noah.word.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByPostEntity_Id(long postId);
}
