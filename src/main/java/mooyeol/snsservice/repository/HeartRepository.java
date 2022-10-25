package mooyeol.snsservice.repository;

import mooyeol.snsservice.domain.Heart;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findHeartByMemberAndPost(Member member, Post post);
}
