package mooyeol.snsservice.repository.member;

import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
public class H2MemberRepository implements MemberRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        try {
            String query = "select m from Member m where m.email=:email";
            Member member = em.createQuery(query, Member.class).setParameter("email", email).getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

}
