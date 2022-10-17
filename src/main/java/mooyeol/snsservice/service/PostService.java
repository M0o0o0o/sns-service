package mooyeol.snsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.controller.PostUpdateDto;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.domain.PostTag;
import mooyeol.snsservice.domain.Tag;
import mooyeol.snsservice.repository.post.PostRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    /**
     * 1. hahsTags가 들어왔다면 Tag 테이블에서 존재하지 않는 tag들을 찾는다.
     *  1.1 존재하지 않는 tag가 있다면 tag를 생성한다.
     */
    private final PostRepository postRepository;

    @Transactional
    public Optional<Post> addPost(Post post, List<String> hashtags) {
        postRepository.savePost(post);

        setHashTags(post, hashtags);
        return Optional.of(post);
    }


    @Transactional
    public Post updatePost(Long id, PostUpdateDto postDto, List<String> hashTags, Member member) {
        Post post = postRepository.findPost(id);

        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        if (member.getId() != post.getMember().getId()) {
            throw new AccessDeniedException("글 작성자가 아닙니다.");
        }

        if(postDto.getTitle() != null) post.setTitle(postDto.getTitle());
        if(postDto.getContent() != null) post.setContent(postDto.getContent());
        if(hashTags != null) setHashTags(post, hashTags);

        return post;
    }


    @Transactional
    public void deletePost(Long id, Member member) {
        Post post = postRepository.findPost(id);
        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        if (member.getId() != post.getMember().getId()) {
            throw new AccessDeniedException("글 작성자가 아닙니다.");
        }
        postRepository.deletePost(post);
    }

    @Transactional
    public Optional<Post> findPost(Long id, boolean isLoggedIn) {
        Optional<Post> optionalPost = postRepository.findPostWithHashTags(id);


        if (isLoggedIn && optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setViews(post.getViews() + 1);
        }

        return optionalPost;
    }

    private void setHashTags(Post post, List<String> hashtags) {
        if (hashtags != null) {
            List<Tag> existTags = postRepository.findTags(hashtags);
            // 이미 존재하는 TAG를 제외하고 새로 생성해야 하는데 지금은 중복으로 들어간다.
            for (String hashtag : hashtags) {
                if (!existTags.contains(hashtag)) {
                    Tag tag = new Tag();
                    tag.setName(hashtag);
                    postRepository.saveTag(tag);
                    existTags.add(tag);
                }
            }

            for (Tag tag : existTags) {
                PostTag postTag = new PostTag();
                postTag.setPost(post);
                postTag.setTag(tag);
                postRepository.savePostTag(postTag);
            }
        }
    }
}

