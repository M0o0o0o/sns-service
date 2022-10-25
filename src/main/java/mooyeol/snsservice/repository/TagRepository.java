package mooyeol.snsservice.repository;

import mooyeol.snsservice.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
