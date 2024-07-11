package dev.noah.word.service;

import dev.noah.word.entity.MemberEntity;
import dev.noah.word.entity.PostEntity;
import dev.noah.word.exception.NotAuthorizedException;
import dev.noah.word.exception.MemberNotFoundException;
import dev.noah.word.exception.PostNotFoundException;
import dev.noah.word.repository.CommentJpaRepository;
import dev.noah.word.repository.MemberJpaRepository;
import dev.noah.word.repository.PostJpaRepository;
import dev.noah.word.response.SearchAllPostResponse;
import dev.noah.word.response.SearchPostResponse;
import dev.noah.word.service.utils.ImageUtilityComponent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ServerErrorException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    @Value("${spring.cloud.aws.s3.bucket-url}")
    private String bucketUrl;

    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final CommentJpaRepository commentJpaRepository;
    private final ImageUtilityComponent imageUtilityComponent;
    private final S3Service s3Service;

    /* query: 1
     * 1. JPA, 게시글 전체 조회 (최적화)
     */
    public List<SearchAllPostResponse> searchAll() {
        return postJpaRepository.findAllWithAuthorAndComments().stream()
                .map(simplePostDto -> new SearchAllPostResponse(
                        simplePostDto.id(),
                        simplePostDto.authorName(),
                        simplePostDto.authorImageUrl(),
                        simplePostDto.createdDate(),
                        simplePostDto.title(),
                        simplePostDto.views(),
                        simplePostDto.likes(),
                        simplePostDto.comments()
                ))
                .toList();
    }

    /* query: 3
     * 1. JPA, 게시글 조회
     * 2. JPA, 게시글 views 1 증가 (Dirty Checking)
     * 3. JPA, 게시글 조회 (최적화)
     *
     * - views 로직 개선 필요
     * - views 로직 없다면 1개의 쿼리로 조회 가능
     */
    @Transactional
    public SearchPostResponse searchById(long id) {
        // FIXME
        postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new).increaseViews();

        return postJpaRepository.findWithAuthorAndCommentsById(id)
                .orElseThrow(PostNotFoundException::new);
    }

    /* query: 2
     * 1. JPA, 사용자 조회
     * 2. JPA, 게시글 게시글 저장
     */
    @Transactional
    public void createPost(long memberId, MultipartFile image, String title, String content) {
        MemberEntity foundMemberEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        if (image == null) {
            postJpaRepository.save(new PostEntity(foundMemberEntity, null, title, content));

            return;
        }

        String imageName = imageUtilityComponent.generateImageName(image);

        postJpaRepository.save(new PostEntity(foundMemberEntity, bucketUrl + imageName, title, content));

        s3Service.uploadFile(image, imageName);
    }

    /* query: 3
     * 1. JPA, 사용자 존재 유무 확인
     * 2. JPA, 게시글 조회
     * 3. JPA. 게시글 갱신
     */
    @Transactional
    public void editPost(long id, long memberId, MultipartFile image, String title, String content) {
        if (!memberJpaRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        PostEntity foundPostEntity = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        if (foundPostEntity.getMemberEntity().getId() != memberId) {
            throw new NotAuthorizedException();
        }

        String currentImageUrl = foundPostEntity.getImageUrl();

        if (image == null || image.isEmpty()) {
            foundPostEntity.editPost(null, title, content);
        } else {
            String imageName = imageUtilityComponent.generateImageName(image);

            foundPostEntity.editPost(bucketUrl + imageName, title, content);

            s3Service.uploadFile(image, imageName);
        }

        if (currentImageUrl != null) {
            s3Service.deleteFile(getCurrentFileName(currentImageUrl));
        }
    }

    /* query: 5
     * 1. JPA, 사용자 존재 유무 확인
     * 2. JPA, 게시글 조회
     * 3. JPA. 게시글에 작성한 댓글 삭제
     * 4-1. JPA, 게시글 조회
     * 4-2. JPA, 게시글 삭제
     */
    @Transactional
    public void deletePost(long id, long memberId) {
        if (!memberJpaRepository.existsById(memberId)) {
            throw new MemberNotFoundException();
        }

        PostEntity foundPostEntity = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        if (foundPostEntity.getMemberEntity().getId() != memberId) {
            throw new NotAuthorizedException();
        }

        commentJpaRepository.deleteAllByPostId(id);
        postJpaRepository.delete(foundPostEntity);

        s3Service.deleteFile(getCurrentFileName(foundPostEntity.getImageUrl()));
    }

    private String getCurrentFileName(String imageUrl) {
        try {
            URL url = new URL(imageUrl);

            return url.getFile().substring(1);
        } catch (MalformedURLException e) {
            throw new ServerErrorException("failed to parse image url", e);
        }
    }
}
