package mooyeol.snsservice.service;

import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void join(Member member) {
        /**
         * memberDto로 넘어온 이메일이 중복되는 지 확인 후에 repository를 통해 멤버를 생성한다.
         */
        checkDuplicatedEmail(member.getEmail());
        memberRepository.save(member);
    }

    public void checkDuplicatedEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    }

}
