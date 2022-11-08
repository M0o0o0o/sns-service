package mooyeol.snsservice.repository.post;

import mooyeol.snsservice.config.TestConfig;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
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

        em.flush();
        em.clear();

        //when
        Optional<Post> optionalPost = postRepository.findById(post.getId());

        //then
        assertTrue(optionalPost.isPresent());
        assertEquals(post.getId(), optionalPost.get().getId());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    public void failure_find_post() {
        //given
        Post post = makePost();
        em.persist(post);
        em.remove(post);

        //when
        Optional<Post> optionalPost = postRepository.findById(post.getId());

        //then
        assertTrue(optionalPost.isEmpty());
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