package mooyeol.snsservice.repository;

import mooyeol.snsservice.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom{
    @Query("select distinct p from Post p join fetch p.postTags where p.id = :id")
    Optional<Post> findPostWithHashTags(@Param("id") Long id);
}
