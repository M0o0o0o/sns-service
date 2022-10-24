package mooyeol.snsservice.repository.post;

import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 조회 성공 테스트")
    public void success_find_post() {
        //given
        Post post = makePost();
        Member member = makeMember();

        em.persist(member);
        post.setMember(member);
        em.persist(post);

        //when
        postRepository.findPost(post.getId());

        //then
        assertEquals(post, em.find(Post.class, post.getId()));

    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    public void failure_find_post() {
        //given
        Post post = makePost();
        em.persist(post);
        em.remove(post);

        //when
        Post findPost = postRepository.findPost(post.getId());

        //then
        assertNull(findPost);
    }


    private Member makeMember() {
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword("testpwd");

        return member;
    }

    private Post makePost() {
        Post post = new Post();
        post.setTitle("test title");
        post.setContent("test content");
        return post;
    }

}