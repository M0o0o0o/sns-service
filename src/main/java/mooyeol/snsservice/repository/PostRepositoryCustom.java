package mooyeol.snsservice.repository;

import mooyeol.snsservice.dto.PostConditionDto;
import mooyeol.snsservice.dto.PostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    Page<PostListDto> findPosts(Pageable pageable, PostConditionDto condition);
}
