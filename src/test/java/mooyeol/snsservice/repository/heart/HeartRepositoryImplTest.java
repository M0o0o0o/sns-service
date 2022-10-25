package mooyeol.snsservice.repository.heart;

import mooyeol.snsservice.domain.Heart;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.repository.HeartRepository;
import mooyeol.snsservice.repository.MemberRepository;
import mooyeol.snsservice.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class HeartRepositoryImplTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    HeartRepository heartRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("좋아요 저장")
    public void success_heart_save() {
        // given
        Member member = makeMember();
        Post post = makePost();
        em.persist(member);

        post.setMember(member);
        em.persist(post);

        // when
        Heart heart = new Heart();
        heart.setPost(post);
        heart.setMember(member);
        heartRepository.save(heart);

        // then
        assertEquals(heart, em.find(Heart.class, heart.getId()));
    }

    @Test
    @DisplayName("좋아요 삭제")
    public void success_heart_delete() {
        //given
        Member member = makeMember();
        Post post = makePost();

        em.persist(member);
        post.setMember(member);
        em.persist(post);

        Heart heart = new Heart();
        heart.setMember(member);
        heart.setPost(post);

        em.persist(heart);
        heartRepository.delete(heart);

        //when
        Optional<Heart> OptionalHeart = heartRepository.findHeartByMemberAndPost(member, post);

        //then
        assertTrue(OptionalHeart.isEmpty());

    }

    @Test
    @DisplayName("좋아요 조회")
    public void success_heart_find() {
        //given
        Member member = makeMember();
        Post post = makePost();

        em.persist(member);
        post.setMember(member);
        em.persist(post);

        Heart heart = new Heart();
        heart.setPost(post);
        heart.setMember(member);

        em.persist(heart);

        //when
        Optional<Heart> optionalHeart = heartRepository.findHeartByMemberAndPost(member, post);

        //then
        assertTrue(optionalHeart.isPresent());
        assertEquals(heart, optionalHeart.get());

    }

    @Test
    @DisplayName("존재하지 않는 좋아요 조회")
    public void failure_find_heart() {
        //given
        Member member = makeMember();
        Post post = makePost();

        em.persist(member);
        post.setMember(member);
        em.persist(post);

        //when
        Optional<Heart> heart = heartRepository.findHeartByMemberAndPost(member, post);

        //then
        assertTrue(heart.isEmpty());
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    public void success_save_post() {
        //given
        Post post = makePost();

        //when
        postRepository.savePost(post);

        //then
        assertEquals(post, em.find(Post.class, post.getId()));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void success_delete_post() {
        //given
        Post post = makePost();
        em.persist(post);

        //when
        postRepository.deletePost(post);

        //then
        assertNull(em.find(Post.class, post.getId()));

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