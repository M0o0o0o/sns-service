package mooyeol.snsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mooyeol.snsservice.domain.Member;
import mooyeol.snsservice.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(Member member) {
        checkDuplicatedEmail(member.getEmail());
        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return memberRepository.save(member);
    }

    public void checkDuplicatedEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(m -> {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        });
    }

}
