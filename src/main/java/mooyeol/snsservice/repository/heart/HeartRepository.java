package mooyeol.snsservice.repository.heart;

import mooyeol.snsservice.domain.Heart;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.domain.Post;

import java.util.Optional;

public interface HeartRepository {
    Optional<Heart> findHeart(Member member, Post post);

    void saveHeart(Heart heart);

    void deleteHeart(Heart heart);
}
