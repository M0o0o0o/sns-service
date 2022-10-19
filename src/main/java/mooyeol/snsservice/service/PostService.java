package mooyeol.snsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.controller.PostConditionDto;
import mooyeol.snsservice.controller.PostListDto;
import mooyeol.snsservice.controller.PostUpdateDto;
import mooyeol.snsservice.domain.*;
import mooyeol.snsservice.repository.heart.HeartRepository;
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

    private final PostRepository postRepository;
    private final HeartRepository heartRepository;

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
        Optional<Post> OptionalPost = postRepository.findPostWithHashTags(id);

        if (OptionalPost.isPresent() && isLoggedIn) {
            Post post = OptionalPost.get();
            post.setViews(post.getViews() + 1);
        }
        return OptionalPost;
    }

    @Transactional
    public List<PostListDto> findPosts(PostConditionDto c) {
        List<PostListDto> posts = postRepository.findPosts(c.getOrder(), c.getDesc(), c.getSearch(), c.getListHashTags(), c.getPage(), c.getCnt());
        return posts;
    }

    private void setHashTags(Post post, List<String> hashTags) {
        if(hashTags == null) return;

        List<Tag> existTags = postRepository.findTags(hashTags);

        for (String hashTag : hashTags) {
            boolean isExist = false;
            for (Tag existTag : existTags) {
                if (existTag.getName().equals(hashTag)) {
                    isExist = true;
                    break;
                }
            }

            if(isExist) continue;

            Tag tag = new Tag();
            tag.setName(hashTag);
            postRepository.saveTag(tag);
            existTags.add(tag);
        }

        for (Tag tag : existTags) {
            PostTag postTag = new PostTag();
            postTag.setPost(post);
            postTag.setTag(tag);
            postTag.setPostTagName(tag.getName());
            postRepository.savePostTag(postTag);
        }

    }

    @Transactional
    public void updateHeart(Long id, Member member) {
        Post post = postRepository.findPost(id);
        if (post == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        Optional<Heart> heart = heartRepository.findHeart(member, post);

        if(heart.isPresent()){
            heartRepository.deleteHeart(heart.get());
            post.setHearts(post.getHearts() - 1);
            return;
        }

        Heart newHeart = new Heart();
        newHeart.setPost(post);
        newHeart.setMember(member);
        post.setHearts(post.getHearts() + 1);
        heartRepository.saveHeart(newHeart);
    }
}

