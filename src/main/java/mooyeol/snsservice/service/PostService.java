package mooyeol.snsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.dto.PostConditionDto;
import mooyeol.snsservice.dto.PostListDto;
import mooyeol.snsservice.dto.PostUpdateDto;
import mooyeol.snsservice.domain.*;
import mooyeol.snsservice.repository.HeartRepository;
import mooyeol.snsservice.repository.PostRepository;
import mooyeol.snsservice.repository.PostTagRepository;
import mooyeol.snsservice.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HeartRepository heartRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

    @Transactional
    public Optional<Post> addPost(Post post, List<String> hashtags, Object principal) {
        post.setMember((Member) principal);

        postRepository.save(post);

        setHashTags(post, hashtags);
        return Optional.of(post);
    }


    @Transactional
    public Post updatePost(Long id, PostUpdateDto postDto, List<String> hashTags, Member member) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        Post post = optionalPost.get();

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
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        Post post = optionalPost.get();

        if (member.getId() != post.getMember().getId()) {
            throw new AccessDeniedException("글 작성자가 아닙니다.");
        }
        postRepository.delete(post);
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
    public Page<PostListDto> findPosts(PostConditionDto c) {
        PageRequest pageRequest = PageRequest.of(c.getPage(), c.getCnt(), getSort(c));
        Page<PostListDto> posts = postRepository.findPosts(pageRequest, c);
        return posts;
    }

    private Sort getSort(PostConditionDto condition) {
        return Sort.by(condition.getDesc().equals("true") ? Sort.Direction.DESC : Sort.Direction.ASC, condition.getOrder());
    }


    @Transactional
    public void updateHeart(Long id, Member member) {
        Optional<Post> optionalPost = postRepository.findById(id);

        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

        Post post = optionalPost.get();

        Optional<Heart> heart = heartRepository.findHeartByMemberAndPost(member, post);

        if(heart.isPresent()){
            heartRepository.delete(heart.get());
            post.setHearts(post.getHearts() - 1);
            return;
        }

        Heart newHeart = new Heart();
        newHeart.setPost(post);
        newHeart.setMember(member);
        post.setHearts(post.getHearts() + 1);
        heartRepository.save(newHeart);
    }

    private void setHashTags(Post post, List<String> hashTags) {
        if(hashTags == null) return;

        List<Tag> existTags = tagRepository.findTags(hashTags);

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
            tagRepository.save(tag);
            existTags.add(tag);
        }

        for (Tag tag : existTags) {
            PostTag postTag = new PostTag();
            postTag.setPost(post);
            postTag.setTag(tag);
            postTag.setPostTagName(tag.getName());
            postTagRepository.save(postTag);
        }
    }
}

