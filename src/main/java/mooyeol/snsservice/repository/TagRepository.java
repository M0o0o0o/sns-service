package mooyeol.snsservice.repository;

import mooyeol.snsservice.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t WHERE t.name IN :hashTags")
    List<Tag> findTags(@Param("hashTags") List<String> hashTags);
}
