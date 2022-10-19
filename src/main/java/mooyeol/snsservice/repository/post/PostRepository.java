package mooyeol.snsservice.repository.post;

import mooyeol.snsservice.controller.PostConditionDto;
import mooyeol.snsservice.controller.PostListDto;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.domain.PostTag;
import mooyeol.snsservice.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    // add Post
    void savePost(Post post);

    // delete post
    void deletePost(Post post);

    // find not int tags()
    List<Tag> findTags(List<String> hashTags);


    // add tag
    void saveTag(Tag tag);

    void savePostTag(PostTag postTag);


    Post findPost(Long id);

    Optional<Post> findPostWithHashTags(Long id);

    List<PostListDto> findPosts(String order, String desc, String search, List<String> listHashTags, int page, int cnt);
}
