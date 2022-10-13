package mooyeol.snsservice.config;

import mooyeol.snsservice.repository.member.H2MemberRepository;
import mooyeol.snsservice.repository.member.MemberRepository;
import mooyeol.snsservice.repository.post.PostRepository;
import mooyeol.snsservice.repository.post.PostRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public MemberRepository memberRepository() {
        return new H2MemberRepository();
    }

    @Bean
    public PostRepository postRepository() {
        return new PostRepositoryImpl();
    }
}
