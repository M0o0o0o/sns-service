package mooyeol.snsservice.controller;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class PostUpdateDto {

    @Size(max=255, message = "{Size.postAddDto.title}")
    private String title;

    @Size(max=255, message = "{Size.postAddDto.content}")
    private String content;

    private String hashTags;

}
