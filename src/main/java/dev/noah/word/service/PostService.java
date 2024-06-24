package dev.noah.word.service;

import dev.noah.word.entity.MemberEntity;
import dev.noah.word.entity.PostEntity;
import dev.noah.word.exception.NotAuthorizedException;
import dev.noah.word.exception.MemberNotFoundException;
import dev.noah.word.exception.PostNotFoundException;
import dev.noah.word.repository.MemberJpaRepository;
import dev.noah.word.repository.PostJpaRepository;
import dev.noah.word.response.SearchAllPostResponse;
import dev.noah.word.response.SearchPostResponse;
import dev.noah.word.service.utils.ImageUtilityComponent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class PostService {

    private static final String POST_IMAGE_SAVE_DIRECTORY_PATH = "/post-images/";
    private static final String POST_IMAGE_SAVE_RELATIVE_PATH = "src/main/resources/public" + POST_IMAGE_SAVE_DIRECTORY_PATH;

    private final PostJpaRepository postJpaRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ImageUtilityComponent imageUtilityComponent;

    public List<SearchAllPostResponse> searchAll() {
        // TODO comments 처리 필요
        return postJpaRepository.findAll().stream()
                .map(postEntity -> new SearchAllPostResponse(
                        postEntity.getId(),
                        postEntity.getMemberEntity().getNickname(),
                        postEntity.getMemberEntity().getImageUrl(),
                        postEntity.getCreatedDate(),
                        postEntity.getTitle(),
                        postEntity.getViews(),
                        postEntity.getLikes(),
                        0))
                .toList();
    }

    @Transactional
    public SearchPostResponse searchById(long id) {
        // TODO comments 처리 필요
        PostEntity foundPostEntity = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        // TODO 고민
        foundPostEntity.increaseViews();
        postJpaRepository.save(foundPostEntity);

        return new SearchPostResponse(
                foundPostEntity.getId(),
                foundPostEntity.getMemberEntity().getNickname(),
                foundPostEntity.getMemberEntity().getNickname(),
                foundPostEntity.getCreatedDate(),
                foundPostEntity.getTitle(),
                foundPostEntity.getContent(),
                foundPostEntity.getViews(),
                0
        );
    }

    @Transactional
    public void createPost(long memberId, MultipartFile image, String title, String content) {
        MemberEntity foundMemberEntity = memberJpaRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        String imageUrl = imageUtilityComponent
                .saveImageAndReturnImageUrl(image, POST_IMAGE_SAVE_DIRECTORY_PATH, POST_IMAGE_SAVE_RELATIVE_PATH);

        postJpaRepository.save(new PostEntity(foundMemberEntity, imageUrl, title, content));
    }


    @Transactional
    public void editPost(long id, long memberId, MultipartFile image, String title, String content) {
        PostEntity foundPostEntity = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        if (foundPostEntity.getMemberEntity().getId() != memberId) {
            throw new NotAuthorizedException();
        }

        String imageUrl = imageUtilityComponent
                .saveImageAndReturnImageUrl(image, POST_IMAGE_SAVE_DIRECTORY_PATH, POST_IMAGE_SAVE_RELATIVE_PATH);

        // INFO: 사실 이미지를 삭제하면 복구할 방법이 없다. 소프르 삭제를 수행해야 한다.
        imageUtilityComponent.deleteImage(foundPostEntity.getImageUrl());

        foundPostEntity.editPost(imageUrl, title, content);

        postJpaRepository.save(foundPostEntity);
    }

    @Transactional
    public void deletePost(long id, long memberId) {
        PostEntity foundPostEntity = postJpaRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        if (foundPostEntity.getMemberEntity().getId() != memberId) {
            throw new NotAuthorizedException();
        }

        postJpaRepository.deleteById(id);

        // INFO: 사실 이미지를 삭제하면 복구할 방법이 없다. 소프르 삭제를 수행해야 한다.
        imageUtilityComponent.deleteImage(foundPostEntity.getImageUrl());
    }
}
