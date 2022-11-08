package mooyeol.snsservice.repository.member;

import mooyeol.snsservice.config.TestConfig;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class H2MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    private PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void success_save_member() {
        //given
        String uniqueId = UUID.randomUUID().toString();

        Member member = new Member();
        member.setEmail(uniqueId + "@test.com");
        member.setPassword(delegatingPasswordEncoder.encode("pwd"));

        //when
        memberRepository.save(member);

        //then
        assertEquals(member, em.find(Member.class, member.getId()));

    }

    @Test
    @DisplayName("이메일로 멤버 조회 성공 테스트")
    public void success_find_by_email() {
        String uniqueId = UUID.randomUUID().toString();

        Member member = new Member();
        member.setEmail(uniqueId + "@test.com");
        member.setPassword(delegatingPasswordEncoder.encode("pwd"));

        em.persist(member);

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(member.getEmail());

        //then
        assertTrue(optionalMember.isPresent());
        assertEquals(member, optionalMember.get());

    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원조회 테스트")
    public void failure_find_by_email() {
        //given
        String uniqueId = UUID.randomUUID().toString();

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(uniqueId);

        //then
        assertTrue(optionalMember.isEmpty());

    }
}