package mooyeol.snsservice.repository.heart;

import mooyeol.snsservice.domain.Heart;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Optional;

/**
 * @Todo Cascade 설정(member - heart - post)
 */
public class HeartRepositoryImpl implements HeartRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Optional<Heart> findHeart(Member member, Post post) {
        try {
            String jpql = "select h from Heart h where " +
                    "h.member.id = :memberId " +
                    "and h.post.id = :postId";
            TypedQuery<Heart> query = em.createQuery(jpql, Heart.class)
                    .setParameter("memberId", member.getId())
                    .setParameter("postId", post.getId());
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void saveHeart(Heart heart) {
        em.persist(heart);
    }

    @Override
    public void deleteHeart(Heart heart) {
        em.remove(heart);
    }
}
