package mooyeol.snsservice.repository.post;

import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.controller.PostConditionDto;
import mooyeol.snsservice.controller.PostListDto;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.domain.PostTag;
import mooyeol.snsservice.domain.Tag;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PostRepositoryImpl implements PostRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Post findPost(Long id) {
        try {
            String jpql = "SELECT p FROM Post p join fetch p.member where p.id = :id";
            TypedQuery<Post> query = em.createQuery(jpql, Post.class);
            return query.setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Optional<Post> findPostWithHashTags(Long id) {
        try {
            String queryStr = "SELECT distinct p FROM Post p join fetch p.postTags where p.id = :id";
            TypedQuery<Post> query = em.createQuery(queryStr, Post.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<PostListDto> findPosts(String order, String desc, String search, List<String> listHashTags, int page, int cnt) {
        String jpql = "select distinct p.id, p.title, p.content, p.createdDate, p.member.email, p.hearts, p.views from Post p join p.postTags t join p.member m" +
                " where p.id = t.post.id and p.member.id = m.id";

        if (search != null) {
            jpql += " and p.title like :search";
        }

        if (listHashTags != null) {
            for (int i = 0; i < listHashTags.size(); i++) {
                jpql += " and t.postTagName = :tag" + i;
            }
        }

        switch (order) {
            case "date":
                jpql += " order by p.createdDate ";
                break;
            case "heart":
                jpql += " order by p.hearts ";
                break;
            case "view":
                jpql += " order by p.views ";
        }

        if (desc.equals("true")) {
            jpql += "desc";
        }else {
            jpql += "asc";
        }

        Query query = em.createQuery(jpql);

        if (search != null) {
            query.setParameter("search", "%" + search + "%");
        }

        if (listHashTags != null) {
            for (int i = 0; i < listHashTags.size(); i++) {
                query.setParameter("tag" + i, listHashTags.get(i));
            }
        }

        List<PostListDto> results = new ArrayList<>();
        for (Object o : query.setFirstResult((page - 1) * cnt).setMaxResults(cnt).getResultList()) {
            Object[] result = (Object[]) o;
            PostListDto dto = new PostListDto();
            dto.setId((long) result[0]);
            dto.setTitle((String) result[1]);
            dto.setContent((String) result[2]);
            dto.setCreatedDate((LocalDateTime) result[3]);
            dto.setWriter((String) result[4]);
            dto.setHearts((long) result[5]);
            dto.setViews((long) result[6]);
            results.add(dto);
        }
        return results;
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
