package mooyeol.snsservice.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import mooyeol.snsservice.domain.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class PostAddDto {

    @NotBlank(message = "{NotBlank.postAddDto.title}")
    @Size(max=255, message = "{Size.postAddDto.title}")
    private String title;

    @NotBlank(message = "{NotBlank.postAddDto.content}")
    @Size(max=255, message = "{Size.postAddDto.content}")
    private String content;

    private String hashTags;

    public Post toEntity() {
        Post post = new Post();
        post.setTitle(this.title);
        post.setContent(this.content);
        return post;
    }
}
