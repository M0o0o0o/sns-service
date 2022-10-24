package mooyeol.snsservice.service;

import mooyeol.snsservice.controller.PostUpdateDto;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.repository.heart.HeartRepository;
import mooyeol.snsservice.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private HeartRepository heartRepository;

    @InjectMocks
    private PostService postService;

    /**
     * @Todo postService.addPost()는 현재 이렇게 할 서비스 로직이 없기에 일단 제외하고 테스트를 진행했다.
     */

    /**
     * 게시글 업데이트 Tests
     */

    @Test
    @DisplayName("게시글 업데이트 후 IllegalArgumentException 발생")
    public void update_post_throw_IllegalArgumentException() {
        //given
        PostUpdateDto dto = new PostUpdateDto("Test Title", "Test Content", null);
        when(postRepository.findPost(any())).thenReturn(null);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(1L, dto, new ArrayList<>(), new Member());
        });
    }

    @Test
    @DisplayName("게시글 업데이트 중 AccessDeniedException 발생")
    public void update_post_throw_AccessDeniedException() {
        //given
        Member requestMember = new Member();
        requestMember.setId(1L);

        Member ownerOfPost = new Member();
        ownerOfPost.setId(2L);

        Post findPost = new Post();
        findPost.setMember(ownerOfPost);

        when(postRepository.findPost(any())).thenReturn(findPost);

        //when, then
        assertThrows(AccessDeniedException.class, () -> postService.updatePost(1L, new PostUpdateDto(), null, requestMember));
    }

    @Test
    @DisplayName("게시글 업데이트 성공 테스트")
    public void success_update_post() {

        //given
        String afterTitle = "after Title";
        String afterContent = "after content";

        PostUpdateDto dto = new PostUpdateDto(afterTitle, afterContent, null);

        Post findPost = new Post();
        findPost.setTitle("before title");
        findPost.setContent("before content");

        Member requestMember = new Member();
        requestMember.setId(1L);
        findPost.setMember(requestMember);

        when(postRepository.findPost(any())).thenReturn(findPost);

        //when
        Post post = postService.updatePost(1L, dto, null, requestMember);

        //then
        assertEquals(afterTitle, post.getTitle());
        assertEquals(afterContent, post.getContent());
    }

    /**
     * 게시글 삭제 Tests
     */

    @Test
    @DisplayName("게시글 삭제 중 IllegalArgumentException 발생 테스트")
    public void delete_post_throw_IllegalArgumentException() {
        //given
        when(postRepository.findPost(any())).thenReturn(null);

        //when, then
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(1L, null));
    }

    @Test
    @DisplayName("게시글 삭제 중 게시글 작성자가 아닌 경우(AccessDeniedException)")
    public void delete_post_throw_AccessDeniedException() {
        //given
        Member ownerOfPost = new Member();
        ownerOfPost.setId(1L);

        Member requestMember = new Member();
        requestMember.setId(2L);

        Post findPost = new Post();
        findPost.setMember(ownerOfPost);

        when(postRepository.findPost(any())).thenReturn(findPost);

        //when, then
        assertThrows(AccessDeniedException.class, () -> postService.deletePost(1L, requestMember));
    }

    /**
     * 단일 게시글 조회 테스트
     */
    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    public void failure_post_find() {
        //given
        when(postRepository.findPostWithHashTags(any())).thenReturn(Optional.empty());

        //when
        Optional<Post> post = postService.findPost(1L, false);

        //then
        assertTrue(post.isEmpty());
    }


    @Test
    @DisplayName("게시글 조회에 성공하지만, 로그인한 회원이 아닌 경우 view 증가 X")
    public void success_post_find_not_increase_view() {
        //given
        int views = 15;
        Post post = new Post();
        post.setViews(views);

        when(postRepository.findPostWithHashTags(any())).thenReturn(Optional.of(post));

        //when
        Optional<Post> optionalPost = postService.findPost(1L, false);

        //then
        assertTrue(optionalPost.isPresent());
        assertEquals(views, optionalPost.get().getViews());
    }

    @Test
    @DisplayName("게시글 조회도 성공하고 조회수도 1 증가하는 경우")
    public void success_post_find_increase_view() {
        int views = 15;
        Post post = new Post();
        post.setViews(views++);

        when(postRepository.findPostWithHashTags(any())).thenReturn(Optional.of(post));

        //when
        Optional<Post> optionalPost = postService.findPost(1L, true);

        //then
        assertTrue(optionalPost.isPresent());
        assertEquals(views, optionalPost.get().getViews());

    }
}