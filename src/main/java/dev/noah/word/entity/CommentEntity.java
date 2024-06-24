package dev.noah.word.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false, length = 256)
    private String content;

    public CommentEntity(MemberEntity memberEntity, PostEntity postEntity, String content) {
        this.memberEntity = memberEntity;
        this.postEntity = postEntity;
        this.content = content;
    }

    public void edit(String content) {
        this.content = content;
    }
}
