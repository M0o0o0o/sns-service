package mooyeol.snsservice.repository;


import mooyeol.snsservice.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
