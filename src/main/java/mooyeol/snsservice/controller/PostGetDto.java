package mooyeol.snsservice.controller;

import lombok.Getter;
import mooyeol.snsservice.domain.Post;
import mooyeol.snsservice.domain.PostTag;

import java.util.Optional;

@Getter
public class PostGetDto {
    private long id;
    private String title;
    private String content;
    private long hearts;
    private long views;
    private String hashTags;


    public PostGetDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hearts = post.getHearts();
        this.views = post.getViews();

        StringBuilder sb = new StringBuilder();
        for (PostTag postTag : post.getPostTags()) {
            sb.append(postTag.getPostTagName() + ",");
        }
        this.hashTags = sb.toString();
    }
}
