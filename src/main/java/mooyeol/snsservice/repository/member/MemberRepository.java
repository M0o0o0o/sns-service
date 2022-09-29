package mooyeol.snsservice.repository.member;


import mooyeol.snsservice.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findByEmail(String email);

}
