package mooyeol.snsservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import mooyeol.snsservice.domain.Post;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PostUpdateDto {

    @Size(max=255, message = "{Size.postAddDto.title}")
    private String title;

    @Size(max=255, message = "{Size.postAddDto.content}")
    private String content;

    private String hashTags;

    public PostUpdateDto(String title, String content, String hashTags) {
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
    }

    public Post toEntity() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setContent(this.content);

        return post;
    }
}
