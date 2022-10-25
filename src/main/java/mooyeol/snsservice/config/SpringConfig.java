package mooyeol.snsservice.config;

import mooyeol.snsservice.repository.post.PostRepository;
import mooyeol.snsservice.repository.post.PostRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public PostRepository postRepository() {
        return new PostRepositoryImpl();
    }
}
