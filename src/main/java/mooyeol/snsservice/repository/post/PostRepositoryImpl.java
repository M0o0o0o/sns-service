package mooyeol.snsservice.repository.post;

import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.controller.PostConditionDto;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.domain.PostTag;
import mooyeol.snsservice.domain.Tag;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostRepositoryImpl implements PostRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Post findPost(Long id) {
        return em.find(Post.class, id);
    }

    @Override
    public Optional<Post> findPostWithHashTags(Long id) {
        try {
            String queryStr = "SELECT p FROM Post p join fetch p.postTags where p.id = :id";
            TypedQuery<Post> query = em.createQuery(queryStr, Post.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void savePost(Post post) {
        em.persist(post);
    }


    @Override
    public void deletePost(Post post) {
        em.remove(post);
    }

    @Override
    public List<Tag> findTags(List<String> hashTags) {
        String queryStr = "SELECT t FROM Tag t WHERE t.name IN :hashTags";
        TypedQuery<Tag>  query = em.createQuery(queryStr, Tag.class);
        query.setParameter("hashTags", hashTags);
        return query.getResultList();

    }

    @Override
    public void saveTag(Tag tag) {
        em.persist(tag);
    }

    @Override
    public void savePostTag(PostTag postTag) {
        em.persist(postTag);
        postTag.getPost().getPostTags().add(postTag);
    }

}
